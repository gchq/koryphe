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

package uk.gov.gchq.koryphe.binaryoperator;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.binaryoperator.Product;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


class BinaryOperatorCompositeTest extends BinaryOperatorTest<BinaryOperatorComposite> {
    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        BinaryOperatorComposite boc = getInstance();
        String json = "{" +
                    "\"operators\": [" +
                        "{" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.binaryoperator.Sum\"" +
                        "}," +
                        "{" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.binaryoperator.Product\"" +
                        "}" +
                    "]" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(boc);
        BinaryOperatorComposite deserialised = JsonSerialiser.deserialise(json, BinaryOperatorComposite.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertThat(deserialised).isEqualTo(boc);
    }

    @Override
    protected BinaryOperatorComposite getInstance() {
        return new BinaryOperatorComposite(Arrays.asList(
                new Sum(),
                new Product()
        ));
    }

    @Override
    protected Iterable<BinaryOperatorComposite> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new BinaryOperatorComposite(),
                new BinaryOperatorComposite(Arrays.asList(
                        new Product(),
                        new Sum()
                )),
                new BinaryOperatorComposite(Arrays.asList(
                        new Sum(),
                        new Sum()
                ))
        );
    }

    @Test
    public void shouldExecuteEachBinaryOperatorInOrder() {
        // Given
        BinaryOperatorComposite instance = getInstance(); // Sum then Product
        BinaryOperatorComposite reversed = new BinaryOperatorComposite(Arrays.asList(
                new Product(),
                new Sum()
        ));

        // When
        Object aggregated = instance.apply(5, 10);
        Object aggregatedInReverseOrder = reversed.apply(5, 10);

        // Then
        assertThat(aggregated).isEqualTo(150); // (5 + 10) x 10 = 150
        assertThat(aggregatedInReverseOrder).isEqualTo(60); // (5 x 10) + 10 = 60
    }
}
