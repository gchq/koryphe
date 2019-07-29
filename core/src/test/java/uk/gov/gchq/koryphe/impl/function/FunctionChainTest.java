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

import com.google.common.collect.Sets;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class FunctionChainTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new FunctionChain();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return FunctionChain.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final FunctionChain function = new FunctionChain.Builder<>()
                .execute(new Integer[]{1}, new ToUpperCase(), new Integer[]{2})
                .execute(new Integer[]{2}, new ToSet(), new Integer[]{3})
                .build();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals("{" +
                "\"class\":\"uk.gov.gchq.koryphe.impl.function.FunctionChain\"," +
                "\"functions\":[" +
                "{\"class\":\"uk.gov.gchq.koryphe.tuple.function.TupleAdaptedFunctionComposite\",\"functions\":[{\"selection\":[1],\"function\":{\"class\":\"uk.gov.gchq.koryphe.impl.function.ToUpperCase\"},\"projection\":[2]}]}," +
                "{\"class\":\"uk.gov.gchq.koryphe.tuple.function.TupleAdaptedFunctionComposite\",\"functions\":[{\"selection\":[2],\"function\":{\"class\":\"uk.gov.gchq.koryphe.impl.function.ToSet\"},\"projection\":[3]}]}" +
                "]" +
                "}", json);
    }

    @Test
    public void shouldApplyAllTupleFunctions() {
        // Given
        final FunctionChain function = new FunctionChain.Builder<>()
                .execute(new Integer[]{0}, new ToUpperCase(), new Integer[]{1})
                .execute(new Integer[]{1}, new ToSet(), new Integer[]{2})
                .build();
        final ArrayTuple input = new ArrayTuple("someString", null, null);

        // When
        final Object result = function.apply(input);

        // Then
        assertEquals(new ArrayTuple("someString", "SOMESTRING", Sets.newHashSet("SOMESTRING")), result);
    }

    @Test
    public void shouldApplyAllFunctions() {
        // Given
        final FunctionChain function = new FunctionChain(new ToUpperCase(), new ToSet());
        final String input = "someString";

        // When
        final Object result = function.apply(input);

        // Then
        assertEquals(Sets.newHashSet("SOMESTRING"), result);
    }

    @Test
    public void shouldReturnInputWhenNoFunctions() {
        // Given
        final FunctionChain function = new FunctionChain();
        function.setComponents(null);
        final String input = "someString";

        // When
        final Object result = function.apply(input);

        // Then
        assertEquals(input, result);
    }
}
