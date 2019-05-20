/*
 * Copyright 2019 Crown Copyright
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

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.ReflectiveTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ToTupleTest extends FunctionTest {

    @Test
    public void shouldConvertListIntoArrayTuple() {
        // Given
        final ToTuple function = new ToTuple();

        // When
        Tuple output = function.apply(Lists.newArrayList(1, 2, 3, 4));

        // Then
        assertEquals(new ArrayTuple(1, 2, 3, 4), output);
    }

    @Test
    public void shouldConvertPrimitiveArrayIntoArrayTuple() {
        // Given
        final ToTuple function = new ToTuple();

        // When
        Tuple output = function.apply(new int[]{1, 2, 3, 4});

        // Then
        assertEquals(new ArrayTuple(1, 2, 3, 4), output);
    }

    @Test
    public void shouldConvertArrayIntoArrayTuple() {
        // Given
        final ToTuple function = new ToTuple();

        // When
        Tuple output = function.apply(new Integer[]{1, 2, 3, 4});

        // Then
        assertEquals(new ArrayTuple(1, 2, 3, 4), output);
    }

    @Test
    public void shouldConvertMapIntoMapTuple() {
        // Given
        final ToTuple function = new ToTuple();
        Map<String, Object> input = new HashMap<>();
        input.put("A", 1);
        input.put("B", 2);
        input.put("C", 3);

        // When
        Tuple output = function.apply(input);

        // Then
        assertEquals(new MapTuple<>(input), output);
    }

    @Test
    public void shouldConvertObjectIntoReflectiveTuple() {
        // Given
        final ToTuple function = new ToTuple();
        Object input = new SimpleObj("value1");
        // When
        Tuple output = function.apply(input);

        // Then
        assertEquals(new ReflectiveTuple(input), output);
    }

    @Override
    protected Function getInstance() {
        return new ToTuple();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return ToTuple.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToTuple function = new ToTuple();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToTuple\"" +
                "}"), json);

        // When 2
        final ToTuple deserialisedMethod = JsonSerialiser.deserialise(json, ToTuple.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }

    private static class SimpleObj {
        private String field;

        public SimpleObj(final String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }

        public void setField(final String field) {
            this.field = field;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final SimpleObj simpleObj = (SimpleObj) o;

            return new EqualsBuilder()
                    .append(field, simpleObj.field)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(field)
                    .toHashCode();
        }
    }
}
