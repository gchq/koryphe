/*
 * Copyright 2017-2025 Crown Copyright
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
    public static final boolean DEFAULT_FULLY_CONTAINED = false;

    private RangeUtil() {
    }

    /**
     * Checks the start and end values within a provided range.
     *
     * @param startValue     the start value to test
     * @param endValue       the end value to test
     * @param rangeStart     the start of the defined allowed range
     * @param rangeEnd       the end of the defined allowed range
     * @param startInclusive true if the start of the range is inclusive
     * @param endInclusive   true if the end of the range is inclusive
     * @param <T>            the type of the range
     * @return true if the start and end values are within the range.
     */
    public static <T extends Comparable<T>> boolean inRange(
            final Comparable<T> startValue, final Comparable<T> endValue,
            final T rangeStart, final T rangeEnd,
            final Boolean startInclusive, final Boolean endInclusive) {
        return inRange(startValue, endValue, rangeStart, rangeEnd, startInclusive, endInclusive, DEFAULT_FULLY_CONTAINED, DEFAULT_FULLY_CONTAINED);
    }

    /**
     * Checks the start and end values within a provided range.
     *
     * @param startValue          the start value to test
     * @param endValue            the end value to test
     * @param rangeStart          the start of the defined allowed range
     * @param rangeEnd            the start of the defined allowed range
     * @param startInclusive      true if the start of the range is inclusive
     * @param endInclusive        true if the end of the range is inclusive
     * @param startFullyContained true if the start of the range is fully contained
     * @param endFullyContained   true if the end of the range is fully contained
     * @param <T>                 the type of the range
     * @return true if the start and end values are within the range.
     */
    public static <T extends Comparable<T>> boolean inRange(
            final Comparable<T> startValue, final Comparable<T> endValue,
            final T rangeStart, final T rangeEnd,
            final Boolean startInclusive, final Boolean endInclusive,
            final Boolean startFullyContained, final Boolean endFullyContained) {
        if (null == startValue || null == endValue) {
            return false;
        }

        final boolean isStartFullyContained = null == startFullyContained ? DEFAULT_FULLY_CONTAINED : startFullyContained;
        final boolean isEndFullyContained = null == endFullyContained ? DEFAULT_FULLY_CONTAINED : endFullyContained;

        final boolean startMoreThanStart = isMoreThanStart(startValue, rangeStart, startInclusive);
        final boolean startLessThanEnd = isLessThanEnd(startValue, rangeEnd, endInclusive);
        final boolean startInRange = startMoreThanStart && startLessThanEnd;

        final boolean endMoreThanStart = isMoreThanStart(endValue, rangeStart, startInclusive);
        final boolean endLessThanEnd = isLessThanEnd(endValue, rangeEnd, endInclusive);
        final boolean endInRange = endMoreThanStart && endLessThanEnd;

        // [  ] the test item time range
        // {  } the requested time range

        // Full match
        // {  [  ]  }
        if (startInRange && endInRange) {
            return true;
        }

        // Partial match
        // {  [  }  ]   or   [  {  ]  }
        if ((startInRange && !endLessThanEnd && !isEndFullyContained) || (endInRange && !startMoreThanStart && !isStartFullyContained)) {
            return true;
        }

        // Partial match
        // [  {  }  ]
        if (!startMoreThanStart && !isStartFullyContained && !endLessThanEnd && !isEndFullyContained) {
            return true;
        }

        return false;
    }

    private static <T extends Comparable<T>> boolean isMoreThanStart(final Comparable<T> value, final T rangeStart, final Boolean startInclusive) {
        final boolean result;
        if (null == rangeStart) {
            result = true;
        } else if (null == startInclusive || startInclusive) {
            result = value.compareTo(rangeStart) >= 0;
        } else {
            result = value.compareTo(rangeStart) > 0;
        }
        return result;
    }

    private static <T extends Comparable<T>> boolean isLessThanEnd(final Comparable<T> value, final T rangeEnd, final Boolean endInclusive) {
        final boolean result;
        if (null == rangeEnd) {
            result = true;
        } else if (null == endInclusive || endInclusive) {
            result = value.compareTo(rangeEnd) <= 0;
        } else {
            result = value.compareTo(rangeEnd) < 0;
        }
        return result;
    }
}
