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

package uk.gov.gchq.koryphe.util;

import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Reflection utilities. Contains methods such as getting sub classes.
 * By default only classes prefixed with uk.gov.gchq will be scanned. If you
 * wish to include your own packages/classes in the scanner you can call
 * {@link ReflectionUtil#addPaths(String...)} or set the System Property
 * "gaffer.reflection.paths" with a csv of your additional paths.
 */
public final class ReflectionUtil {
    public static final String PATHS_KEY = "gaffer.reflection.paths";
    private static final String DEFAULT_PATH = "uk.gov.gchq";
    private static final Set<String> PATHS = Sets.newHashSet(DEFAULT_PATH);
    private static final Map<Class<?>, Map<String, Class>> SIMPLE_CLASS_NAMES = new HashMap<>();
    private static final Map<Class<?>, Set<Class>> SUB_CLASSES = new HashMap<>();

    private ReflectionUtil() {
        // Private constructor to prevent instantiation.
    }

    public static void addPaths(final String... paths) {
        if (null != paths) {
            for (final String path : paths) {
                if (null != path) {
                    Collections.addAll(PATHS, path.replace(" ", "").split(","));
                }
            }
        }
    }

    /**
     * Get a map of simple class name to class, for all sub classes of a given class.
     * The results are cached.
     *
     * @param clazz the class to get simple class names for.
     * @return a map of simple class name to class
     */
    public static Map<String, Class> getSimpleClassNames(final Class<?> clazz) {
        Map<String, Class> simpleClassNames = SIMPLE_CLASS_NAMES.get(clazz);
        if (null == simpleClassNames) {
            final Set<Class> classes = getSubClasses(clazz);
            simpleClassNames = new HashMap<>(classes.size());
            for (final Class op : classes) {
                if (simpleClassNames.containsKey(op.getSimpleName())) {
                    // Multiple classes with the same simple name
                    simpleClassNames.put(op.getSimpleName(), null);
                } else {
                    simpleClassNames.put(op.getSimpleName(), op);
                }
            }
            simpleClassNames = Collections.unmodifiableMap(simpleClassNames);
            SIMPLE_CLASS_NAMES.put(clazz, simpleClassNames);
        }
        return simpleClassNames;
    }

    /**
     * Get sub classes of a given class. The results are cached.
     *
     * @param clazz the class to get sub types of.
     * @return the sub classes.
     */
    public static Set<Class> getSubClasses(final Class<?> clazz) {
        Set<Class> subClasses = SUB_CLASSES.get(clazz);
        if (null == subClasses) {
            addPaths(System.getProperty(PATHS_KEY));

            final Set<URL> urls = new HashSet<>();
            for (final String packagePrefix : PATHS) {
                urls.addAll(ClasspathHelper.forPackage(packagePrefix));
            }

            subClasses = new HashSet<>();
            subClasses.addAll(new Reflections(urls).getSubTypesOf(clazz));
            keepPublicConcreteClasses(subClasses);
            subClasses = Collections.unmodifiableSet(subClasses);
            SUB_CLASSES.put(clazz, subClasses);
        }

        return subClasses;
    }

    private static void keepPublicConcreteClasses(final Collection<Class> classes) {
        if (null != classes) {
            final Iterator<Class> itr = classes.iterator();
            while (itr.hasNext()) {
                final Class clazz = itr.next();
                if (null != clazz) {
                    final int modifiers = clazz.getModifiers();
                    if (Modifier.isAbstract(modifiers)
                            || Modifier.isInterface(modifiers)
                            || Modifier.isPrivate(modifiers)
                            || Modifier.isProtected(modifiers)) {
                        itr.remove();
                    }
                }
            }
        }
    }
}
