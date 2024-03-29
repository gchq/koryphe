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

package uk.gov.gchq.koryphe.impl.binaryoperator;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class OrTest extends BinaryOperatorTest<Or> {

    @Test
    public void shouldCompareBooleans() {
        // Given
        final Or function = new Or();

        // When
        final boolean ff = function.apply(false, false);
        final boolean ft = function.apply(false, true);
        final boolean tf = function.apply(true, false);
        final boolean tt = function.apply(true, true);

        // Then
        assertThat(ff).isFalse();
        assertThat(ft).isTrue();
        assertThat(tf).isTrue();
        assertThat(tt).isTrue();
    }

    @Test
    public void shouldHandleNulls() {
        // Given
        final Or function = new Or();

        // When
        final boolean nf = function.apply(null, false);
        final boolean fn = function.apply(false, null);

        // Then
        assertThat(nf).isFalse();
        assertThat(fn).isFalse();
    }

    @Override
    protected Or getInstance() {
        return new Or();
    }

    @Override
    protected Iterable<Or> getDifferentInstancesOrNull() {
        return null;
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Or function = new Or();

        // When 1
        final String json = JsonSerialiser.serialise(function);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.Or\"%n" +
                "}"), json);

        // When 2
        final Or deserialisedFunction = JsonSerialiser.deserialise(json, Or.class);

        // Then 2
        assertThat(deserialisedFunction).isNotNull();
    }
}
