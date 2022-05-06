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

import uk.gov.gchq.koryphe.impl.function.Increment;
import uk.gov.gchq.koryphe.impl.function.MultiplyBy;

import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class MappedIterableTest {

    @Test
    public void shouldThrowIAXWhenArrayOfIterablesAreNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> new MappedIterable(null, Lists.newArrayList(new Increment(4))));
    }

    @Test
    public void shouldCorrectlyApplySingleFunction() {
        // Given
        final List<Integer> itr = Lists.newArrayList(1, 2, 3, 4, 5);
        final List<Function> functions = Lists.newArrayList(new Increment(4));

        // When
        MappedIterable<Integer, Integer> mappedIterable = new MappedIterable<Integer, Integer>(itr, functions);
        
        // Then
        assertThat(mappedIterable).containsExactly(5, 6, 7, 8, 9);
    }

    @Test
    public void shouldCorrectlyApplyMultipleFunctions() {
        // Given
        final List<Integer> itr = Lists.newArrayList(1, 2, 3, 4, 5);
        final List<Function> functions = Lists.newArrayList(new Increment(4), new MultiplyBy(2));

        // When
        MappedIterable<Integer, Integer> mappedIterable = new MappedIterable<Integer, Integer>(itr, functions);
        
        // Then
        assertThat(mappedIterable).containsExactly(10, 12, 14, 16, 18);
    }

}
