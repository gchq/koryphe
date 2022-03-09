/*
 * Copyright 2022 Crown Copyright
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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Copies of various methods/class from the Java 11 JDK.
 */
public final class JavaUtils {

    private JavaUtils() {
        // Empty
    }

    public static <T> T requireNonNullElse(final T input, final T other) {
        if (null != input) {
            return input;
        } else {
            return other;
        }
    }

    public static class Set {

        public static <T> HashSet<T> of(final T... item) {
            return com.google.common.collect.Sets.newHashSet(item);
        }

        public static <T> HashSet<T> copyOf(final java.util.Set<T> set) {
            return new HashSet<>(set);
        }

        public static <T> java.util.Set<T> immutableCopyOf(final java.util.Set<T> set) {
            return Collections.unmodifiableSet(set);
        }
    }

    public static class Map {

        public static <K, V> HashMap<K, V> copyOf(final java.util.Map<K, V> map) {
            return new HashMap<>(map);
        }

        public static <K, V> java.util.Map<K, V> immutableCopyOf(final java.util.Map<K, V> map) {
            return Collections.unmodifiableMap(map);
        }
    }
}
