/*
 * Copyright 2017-2020 Crown Copyright
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

package uk.gov.gchq.koryphe.util;

import com.google.common.collect.Sets;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reflection utilities. Contains methods such as getting sub classes.
 * By default only classes prefixed with 'uk.gov.gchq' will be scanned.
 * If you wish to include your own packages/classes in the scanner you can call
 * {@link ReflectionUtil#addReflectionPackages(String...)} or set the System Property
 * "koryphe.reflection.packages" with a csv of your additional packages.
 */
public final class ReflectionUtil {
    public static final String PACKAGES_KEY = "koryphe.reflection.packages";
    public static final Set<String> DEFAULT_PACKAGES = Set.of("uk.gov.gchq");

    private static Set<String> packages;
    private static Map<Class<?>, Map<String, Set<Class>>> simpleClassNamesCache;
    private static Map<Class<?>, Set<Class>> subclassesCache;
    private static Map<Class<? extends Annotation>, Set<Class>> annoClassesCache;

    static {
        resetReflectionPackages();
        resetReflectionCache();
    }

    private ReflectionUtil() {
        // Private constructor to prevent instantiation.
    }

    /**
     * Simply returns Class.forName(className), but any ClassNotFoundException exceptions are caught and null is returned.
     *
     * @param className the class name to lookup
     * @return the class if found, otherwise null.
     */
    public static Class<?> getClassFromName(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Get a map of simple class name to class, for all sub classes of a given class.
     * The results are cached.
     *
     * @param clazz the class to get simple class names for.
     * @return a map of simple class name to class
     */
    public static Map<String, Set<Class>> getSimpleClassNames(final Class<?> clazz) {
        Map<String, Set<Class>> simpleClassNames = simpleClassNamesCache.get(clazz);
        if (null == simpleClassNames) {
            final Set<Class> subTypes = getSubTypes(clazz);
            simpleClassNames = new HashMap<>(subTypes.size());
            for (final Class subType : subTypes) {
                final Set<Class> simpleClasses = simpleClassNames.computeIfAbsent(subType.getSimpleName(), k -> new HashSet<>());
                simpleClasses.add(subType);
            }
            simpleClassNames = Map.copyOf(simpleClassNames);
            simpleClassNamesCache.put(clazz, simpleClassNames);
        }
        return simpleClassNames;
    }

    /**
     * Get implementations of a given class. The results are cached.
     *
     * @param clazz the class to get sub types of.
     * @return the sub classes.
     */
    public static Set<Class> getSubTypes(final Class<?> clazz) {
        Set<Class> subClasses = subclassesCache.get(clazz);
        if (null == subClasses) {
            updateReflectionPackages();

            final Set<Class> newSubClasses = new HashSet<>();
            if (clazz.isInterface()) {
                getScanner().matchClassesImplementing(clazz, c -> {
                    if (isPublicConcrete(c)) {
                        newSubClasses.add(c);
                    }
                }).scan();
            } else {
                getScanner().matchSubclassesOf(clazz, c -> {
                    if (isPublicConcrete(c)) {
                        newSubClasses.add(c);
                    }
                }).scan();
            }
            subClasses = Set.copyOf(newSubClasses);
            subclassesCache.put(clazz, subClasses);
        }
        return subClasses;
    }

    /**
     * Get classes annotated with the given annotation class. The results are cached.
     *
     * @param annoClass the annotation class to get classes for.
     * @return the annotated classes.
     */
    public static Set<Class> getAnnotatedTypes(final Class<? extends Annotation> annoClass) {
        Set<Class> annoClasses = annoClassesCache.get(annoClass);
        if (null == annoClasses) {
            updateReflectionPackages();
            annoClasses = new HashSet<>();
            getScanner().matchClassesWithAnnotation(annoClass, annoClasses::add).scan();
            annoClasses = Set.copyOf(annoClasses);
            subclassesCache.put(annoClass, annoClasses);
        }

        return annoClasses;
    }

    /**
     * @param clazz the class to test
     * @return true if the class is public, not abstract and not an interface.
     */
    public static boolean isPublicConcrete(final Class clazz) {
        final int modifiers = clazz.getModifiers();
        return Modifier.isPublic(modifiers)
                && !Modifier.isAbstract(modifiers)
                && !Modifier.isInterface(modifiers);
    }

    /**
     * Removes non public or non concrete classes from the given collection.
     *
     * @param classes the collection of classes to modify.
     */
    public static void keepPublicConcreteClasses(final Collection<Class> classes) {
        if (null != classes) {
            final Iterator<Class> itr = classes.iterator();
            while (itr.hasNext()) {
                final Class clazz = itr.next();
                if (null != clazz) {
                    if (!isPublicConcrete(clazz)) {
                        itr.remove();
                    }
                }
            }
        }
    }

    /**
     * Resets the list of packages that are scanned. It will be set back to the
     * default path and the paths set in the system property
     * "koryphe.reflection.packages".
     */
    public static void resetReflectionPackages() {
        packages = ConcurrentHashMap.newKeySet();
        addReflectionPackages(DEFAULT_PACKAGES);
        addReflectionPackages(System.getProperty(PACKAGES_KEY));
    }

    /**
     * Resets the caches.
     */
    public static void resetReflectionCache() {
        simpleClassNamesCache = new ConcurrentHashMap<>();
        subclassesCache = new ConcurrentHashMap<>();
        annoClassesCache = new ConcurrentHashMap<>();
    }

    /**
     * Updates the reflection packages using the system property.
     */
    public static void updateReflectionPackages() {
        addReflectionPackages(System.getProperty(PACKAGES_KEY));
    }

    /**
     * Adds new reflection packages. If any new packages are found then the
     * reflection cache is reset.
     *
     * @param newPackages new packages to add. These can be CSVs.
     */
    public static void addReflectionPackages(final String... newPackages) {
        if (null != newPackages && 0 < newPackages.length) {
            if (1 == newPackages.length) {
                addReflectionPackages(Collections.singleton(newPackages[0]));
            } else {
                addReflectionPackages(Sets.newHashSet(newPackages));
            }
        }
    }

    public static void addReflectionPackages(final Iterable<String> newPackages) {
        if (null != newPackages) {
            boolean hasNewPackage = false;
            for (final String packageCsv : newPackages) {
                if (null != packageCsv) {
                    for (final String path : packageCsv.replace(" ", "").split(",")) {
                        final String pathChecked = path.endsWith(".") ? path.substring(0, path.length() - 1) : path;
                        if (!packages.contains(pathChecked)) {
                            packages.add(pathChecked);
                            hasNewPackage = true;
                        }
                    }
                }
            }

            if (hasNewPackage) {
                resetReflectionCache();
            }
        }
    }

    public static Set<String> getReflectionPackages() {
        return Set.copyOf(packages);
    }

    private static FastClasspathScanner getScanner() {
        return new FastClasspathScanner(packages.toArray(String[]::new));
    }
}
