/*
 * Copyright 2020-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.tuple;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.util.EqualityTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

// Can't extend FunctionTest as TupleOutputAdapter is a BiFunction rather than a Function.
class TupleOutputAdapterTest extends EqualityTest<TupleOutputAdapter> {

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        TupleOutputAdapter instance = getInstance();
        String json = "" +
                "{" +
                "   \"class\": \"uk.gov.gchq.koryphe.tuple.TupleOutputAdapter\"," +
                "   \"projection\": [ \"input\" ]" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(instance);
        TupleOutputAdapter deserialised = JsonSerialiser.deserialise(json, TupleOutputAdapter.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertThat(deserialised).isEqualTo(instance);
    }

    @Override
    protected TupleOutputAdapter getInstance() {
        return new TupleOutputAdapter(new String[] { "input" });
    }

    @Override
    protected Iterable<TupleOutputAdapter> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new TupleOutputAdapter(new String[] { "Different" }),
                new TupleOutputAdapter(new Integer[] { 1, 2, 3 }),
                new TupleOutputAdapter()
        );
    }

    @Test
    public void shouldAddToTuple() {
        // Given
        ArrayTuple state = new ArrayTuple(3);
        state.put(0, "thing");
        state.put(1, 50L);

        // When
        TupleOutputAdapter<Integer, Object> adapter = new TupleOutputAdapter<>(new Integer[]{2});
        Tuple<Integer> adapted = adapter.apply(state, "test");

        // Then
        assertThat(adapted.get(2)).isEqualTo("test");
    }

    @Test
    public void shouldReplaceAnyExistingValueInTuple() {
        // Given
        ArrayTuple state = new ArrayTuple(3);
        state.put(0, "thing");
        state.put(1, 50L);
        state.put(2, "Replace me");

        // When
        TupleOutputAdapter<Integer, Object> adapter = new TupleOutputAdapter<>(new Integer[]{2});
        Tuple<Integer> adapted = adapter.apply(state, "test");

        // Then
        assertThat(adapted.get(2)).isEqualTo("test");
    }
}
