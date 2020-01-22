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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class IsLongerThanTest extends PredicateTest {
    @Test
    public void shouldSetAndGetMinLength() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When
        final int minLength1 = filter.getMinLength();
        filter.setMinLength(10);
        final int minLength2 = filter.getMinLength();

        // Then
        assertEquals(5, minLength1);
        assertEquals(10, minLength2);
    }

    @Test
    public void shouldAcceptTheValueWhenMoreThan() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When
        final boolean accepted = filter.test("123456");

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenLessThan() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When
        final boolean accepted = filter.test("1234");

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldAcceptTheIterableValueWhenEqualTo() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5, true);

        // When
        final boolean accepted = filter.test(Collections.nCopies(5, "item"));

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectTheIterableValueWhenNotEqualTo() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5, false);

        // When
        final boolean accepted = filter.test(Collections.nCopies(5, "item"));

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldAcceptTheIterableValueWhenMoreThan() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5, true);

        // When
        final boolean accepted = filter.test(Collections.nCopies(6, "item"));

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectTheIterableValueWhenLessThan() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When
        final boolean accepted = filter.test(Collections.nCopies(4, "item"));

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldRejectTheValueWhenEqual() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When
        final boolean accepted = filter.test("12345");

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldThrowExceptionWhenTheValueWhenUnknownType() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When / Then
        try {
            filter.test(4);
            fail("Exception expected");
        } catch (final IllegalArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final int min = 5;
        final IsLongerThan filter = new IsLongerThan(min);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsLongerThan\",%n" +
                "  \"orEqualTo\" : false,%n" +
                "  \"minLength\" : 5%n" +
                "}"), json);

        // When 2
        final IsLongerThan deserialisedFilter = JsonSerialiser.deserialise(json, IsLongerThan.class);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(min, deserialisedFilter.getMinLength());
    }

    @Test
    public void shouldCheckInputClass() {
        final IsLongerThan predicate = new IsLongerThan(10);

        assertTrue(predicate.isInputValid(String.class).isValid());
        assertTrue(predicate.isInputValid(Object[].class).isValid());
        assertTrue(predicate.isInputValid(Integer[].class).isValid());
        assertTrue(predicate.isInputValid(Collection.class).isValid());
        assertTrue(predicate.isInputValid(List.class).isValid());
        assertTrue(predicate.isInputValid(Map.class).isValid());
        assertTrue(predicate.isInputValid(HashMap.class).isValid());

        assertFalse(predicate.isInputValid(String.class, HashMap.class).isValid());
        assertFalse(predicate.isInputValid(Double.class).isValid());
        assertFalse(predicate.isInputValid(Integer.class, Integer.class).isValid());
    }

    @Override
    protected Class<IsLongerThan> getPredicateClass() {
        return IsLongerThan.class;
    }

    @Override
    protected IsLongerThan getInstance() {
        return new IsLongerThan(5);
    }
}
