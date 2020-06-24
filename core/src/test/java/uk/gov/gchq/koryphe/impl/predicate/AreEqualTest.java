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

package uk.gov.gchq.koryphe.impl.predicate;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AreEqualTest extends PredicateTest {

    @Test
    public void shouldAcceptTheWhenEqualValues() {
        // Given
        final AreEqual equals = new AreEqual();

        // When
        boolean accepted = equals.test("test", "test");

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldAcceptWhenAllNull() {
        // Given
        final AreEqual equals = new AreEqual();

        // When
        boolean accepted = equals.test(null, null);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectWhenOneIsNull() {
        // Given
        final AreEqual equals = new AreEqual();

        // When
        boolean accepted = equals.test(null, "test");

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldRejectWhenNotEqual() {
        // Given
        final AreEqual equals = new AreEqual();

        // When
        boolean accepted = equals.test("test", "test2");

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final AreEqual filter = new AreEqual();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.AreEqual\"%n" +
                "}"), json);

        // When 2
        final AreEqual deserialisedFilter = JsonSerialiser.deserialise(json, AreEqual.class);

        // Then 2
        assertNotNull(deserialisedFilter);
    }

    @Override
    protected Class<AreEqual> getPredicateClass() {
        return AreEqual.class;
    }

    @Override
    protected AreEqual getInstance() {
        return new AreEqual();
    }
}
