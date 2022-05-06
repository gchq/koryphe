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

package uk.gov.gchq.koryphe.iterable;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.predicate.IsLessThan;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class FilteredIterableTest {

    @Test
    public void shouldThrowIAXWhenArrayOfIterablesAreNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> new FilteredIterable(null, Lists.newArrayList(new IsLessThan(4))));
    }

    @Test
    public void shouldCorrectlyFilterSinglePredicate() {
        // Given
        final List<Integer> itr = Lists.newArrayList(1, 2, 3, 4, 5);
        final List<Predicate> predicates = Lists.newArrayList(new IsLessThan(4));

        // When
        FilteredIterable<Integer> filteredIterable = new FilteredIterable<Integer>(itr, predicates);
        
        // Then
        assertThat(filteredIterable).containsExactly(1, 2, 3);
    }

    @Test
    public void shouldCorrectlyFilterMultiplePredicates() {
        // Given
        final List<Integer> itr = Lists.newArrayList(1, 2, 3, 4, 5);
        final List<Predicate> predicates = Lists.newArrayList(new IsLessThan(4), new IsMoreThan(1));

        // When
        FilteredIterable<Integer> filteredIterable = new FilteredIterable<Integer>(itr, predicates);
        
        // Then
        assertThat(filteredIterable).containsExactly(2, 3);
    }

}
