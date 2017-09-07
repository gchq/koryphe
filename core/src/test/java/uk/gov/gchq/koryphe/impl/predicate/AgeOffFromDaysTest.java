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

package uk.gov.gchq.koryphe.impl.predicate;

import org.junit.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AgeOffFromDaysTest extends PredicateTest {
    public static final int MINUTE_IN_MILLISECONDS = 60000;
    public static final int DAY_IN_MILLISECONDS = 24 * 60 * 60 * 1000;
    public static final int AGE_OFF_DAYS = 14;
    public static final long AGE_OFF_MILLISECONDS = AGE_OFF_DAYS * DAY_IN_MILLISECONDS;

    @Test
    public void shouldAcceptWhenWithinAgeOffLimit() {
        // Given
        final AgeOffFromDays filter = new AgeOffFromDays();

        // When
        final boolean accepted = filter.test(System.currentTimeMillis() - AGE_OFF_MILLISECONDS + MINUTE_IN_MILLISECONDS, AGE_OFF_DAYS);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldAcceptWhenOutsideAgeOffLimit() {
        // Given
        final AgeOffFromDays filter = new AgeOffFromDays();

        // When
        final boolean accepted = filter.test(System.currentTimeMillis() - AGE_OFF_MILLISECONDS - DAY_IN_MILLISECONDS, AGE_OFF_DAYS);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldNotAcceptWhenTimestampIsNull() {
        // Given
        final AgeOffFromDays filter = new AgeOffFromDays();

        // When
        final boolean accepted = filter.test(null, 0);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldNotAcceptWhenDaysIsNull() {
        // Given
        final AgeOffFromDays filter = new AgeOffFromDays();

        // When
        final boolean accepted = filter.test(0L, null);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final AgeOffFromDays filter = new AgeOffFromDays();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.AgeOffFromDays\"%n" +
                "}"), json);

        // When 2
        final AgeOffFromDays deserialisedFilter = JsonSerialiser.deserialise(json, AgeOffFromDays.class);

        // Then 2
        assertNotNull(deserialisedFilter);
    }

    @Override
    protected Class<AgeOffFromDays> getPredicateClass() {
        return AgeOffFromDays.class;
    }

    @Override
    protected AgeOffFromDays getInstance() {
        return new AgeOffFromDays();
    }
}
