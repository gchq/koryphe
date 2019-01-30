/*
 * Copyright 2017-2019 Crown Copyright
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
import uk.gov.gchq.koryphe.serialisation.json.SimpleClassNameCache;
import uk.gov.gchq.koryphe.util.CustomObj;
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
        final CustomObj controlValue = new CustomObj();
        final IsLessThan filter = new IsLessThan(controlValue);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsLessThan\",%n" +
                "  \"orEqualTo\" : false,%n" +
                "  \"value\" : {\"uk.gov.gchq.koryphe.util.CustomObj\":{\"value\":\"1\"}}%n" +
                "}"), json);

        // When 2
        final IsLessThan deserialisedFilter = JsonSerialiser.deserialise(json, IsLessThan.class);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(controlValue, deserialisedFilter.getControlValue());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseWithSimpleLongClassName() throws IOException {
        // Given
        final IsLessThan filter = new IsLessThan(1L);

        // When
        final String json;
        try {
            SimpleClassNameCache.setUseFullNameForSerialisation(false);
            json = JsonSerialiser.serialise(filter);
        } finally {
            SimpleClassNameCache.setUseFullNameForSerialisation(SimpleClassNameCache.DEFAULT_USE_FULL_NAME_FOR_SERIALISATION);
        }

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"IsLessThan\",%n" +
                "  \"orEqualTo\" : false,%n" +
                "  \"value\" : {\"Long\":1}%n" +
                "}"), json);

        // When 2
        final IsLessThan deserialisedFilter = JsonSerialiser.deserialise(json, IsLessThan.class);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(1L, deserialisedFilter.getControlValue());
    }

    @Test
    public void shouldCheckInputClass() {
        final IsLessThan predicate = new IsLessThan(1);

        assertTrue(predicate.isInputValid(Integer.class).isValid());
        assertFalse(predicate.isInputValid(Double.class).isValid());
        assertFalse(predicate.isInputValid(Integer.class, Integer.class).isValid());
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
