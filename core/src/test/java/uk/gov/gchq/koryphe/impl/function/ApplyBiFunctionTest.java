/*
 * Copyright 2019-2020 Crown Copyright
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

import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.tuple.n.Tuple2;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ApplyBiFunctionTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new ApplyBiFunction();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return ApplyBiFunction.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ApplyBiFunction function = new ApplyBiFunction<>(new Sum());

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals("{\"class\":\"uk.gov.gchq.koryphe.impl.function.ApplyBiFunction\",\"function\":{\"class\":\"uk.gov.gchq.koryphe.impl.binaryoperator.Sum\"}}", json);
    }

    @Test
    public void shouldApplyBiFunction() {
        // Given
        final ApplyBiFunction<Number, Number, Number> function = new ApplyBiFunction<>(new Sum());
        final Tuple2<Number, Number> input = new Tuple2<>(1, 2);

        // When
        Number result = function.apply(input);

        // Then
        assertEquals(3, result);
    }

    @Test
    public void shouldReturnNullForNullFunction() {
        // Given
        final ApplyBiFunction<Number, Number, Number> function = new ApplyBiFunction<>();
        final Tuple2<Number, Number> input = new Tuple2<>(1, 2);

        // When
        Object result = function.apply(input);

        // Then
        assertNull(result);
    }
}
