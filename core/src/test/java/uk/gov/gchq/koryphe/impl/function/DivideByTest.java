/*
 * Copyright 2017-2020 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.tuple.n.Tuple2;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Collections;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DivideByTest extends FunctionTest {

    @Test
    public void shouldDivideBy2() {
        // Given
        final DivideBy function = new DivideBy(2);

        // When
        Tuple2<Integer, Integer> output = function.apply(4);

        assertEquals(new Tuple2<>(2, 0), output);
    }

    @Test
    public void shouldDivideBy2WithRemainder() {
        // Given
        final DivideBy function = new DivideBy(2);

        // When
        Tuple2<Integer, Integer> output = function.apply(5);

        assertEquals(new Tuple2<>(2, 1), output);
    }

    @Test
    public void shouldDivideBy1IfByIsNull() {
        // Given
        final DivideBy function = new DivideBy();

        // When
        Tuple2<Integer, Integer> output = function.apply(9);

        assertEquals(new Tuple2<>(9, 0), output);
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final DivideBy function = new DivideBy(4);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.DivideBy\",%n" +
                "  \"by\" : 4%n" +
                "}"), json);

        // When 2
        final DivideBy deserialisedDivideBy = JsonSerialiser.deserialise(json, DivideBy.class);

        // Then 2
        assertNotNull(deserialisedDivideBy);
        assertEquals(4, deserialisedDivideBy.getBy());
    }

    @Override
    protected DivideBy getInstance() {
        return new DivideBy(3);
    }

    @Override
    protected Iterable<Function> getDifferentInstances() {
        return Collections.singletonList(new DivideBy(5));
    }

    @Override
    protected Class<DivideBy> getFunctionClass() {
        return DivideBy.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Integer.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Integer.class, Integer.class};
    }
}
