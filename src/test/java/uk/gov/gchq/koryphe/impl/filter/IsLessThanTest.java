/*
 * Copyright 2017 Crown Copyright
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
package uk.gov.gchq.koryphe.impl.filter;

import org.junit.Test;
import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IsLessThanTest extends PredicateTest {
    @Test
    public void shouldAcceptWhenLessThan() {
        // Given
        final IsLessThan filter = new IsLessThan(5);

        // When
        boolean accepted = filter.test(4);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldAcceptWhenLessThanAndOrEqualToIsTrue() {
        // Given
        final IsLessThan filter = new IsLessThan(5, true);

        // When
        boolean accepted = filter.test(4);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenMoreThan() {
        // Given
        final IsLessThan filter = new IsLessThan(5);

        // When
        boolean accepted = filter.test(6);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenMoreThanAndOrEqualToIsTrue() {
        // Given
        final IsLessThan filter = new IsLessThan(5, true);

        // When
        boolean accepted = filter.test(6);

        // Then
        assertFalse(accepted);
    }


    @Test
    public void shouldRejectTheValueWhenEqualTo() {
        // Given
        final IsLessThan filter = new IsLessThan(5);

        // When
        boolean accepted = filter.test(5);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldAcceptTheValueWhenEqualToAndOrEqualToIsTrue() {
        // Given
        final IsLessThan filter = new IsLessThan(5, true);

        // When
        boolean accepted = filter.test(5);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenEqual() {
        // Given
        final IsLessThan filter = new IsLessThan(5);

        // When
        boolean accepted = filter.test(5);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Integer controlValue = 5;
        final IsLessThan filter = new IsLessThan(controlValue);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.filter.IsLessThan\",%n" +
                "  \"orEqualTo\" : false,%n" +
                "  \"value\" : 5%n" +
                "}"), json);

        // When 2
        final IsLessThan deserialisedFilter = JsonSerialiser.deserialise(json, IsLessThan.class);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(controlValue, deserialisedFilter.getControlValue());
    }

    @Override
    protected Class<IsLessThan> getPredicateClass() {
        return IsLessThan.class;
    }

    @Override
    protected IsLessThan getInstance() {
        return new IsLessThan(5);
    }
}
