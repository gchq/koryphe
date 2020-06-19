/*
 * Copyright 2019-2020 Crown Copyright
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
package uk.gov.gchq.koryphe.predicate;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.function.ToString;
import uk.gov.gchq.koryphe.impl.predicate.IsEqual;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdaptedPredicateTest {

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final AdaptedPredicate original = new AdaptedPredicate(new ToString(), new IsEqual("3"));

        // When
        final String serialised = JsonSerialiser.serialise(original);

        // Then
        String expected = "{" +
                    "\"inputAdapter\":{" +
                        "\"class\":\"uk.gov.gchq.koryphe.impl.function.ToString\"" +
                    "}," +
                    "\"predicate\":{" +
                        "\"class\":\"uk.gov.gchq.koryphe.impl.predicate.IsEqual\"," +
                        "\"value\":\"3\"" +
                    "}" +
                "}";

        assertEquals(expected, serialised);

        // When
        final AdaptedPredicate deserialised = JsonSerialiser.deserialise(serialised, AdaptedPredicate.class);

        // Then
        assertEquals(original.getPredicate().getClass(), deserialised.getPredicate().getClass());
        assertEquals(original.getInputAdapter().getClass(), deserialised.getInputAdapter().getClass());
        assertEquals(((IsEqual) original.getPredicate()).getControlValue(), ((IsEqual) deserialised.getPredicate()).getControlValue());
    }
}
