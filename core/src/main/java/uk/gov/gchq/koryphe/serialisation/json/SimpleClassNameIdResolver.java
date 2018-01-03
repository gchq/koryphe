/*
 * Copyright 2017 Crown Copyright
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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import uk.gov.gchq.koryphe.util.ReflectionUtil;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>A {@code SimpleClassNameIdResolver} is a {@link TypeIdResolver} that allows
 * simple class names to be used as type ids, rather than needing to provide the
 * entire class name.
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
 */
public class SimpleClassNameIdResolver implements TypeIdResolver {
    private static final Set<Class> DEFAULT_PARENT_CLASSES = Sets.newHashSet(
            Predicate.class,
            Function.class,
            BinaryOperator.class
    );

    private static final Set<String> DEFAULT_CORE_PACKAGES = Sets.newLinkedHashSet(
            Arrays.asList("java.lang.", "java.util.")
    );

    private static Set<String> corePackages;
    private static Set<Class> parentClasses;
    private static Map<String, Set<Class>> idToClasses;

    /**
     * If true then the full class name is used for serialisation.
     * The default is true, this could be changed to true in the next major release.
     */
    private static boolean useFullNameForSerialisation = true;

    private ClassNameIdResolver defaultResolver;

    private JavaType baseType;

    static {
        reset();
    }

    /**
     * Allows you to add additional classes to the cache that you wish to be
     * accessed via the simple class name.
     *
     * @param includeSubtypes if true then all implementations are included.
     * @param classes         the classes to be included.
     */
    public static void addSimpleClassNames(final boolean includeSubtypes, final Class... classes) {
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
     * Setter for useFullNameForSerialisation.
     *
     * @param useFullNameForSerialisation if true then the full class name is used for serialisation.
     */
    public static void setUseFullNameForSerialisation(final boolean useFullNameForSerialisation) {
        SimpleClassNameIdResolver.useFullNameForSerialisation = useFullNameForSerialisation;
    }

    /**
     * Gets a simple class name for the given class.
     *
     * @param clazz the class to get the simple class name for.
     * @return the simple class name, or null a simple class name can't be used.
     */
    public static String getSimpleClassName(final Class<?> clazz) {
        String id;
        if (useFullNameForSerialisation) {
            id = null;
        } else {
            id = clazz.getSimpleName();
            final Set<Class> classesForId = getClassesFromId(id);
            if (null == classesForId || classesForId.isEmpty()) {
                // Check if the class is in one of the core packages
                if (corePackages.contains(clazz.getPackage().getName())) {
                    addIdClasses(id, Sets.newHashSet(clazz));
                } else {
                    id = null;
                }
            } else if (1 != classesForId.size()) {
                id = null;
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
        String className = null;
        if (null != id && !id.contains(".")) {
            final Set<Class> classes = getClassesFromId(id);
            if (null == classes || classes.isEmpty()) {
                for (final String corePackage : corePackages) {
                    final Class<?> clazz = ReflectionUtil.getClassFromName(corePackage + id);
                    if (null != clazz) {
                        className = corePackage + id;
                        addIdClasses(id, Sets.newHashSet(clazz));
                        break;
                    }
                }
            } else if (1 == classes.size()) {
                className = classes.iterator().next().getName();
            } else {
                throw new IllegalArgumentException("Multiple " + id + " classes exist. Please choose one of the following and specify the full class name: " + classes.stream().map(Class::getName).collect(Collectors.toList()));
            }
        }
        if (null == className) {
            className = id;
        }
        return className;
    }

    /**
     * Resets the caches.
     */
    public static void reset() {
        parentClasses = createParentClasses();
        idToClasses = createIdToClasses();
        corePackages = new LinkedHashSet<>(DEFAULT_CORE_PACKAGES);
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
        for (final Class parentClass : parentClasses) {
            addSimpleClassNames(map, parentClass);
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

    private static void addSimpleClassNames(final Map<String, Set<Class>> map, final Class parentClass) {
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(parentClass);
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

    @Override
    public void init(final JavaType baseType) {
        this.baseType = baseType;
        if (null == baseType.getContentType()) {
            this.baseType = baseType;
        } else {
            this.baseType = baseType.getContentType();
        }
        final ClassNameIdResolver newDefaultResolver = new ClassNameIdResolver(this.baseType, null);
        newDefaultResolver.init(this.baseType);
        init(newDefaultResolver);
    }

    protected void init(final ClassNameIdResolver defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    @Override
    public String idFromValue(final Object value) {
        String id = getSimpleClassName(value.getClass());
        if (null == id) {
            id = defaultResolver.idFromValue(value);
        }
        return id;
    }

    @Override
    public String idFromValueAndType(final Object value, final Class<?> suggestedType) {
        String id = getSimpleClassName(value.getClass());
        if (null == id) {
            id = defaultResolver.idFromValueAndType(value, suggestedType);
        }
        return id;
    }

    @Override
    public String idFromBaseType() {
        String id = getSimpleClassName(baseType.getRawClass());
        if (null == id) {
            id = defaultResolver.idFromBaseType();
        }
        return id;
    }

    @Override
    public JavaType typeFromId(final String id) {
        return defaultResolver.typeFromId(getClassName(id));
    }

    @Override
    public JavaType typeFromId(final DatabindContext context, final String id) {
        return defaultResolver.typeFromId(context, getClassName(id));
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CLASS;
    }


    private static Set<Class> getClassesFromId(final String id) {
        return idToClasses.get(StringUtils.capitalize(id));
    }

    private static void addIdClasses(final String id, final Set<Class> classes) {
        idToClasses.put(StringUtils.capitalize(id), classes);
    }
}
