/*
 * Copyright 2016 Crown Copyright
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
import org.junit.Test;

import uk.gov.gchq.koryphe.iterable.CloseableIterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        assertEquals(Lists.newArrayList(0, 1, 2, 3, 4, 5, 6), Lists.newArrayList(itrConcat));
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
        assertEquals(0, (int) itr.next());
        itr.remove();

        // Then
        assertEquals(itr1Size - 1, itr1.size());
        assertEquals(itr2Size, itr2.size());
        assertEquals(itr3Size, itr3.size());
        assertEquals(itr4Size, itr4.size());
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
        assertEquals(0, (int) itr.next());
        assertEquals(1, (int) itr.next());
        itr.remove();

        // Then
        assertEquals(itr1Size, itr1.size());
        assertEquals(itr2Size, itr2.size());
        assertEquals(itr3Size - 1, itr3.size());
        assertEquals(itr4Size, itr4.size());
    }

    @Test
    public void shouldLimitResultsToFirstItem() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 0;
        final int end = 1;

        // When
        final CloseableIterable<Integer> limitedValues = IterableUtil.limit(values, start, end, true);

        // Then
        assertEquals(values.subList(start, end), Lists.newArrayList(limitedValues));
    }

    @Test
    public void shouldLimitResultsToLastItem() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 2;
        final int end = Integer.MAX_VALUE;

        // When
        final CloseableIterable<Integer> limitedValues = IterableUtil.limit(values, start, end, true);

        // Then
        assertEquals(values.subList(start, values.size()), Lists.newArrayList(limitedValues));
    }

    @Test
    public void shouldNotLimitResults() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 0;
        final int end = Integer.MAX_VALUE;

        // When
        final CloseableIterable<Integer> limitedValues = IterableUtil.limit(values, start, end, true);

        // Then
        assertEquals(values, Lists.newArrayList(limitedValues));
    }

    @Test
    public void shouldReturnNoValuesIfStartIsBiggerThanSize() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 5;
        final int end = Integer.MAX_VALUE;

        // When
        final CloseableIterable<Integer> limitedValues = IterableUtil.limit(values, start, end, true);

        // Then
        assertTrue(Lists.newArrayList(limitedValues).isEmpty());
    }

    @Test
    public void shouldThrowExceptionIfStartIsBiggerThanEnd() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 3;
        final int end = 1;

        // When / Then
        try {
            IterableUtil.limit(values, start, end, false);
            fail("Exception expected");
        } catch (final IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionIfDataIsTruncated() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 0;
        final int end = 2;
        final boolean truncate = false;

        // When
        final CloseableIterable<Integer> limitedValues = IterableUtil.limit(values, start, end, truncate);

        // Then
        try {
            for (final Integer i : limitedValues) {
                // Do nothing
            }
            fail("Exception expected");
        } catch (final Exception e) {
            assertEquals("Limit of " + end + " exceeded.", e
                    .getMessage());
        }
    }

    @Test
    public void shouldHandleNullIterable() {
        // Given
        final CloseableIterable<Integer> nullIterable = IterableUtil.limit(null, 0, 1, true);

        // Then
        assertTrue(Lists.newArrayList(nullIterable).isEmpty());
    }

    @Test
    public void shouldHandleLimitEqualToIterableLength() {
        // Given
        final List<Integer> values = Arrays.asList(0, 1, 2, 3);
        final int start = 0;
        final int end = 4;
        final boolean truncate = false;

        // When
        final CloseableIterable<Integer> equalValues = IterableUtil.limit(values, start, end, truncate);

        // Then
        assertEquals(values, Lists.newArrayList(equalValues));
    }
}
