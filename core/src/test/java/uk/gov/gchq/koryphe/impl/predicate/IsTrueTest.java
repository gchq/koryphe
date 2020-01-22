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

import org.junit.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IsTrueTest extends PredicateTest {
    @Test
    public void shouldAcceptTheValueWhenTrue() {
        // Given
        final IsTrue filter = new IsTrue();

        // When
        boolean accepted = filter.test(true);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldAcceptTheValueWhenObjectTrue() {
        // Given
        final IsTrue filter = new IsTrue();

        // When
        boolean accepted = filter.test(Boolean.TRUE);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenNull() {
        // Given
        final IsTrue filter = new IsTrue();

        // When
        boolean accepted = filter.test((Boolean) null);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenFalse() {
        // Given
        final IsTrue filter = new IsTrue();

        // When
        boolean accepted = filter.test(false);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IsTrue filter = new IsTrue();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsTrue\"%n" +
                "}"), json);

        // When 2
        final IsTrue deserialisedFilter = JsonSerialiser.deserialise(json, IsTrue.class);

        // Then 2
        assertNotNull(deserialisedFilter);
    }

    @Override
    protected Class<IsTrue> getPredicateClass() {
        return IsTrue.class;
    }

    @Override
    protected IsTrue getInstance() {
        return new IsTrue();
    }
}
