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
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class AgeOffTest extends PredicateTest<AgeOff> {

    public static final int MINUTE_IN_MILLISECONDS = 60000;
    public static final long CUSTOM_AGE_OFF = 100000;

    @Test
    public void shouldUseDefaultAgeOffTime() {
        // Given
        final AgeOff filter = new AgeOff();

        // When
        final long ageOffTime = filter.getAgeOffTime();

        // Then
        assertThat(ageOffTime).isEqualTo(AgeOff.AGE_OFF_TIME_DEFAULT);
    }

    @Test
    public void shouldSetAgeOffInDays() {
        // Given
        final int ageOffInDays = 10;
        final AgeOff filter = new AgeOff();
        filter.setAgeOffDays(ageOffInDays);

        // When
        final long ageOffTime = filter.getAgeOffTime();

        // Then
        assertThat(ageOffTime).isEqualTo(ageOffInDays * 24 * 60 * 60 * 1000);
    }

    @Test
    public void shouldSetAgeOffInHours() {
        // Given
        final int ageOffInHours = 10;
        final AgeOff filter = new AgeOff();
        filter.setAgeOffHours(ageOffInHours);

        // When
        final long ageOffTime = filter.getAgeOffTime();

        // Then
        assertThat(ageOffTime).isEqualTo(ageOffInHours * 60 * 60 * 1000);
    }

    @Test
    public void shouldAcceptWhenWithinAgeOffLimit() {
        // Given
        final AgeOff filter = new AgeOff(CUSTOM_AGE_OFF);

        // When / Then
        assertThat(filter).accepts(System.currentTimeMillis() - CUSTOM_AGE_OFF + MINUTE_IN_MILLISECONDS);
    }

    @Test
    public void shouldRejectWhenOutsideAgeOffLimit() {
        // Given
        final AgeOff filter = new AgeOff(CUSTOM_AGE_OFF);

        // When / Then
        assertThat(filter).rejects(System.currentTimeMillis() - CUSTOM_AGE_OFF - MINUTE_IN_MILLISECONDS);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final AgeOff filter = new AgeOff(CUSTOM_AGE_OFF);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.AgeOff\",%n" +
                "  \"ageOffTime\" : 100000%n" +
                "}"), json);

        // When 2
        final AgeOff deserialisedFilter = JsonSerialiser.deserialise(json, AgeOff.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
        assertThat(deserialisedFilter.getAgeOffTime()).isEqualTo(CUSTOM_AGE_OFF);
    }

    @Override
    protected AgeOff getInstance() {
        return new AgeOff();
    }

    @Override
    protected Iterable<AgeOff> getDifferentInstancesOrNull() {
        return Collections.singletonList(new AgeOff(100L));
    }
}
