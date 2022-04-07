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

package uk.gov.gchq.koryphe.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.function.MultiplyLongBy;
import uk.gov.gchq.koryphe.impl.function.StringSplit;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.impl.function.ToString;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class FunctionCompositeTest extends FunctionTest<FunctionComposite>{
    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Object.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        FunctionComposite functionComposite = getInstance();
        String json =
                "{" +
                    "\"functions\": [" +
                        "{" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.function.ToLong\"" +
                        "}," +
                        "{" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.function.MultiplyLongBy\"," +
                            "\"by\": 10" +
                        "}" +
                    "]" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(functionComposite);
        FunctionComposite deserialised = JsonSerialiser.deserialise(json, FunctionComposite.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertThat(deserialised).isEqualTo(functionComposite);
    }

    @Override
    protected FunctionComposite getInstance() {
        return new FunctionComposite(Arrays.asList(
                new ToLong(),
                new MultiplyLongBy(10L)
        ));
    }

    @Override
    protected Iterable<FunctionComposite> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new FunctionComposite(),
                new FunctionComposite(Arrays.asList(
                        new ToString(),
                        new StringSplit(",")
                )),
                new FunctionComposite(Arrays.asList(
                        new ToLong(),
                        new MultiplyLongBy(100L)
                ))
        );
    }

    @Test
    public void shouldReturnInputIfNoComponentsAreProvided() {
        // Given
        FunctionComposite<String, String, Function> functionComposite = new FunctionComposite<>();

        // When
        String output = functionComposite.apply("test");

        // Then
        assertThat(output).isEqualTo("test");
    }

    @Test
    public void shouldApplyFunctionsInOrder() {
        // Given
        FunctionComposite<String, Long, Function> functionComposite = new FunctionComposite<>(Arrays.asList(
                new ToLong(),
                new MultiplyLongBy(100L)
        ));

        // When
        Long transformed = functionComposite.apply("4");

        // Then
        assertThat(transformed).isEqualTo(400L);
    }

    @Test
    public void shouldThrowExceptionIfInputsAndOutputsDontMatch() {
        // Given
        FunctionComposite<Integer, Long, Function> functionComposite = new FunctionComposite<>(Arrays.asList(
                new ToLong(),
                new StringSplit(" ")
        ));

        // When / Then
        assertThatExceptionOfType(ClassCastException.class)
                .isThrownBy(() -> functionComposite.apply(5))
                .withMessageContaining("java.lang.Long cannot be cast to class java.lang.String");
    }
}
