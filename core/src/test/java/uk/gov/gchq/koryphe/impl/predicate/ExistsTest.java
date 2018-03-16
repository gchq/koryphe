/*
 * Copyright 2017-2018 Crown Copyright
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

public class ExistsTest extends PredicateTest {
    @Test
    public void shouldAcceptTheValueWhenNotNull() {
        // Given
        final Exists filter = new Exists();

        // When
        boolean accepted = filter.test("Not null value");

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenNull() {
        // Given
        final Exists filter = new Exists();

        // When
        boolean accepted = filter.test((Object) null);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Exists filter = new Exists();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.Exists\"%n" +
                "}"), json);

        // When 2
        final Exists deserialisedFilter = JsonSerialiser.deserialise(json, Exists.class);

        // Then 2
        assertNotNull(deserialisedFilter);
    }

    @Override
    protected Class<Exists> getPredicateClass() {
        return Exists.class;
    }

    @Override
    protected Exists getInstance() {
        return new Exists();
    }
}
