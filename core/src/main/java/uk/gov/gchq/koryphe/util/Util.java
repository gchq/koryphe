/*
 * Copyright 2018-2020 Crown Copyright
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

/**
 * Common utility methods.
 */
public final class Util {
    private Util() {
    }

    /**
     * Converts varargs strings into an array.
     *
     * @param input the input varargs to convert to an array
     * @return an array containing the input values
     */
    public static String[] arr(final String... input) {
        return input;
    }

    /**
     * Converts varargs strings into an array. Named as 'select' for readability.
     *
     * @param input the input varargs to convert to an array
     * @return an array containing the input values
     */
    public static String[] select(final String... input) {
        return input;
    }

    /**
     * Converts varargs strings into an array. Named as 'project' for readability.
     *
     * @param input the input varargs to convert to an array
     * @return an array containing the input values
     */
    public static String[] project(final String... input) {
        return input;
    }

    /**
     * Converts varargs integers into an array.
     *
     * @param input the input varargs to convert to an array
     * @return an array containing the input values
     */
    public static Integer[] arr(final Integer... input) {
        return input;
    }

    /**
     * Converts varargs integers into an array. Named as 'select' for readability.
     *
     * @param input the input varargs to convert to an array
     * @return an array containing the input values
     */
    public static Integer[] select(final Integer... input) {
        return input;
    }

    /**
     * Converts varargs integers into an array. Named as 'project' for readability.
     *
     * @param input the input varargs to convert to an array
     * @return an array containing the input values
     */
    public static Integer[] project(final Integer... input) {
        return input;
    }
}
