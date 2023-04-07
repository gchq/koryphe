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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * Copies of various methods/classes from the Java 11 JDK.
 * Intended as a temporary proxy to assist with migration to Java 11 in future.
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

        @SafeVarargs
        public static <T> java.util.Set<T> of(final T... item) {
            return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(item)));
        }

        public static <T> java.util.Set<T> copyOf(final java.util.Set<T> set) {
            return Collections.unmodifiableSet(new HashSet<>(set));
        }

        public static <T> java.util.Set<T> immutableCopyOf(final java.util.Set<T> set) {
            return Collections.unmodifiableSet(set);
        }
    }

    public static class Map {

        public static <K, V> java.util.Map<K, V> immutableCopyOf(final java.util.Map<K, V> map) {
            return Collections.unmodifiableMap(map);
        }
    }
}
