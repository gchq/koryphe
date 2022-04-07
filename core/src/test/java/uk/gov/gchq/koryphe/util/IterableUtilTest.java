/*
 * Copyright 2017-2022 Crown Copyright
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

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class IterableUtilTest {

    @Test
    public void shouldWrapAllIterables() {
        // Given
        final List<Integer> itr1 = Collections.singletonList(0);
        final List<Integer> itr2 = new ArrayList<>(0);
        final List<Integer> itr3 = Lists.newArrayList(1, 2, 3, 4);
        final List<Integer> itr4 = Lists.newArrayList(5, 6);

        // When
        final Iterable<Integer> itrConcat = IterableUtil.concat(Arrays.asList(itr1, itr2, itr3, itr4));

        // Then
        assertThat(itrConcat).containsExactly(0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void shouldRemoveElementFromFirstIterable() {
        // Given
        final List<Integer> itr1 = Lists.newArrayList(0);
        final List<Integer> itr2 = new ArrayList<>(0);
        final List<Integer> itr3 = Lists.newArrayList(1, 2, 3, 4);
        final List<Integer> itr4 = Lists.newArrayList(5, 6);

        final int itr1Size = itr1.size();
        final int itr2Size = itr2.size();
        final int itr3Size = itr3.size();
        final int itr4Size = itr4.size();

        final Iterable<Integer> itrConcat = IterableUtil.concat(Arrays.asList(itr1, itr2, itr3, itr4));

        // When
        final Iterator<Integer> itr = itrConcat.iterator();
        assertThat((int) itr.next()).isEqualTo(0);
        itr.remove();

        // Then
        assertThat(itr1).hasSize(itr1Size - 1);
        assertThat(itr2).hasSize(itr2Size);
        assertThat(itr3).hasSize(itr3Size);
        assertThat(itr4).hasSize(itr4Size);
    }

    @Test
    public void shouldRemoveElementFromThirdIterable() {
        // Given
        final List<Integer> itr1 = Lists.newArrayList(0);
        final List<Integer> itr2 = new ArrayList<>(0);
        final List<Integer> itr3 = Lists.newArrayList(1, 2, 3, 4);
        final List<Integer> itr4 = Lists.newArrayList(5, 6);

        final int itr1Size = itr1.size();
        final int itr2Size = itr2.size();
        final int itr3Size = itr3.size();
        final int itr4Size = itr4.size();

        final Iterable<Integer> itrConcat = IterableUtil.concat(Arrays.asList(itr1, itr2, itr3, itr4));

        // When
        final Iterator<Integer> itr = itrConcat.iterator();
        assertThat((int) itr.next()).isEqualTo(0);
        assertThat((int) itr.next()).isEqualTo(1);
        itr.remove();

        // Then
        assertThat(itr1).hasSize(itr1Size);
        assertThat(itr2).hasSize(itr2Size);
        assertThat(itr3).hasSize(itr3Size - 1);
        assertThat(itr4).hasSize(itr4Size);
    }

    @Test
    public void shouldLimitResultsToFirstItem() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 0;
        final int end = 1;

        // When
        final Iterable<Integer> limitedValues = IterableUtil.limit(values, start, end, true);

        // Then
        assertThat(limitedValues).containsExactlyElementsOf(values.subList(start, end));
    }

    @Test
    public void shouldLimitResultsToLastItem() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 2;
        final int end = Integer.MAX_VALUE;

        // When
        final Iterable<Integer> limitedValues = IterableUtil.limit(values, start, end, true);

        // Then
        assertThat(limitedValues).containsExactlyElementsOf(values.subList(start, values.size()));
    }

    @Test
    public void shouldNotLimitResults() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 0;
        final int end = Integer.MAX_VALUE;

        // When
        final Iterable<Integer> limitedValues = IterableUtil.limit(values, start, end, true);

        // Then
        assertThat(limitedValues).containsExactlyElementsOf(values);
    }

    @Test
    public void shouldReturnNoValuesIfStartIsBiggerThanSize() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 5;
        final int end = Integer.MAX_VALUE;

        // When
        final Iterable<Integer> limitedValues = IterableUtil.limit(values, start, end, true);

        // Then
        assertThat(limitedValues).isEmpty();
    }

    @Test
    public void shouldThrowExceptionIfStartIsBiggerThanEnd() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 3;
        final int end = 1;

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IterableUtil.limit(values, start, end, false))
                .withMessage("The start pointer must be less than the end pointer.");
    }

    @Test
    public void shouldThrowExceptionIfDataIsTruncated() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 0;
        final int end = 2;
        final boolean truncate = false;

        // When
        final Iterable<Integer> limitedValues = IterableUtil.limit(values, start, end, truncate);

        // Then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> {
                    for (final Integer i : limitedValues) {
                        // Do nothing
                    }
                })
                .withMessage("Limit of %s exceeded.", end);
    }

    @Test
    public void shouldHandleNullIterable() {
        // Given
        final Iterable<Integer> nullIterable = IterableUtil.limit(null, 0, 1, true);

        // When / Then
        assertThat(nullIterable).isEmpty();
    }

    @Test
    public void shouldHandleLimitEqualToIterableLength() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 0;
        final int end = 4;
        final boolean truncate = false;

        // When
        final Iterable<Integer> equalValues = IterableUtil.limit(values, start, end, truncate);

        // Then
        assertThat(values).containsExactlyElementsOf(equalValues);
    }
}
