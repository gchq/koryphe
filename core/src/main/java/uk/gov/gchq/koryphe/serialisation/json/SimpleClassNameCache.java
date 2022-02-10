/*
 * Copyright 2018-2022 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.koryphe.serialisation.json;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import uk.gov.gchq.koryphe.util.ReflectionUtil;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * A {@code SimpleClassNameCache} is a cache for indexing simple class names.
 * </p>
 * <p>
 * If 2 classes are discovered to have the same simple class name then
 * a useful exception is thrown telling the user to choose which class they want
 * and use the full class name.
 * </p>
 * <p>
 * This class will scan the class path to resolve the simple class names and cache
 * the results. To allow your class to be included in the resolver you need to
 * annotate it with {@link JsonSimpleClassName} or call {@link #addSimpleClassNames(boolean, Class[])}
 * </p>
 * <p>
 * In addition to classes annotated with JsonSimpleClassName, implementations
 * of {@link Predicate}, {@link Function} and {@link BinaryOperator} are included.
 * All classes within java.lang and java.util are also included.
 * </p>
 * <p>
 * By default the full class name will be serialised into json. To override this
 * call {@link #setUseFullNameForSerialisation(boolean)}.
 * </p>
 * <p>
 * Only a small set of packages on the class path are scanned.
 * You can add extra packages to the scanner. See {@link ReflectionUtil} for details.
 * </p>
 *
 * @see ReflectionUtil
 */
public final class SimpleClassNameCache {
    public static final boolean DEFAULT_USE_FULL_NAME_FOR_SERIALISATION = true;

    private static final Set<Class> DEFAULT_PARENT_CLASSES = Set.of(
            Predicate.class,
            Function.class,
            BinaryOperator.class,
            Comparator.class
    );

    private static final Set<String> DEFAULT_CORE_PACKAGES = Set.of("java.lang", "java.util");

    /**
     * Core packages such as java.lang that will be used to try and expand unknown
     * simple class names.
     */
    private static Set<String> corePackages = new LinkedHashSet<>(DEFAULT_CORE_PACKAGES);

    /**
     * Base classes to use to find implementations to be added to idToClasses
     */
    private static Set<Class> baseClasses = ConcurrentHashMap.newKeySet();

    /**
     * Map of simple class name to classes.
     */
    private static Map<String, Set<Class>> idToClasses = new ConcurrentHashMap<>();

    /**
     * If conflicts are found in idToClasses this will be used to try to
     * differentiate the classes.
     */
    private static Map<Class<?>, Map<String, Class>> baseTypeToIdToClass = new ConcurrentHashMap<>();

    /**
     * If true then the full class name is used for serialisation.
     * The default is true, this could be changed to true in the next major release.
     */
    private static boolean useFullNameForSerialisation = DEFAULT_USE_FULL_NAME_FOR_SERIALISATION;

    private static boolean initialised = false;

    private SimpleClassNameCache() {
    }

    public static void initialise() {
        if (!initialised) {
            baseClasses = createParentClasses();
            idToClasses = createIdToClasses();
            initialised = true;
        }
    }

    /**
     * @return if true then the full class name is used for serialisation.
     */
    public static boolean isUseFullNameForSerialisation() {
        return useFullNameForSerialisation;
    }


    /**
     * @param useFullNameForSerialisation if true then the full class name is used for serialisation.
     */
    public static void setUseFullNameForSerialisation(final boolean useFullNameForSerialisation) {
        SimpleClassNameCache.useFullNameForSerialisation = useFullNameForSerialisation;
    }

    /**
     * Allows you to add additional classes to the cache that you wish to be
     * accessed via the simple class name.
     *
     * @param includeSubtypes if true then all implementations are included.
     * @param classes         the classes to be included.
     */
    public static void addSimpleClassNames(final boolean includeSubtypes, final Class... classes) {
        initialise();
        for (final Class clazz : classes) {
            final Set<Class> existingClasses = getClassesFromId(clazz.getSimpleName());
            if (null == existingClasses || !existingClasses.contains(clazz)) {
                if (includeSubtypes) {
                    addSimpleClassNames(idToClasses, clazz);
                } else {
                    addSimpleClassName(idToClasses, clazz);
                }
            }
        }
    }

    /**
     * Gets a simple class name for the given class.
     *
     * @param clazz the class to get the simple class name for.
     * @return the simple class name, or null a simple class name can't be used.
     */
    public static String getSimpleClassName(final Class<?> clazz) {
        if (null == clazz) {
            return null;
        }

        final String simpleClassName = getSimpleClassNameOrNull(clazz);

        // If we can't find a simple class name, then just return the full class name.
        return null != simpleClassName ? simpleClassName : clazz.getName();
    }

    public static String getSimpleClassNameOrNull(final Class<?> clazz) {
        String id;
        if (null == clazz || useFullNameForSerialisation) {
            id = null;
        } else {
            initialise();

            // If the class is an array, use the component type and we will
            // add the array brackets at the end.
            final boolean isArray = null != clazz.getComponentType();
            final Class<?> nonArrayClass = isArray ? clazz.getComponentType() : clazz;
            id = nonArrayClass.getSimpleName();

            final Set<Class> classesForId = getClassesFromId(id);
            if (null == classesForId || classesForId.isEmpty()) {
                // If the class is unknown, check if the class is in one of the core packages
                final Package classPackage = nonArrayClass.getPackage();
                if (null != classPackage && corePackages.contains(classPackage.getName())) {
                    // Found the class, so cache the result for next time.
                    addIdClasses(id, Sets.newHashSet(nonArrayClass));
                } else {
                    id = null;
                }
            } else if (1 != classesForId.size()) {
                // If there are multiple classes with the same class name then
                // we can't return a simple class name.
                id = null;
            }

            if (isArray && null != id) {
                id = id + "[]";
            }
        }
        return id;
    }

    /**
     * Gets the class name for a given id.
     *
     * @param id the simple class name or full class name.
     * @return the full class name or the original id if one could not be found.
     * @throws IllegalArgumentException if there are multiple classes with the same id.
     */
    public static String getClassName(final String id) {
        return getClassName(id, null);
    }


    /**
     * Gets the class name for a given id.
     *
     * @param id       the simple class name or full class name.
     * @param baseType the base type of the ID - used to help resolve conflicting IDs
     * @return the full class name or the original id if one could not be found.
     * @throws IllegalArgumentException if there are multiple classes with the same id.
     */
    public static String getClassName(final String id, final JavaType baseType) {
        String className = null;
        if (null != id && !id.contains(".")) {
            initialise();

            // Remove the array brackets if required, these will be added again at the end.
            final boolean isArray = id.endsWith("[]");
            String nonArrayId = isArray ? id.substring(0, id.length() - 2) : id;
            nonArrayId = StringUtils.capitalize(nonArrayId);

            final Set<Class> classesForId = getClassesFromId(nonArrayId);
            // If the class is unknown (not in the cache) then try the core packages
            if (null == classesForId || classesForId.isEmpty()) {
                for (final String corePackage : corePackages) {
                    final String classNameTmp = corePackage + "." + nonArrayId;
                    final Class<?> clazz = ReflectionUtil.getClassFromName(classNameTmp);
                    if (null != clazz) {
                        className = classNameTmp;
                        addIdClasses(nonArrayId, Sets.newHashSet(clazz));
                        break;
                    }
                }
            } else if (1 == classesForId.size()) {
                // There is exactly one class name for the given ID so we are
                // confident that is this the class required.
                className = classesForId.iterator().next().getName();
            } else {
                // If the base type has been provided then attempt to use
                // it to resolve the conflicts
                if (null != baseType) {
                    final Class<?> baseClass = baseType.getRawClass();

                    // First check the conflict cache
                    Map<String, Class> idToClass = baseTypeToIdToClass.get(baseClass);
                    if (null != idToClass) {
                        final Class clazz = getClassFromId(nonArrayId, idToClass);
                        if (null != clazz) {
                            className = clazz.getName();
                        }
                    }

                    // If the conflict has not previously been seen, then try
                    // and resolve it and add it to the cache.
                    if (null == className) {
                        Class<?> matchedClass = null;
                        for (final Class classForId : classesForId) {
                            if (baseClass.isAssignableFrom(classForId)) {
                                if (null == matchedClass) {
                                    matchedClass = classForId;
                                } else {
                                    matchedClass = null;
                                    break;
                                }
                            }
                        }
                        // If the conflict has been resolved then cache the result
                        if (null != matchedClass) {
                            className = matchedClass.getName();
                            if (null == idToClass) {
                                idToClass = new ConcurrentHashMap<>();
                                baseTypeToIdToClass.put(baseClass, idToClass);
                            }
                            addIdClass(nonArrayId, matchedClass, idToClass);
                        }
                    }
                }
                // If the conflict cannot be resolved then explain to the user
                // that they need to provide the full class name.
                if (null == className) {
                    final List<String> classOptions = classesForId.stream().map(c -> isArray ? "[L" + c.getName() + ";" : c.getName()).collect(Collectors.toUnmodifiableList());
                    throw new IllegalArgumentException("Multiple " + nonArrayId + " classes exist. Please choose one of the following and specify the full class name: " + classOptions);
                }
            }

            // Add the array information if required.
            if (isArray && null != className) {
                className = "[L" + className + ";";
            }
        }

        // If we couldn't find the class name, the just return the original value.
        if (null == className) {
            className = id;
        }
        return className;
    }

    /**
     * Resets the caches.
     */
    public static void reset() {
        baseClasses = createParentClasses();
        idToClasses = createIdToClasses();
        corePackages = new LinkedHashSet<>(DEFAULT_CORE_PACKAGES);
        baseTypeToIdToClass = new ConcurrentHashMap<>();
    }

    private static Set<Class> createParentClasses() {
        final Set<Class> newParentClasses = ConcurrentHashMap.newKeySet();
        newParentClasses.addAll(DEFAULT_PARENT_CLASSES);
        for (final Class clazz : ReflectionUtil.getAnnotatedTypes(JsonSimpleClassName.class)) {
            final JsonSimpleClassName anno = (JsonSimpleClassName) clazz.getAnnotation(JsonSimpleClassName.class);
            if (null != anno) {
                if (anno.includeSubtypes()) {
                    newParentClasses.add(clazz);
                }
            }
        }

        return newParentClasses;
    }

    private static Map<String, Set<Class>> createIdToClasses() {
        final Map<String, Set<Class>> map = new ConcurrentHashMap<>();
        for (final Class baseClass : baseClasses) {
            addSimpleClassNames(map, baseClass);
        }
        for (final Class clazz : ReflectionUtil.getAnnotatedTypes(JsonSimpleClassName.class)) {
            addSimpleClassName(map, clazz);
        }

        return map;
    }

    private static void addSimpleClassName(final Map<String, Set<Class>> map, final Class clazz) {
        final String id = StringUtils.capitalize(clazz.getSimpleName());
        final Set<Class> value = map.get(id);
        if (null == value) {
            map.put(id, Sets.newHashSet(clazz));
        } else {
            value.add(clazz);
        }
    }

    private static void addSimpleClassNames(final Map<String, Set<Class>> map, final Class baseClass) {
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(baseClass);
        for (final Map.Entry<String, Set<Class>> entry : simpleClassNames.entrySet()) {
            final String id = StringUtils.capitalize(entry.getKey());
            final Set<Class> value = map.get(id);
            if (null == value) {
                map.put(id, Sets.newHashSet(entry.getValue()));
            } else {
                value.addAll(entry.getValue());
            }
        }
    }

    private static Set<Class> getClassesFromId(final String id) {
        return idToClasses.get(StringUtils.capitalize(id));
    }

    private static Class getClassFromId(final String id, final Map<String, Class> idToClass) {
        return idToClass.get(StringUtils.capitalize(id));
    }

    private static void addIdClasses(final String id, final Set<Class> classes) {
        idToClasses.put(StringUtils.capitalize(id), classes);
    }

    private static void addIdClass(final String id, final Class clazz, final Map<String, Class> idToClass) {
        idToClass.put(StringUtils.capitalize(id), clazz);
    }
}
