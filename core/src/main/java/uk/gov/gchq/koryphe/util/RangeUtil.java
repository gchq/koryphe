/*
 * Copyright 2017-2018 Crown Copyright
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
 * A utility class for Ranges.
 */
public final class RangeUtil {
    private RangeUtil() {
    }

    /**
     * Checks the start and end values within a provided range.
     *
     * @param startValue     the start value to test
     * @param endValue       the end value to test
     * @param rangeStart     the start of the defined allowed range
     * @param rangeEnd       the start of the defined allowed range
     * @param startInclusive true if the start of the range is inclusive
     * @param endInclusive   true if the end of the range is inclusive
     * @param <T>            the type of the range
     * @return true if the start and end values are within the range.
     */
    public static <T extends Comparable<T>> boolean inRange(
            final Comparable<T> startValue, final Comparable<T> endValue,
            final T rangeStart, final T rangeEnd,
            final Boolean startInclusive, final Boolean endInclusive) {
        if (null == startValue || null == endValue) {
            return false;
        }

        boolean startInRange;
        if (null == rangeStart) {
            startInRange = true;
        } else if (null == startInclusive || startInclusive) {
            startInRange = startValue.compareTo(rangeStart) >= 0;
        } else {
            startInRange = startValue.compareTo(rangeStart) > 0;
        }
        if (!startInRange) {
            return false;
        }

        boolean endInRange;
        if (null == rangeEnd) {
            endInRange = true;
        } else if (null == endInclusive || endInclusive) {
            endInRange = endValue.compareTo(rangeEnd) <= 0;
        } else {
            endInRange = endValue.compareTo(rangeEnd) < 0;
        }
        return endInRange;
    }
}
