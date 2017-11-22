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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TimeWindowTest extends PredicateTest {
    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final TimeWindow filter = new TimeWindow.Builder()
                .start(1000L)
                .size(10L)
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.TimeWindow\",%n" +
                "  \"start\" : 1000,%n" +
                "  \"size\" : 10%n" +
                "}"), json);

        // When 2
        final TimeWindow deserialisedFilter = JsonSerialiser.deserialise(json, TimeWindow.class);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(1000L, (long) deserialisedFilter.getStart());
        assertEquals(10L, (long) deserialisedFilter.getSize());
        assertNull(deserialisedFilter.getStartOffset());
    }

    @Override
    protected Class<TimeWindow> getPredicateClass() {
        return TimeWindow.class;
    }

    @Override
    protected TimeWindow getInstance() {
        return new TimeWindow.Builder()
                .start(1000L)
                .size(10L)
                .build();
    }
}
