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

public class IsFalseTest extends PredicateTest {
    @Test
    public void shouldAcceptTheValueWhenFalse() {
        // Given
        final IsFalse filter = new IsFalse();

        // When
        boolean accepted = filter.test(false);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldAcceptTheValueWhenObjectFalse() {
        // Given
        final IsFalse filter = new IsFalse();

        // When
        boolean accepted = filter.test(Boolean.FALSE);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenNull() {
        // Given
        final IsFalse filter = new IsFalse();

        // When
        boolean accepted = filter.test((Boolean) null);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenTrue() {
        // Given
        final IsFalse filter = new IsFalse();

        // When
        boolean accepted = filter.test(true);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IsFalse filter = new IsFalse();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsFalse\"%n" +
                "}"), json);

        // When 2
        final IsFalse deserialisedFilter = JsonSerialiser.deserialise(json, IsFalse.class);

        // Then 2
        assertNotNull(deserialisedFilter);
    }

    @Override
    protected Class<IsFalse> getPredicateClass() {
        return IsFalse.class;
    }

    @Override
    protected IsFalse getInstance() {
        return new IsFalse();
    }
}
