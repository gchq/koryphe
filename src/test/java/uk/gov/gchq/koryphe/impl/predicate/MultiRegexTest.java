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
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MultiRegexTest extends PredicateTest {

    @Test
    public void shouldAccepValidValue() {
        // Given
        Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile("fail");
        patterns[1] = Pattern.compile("pass");
        final MultiRegex filter = new MultiRegex(patterns);

        // When
        boolean accepted = filter.test("pass");

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectInvalidValue() {
        // Given
        Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile("fail");
        patterns[1] = Pattern.compile("reallyFail");
        final MultiRegex filter = new MultiRegex(patterns);

        // When
        boolean accepted = filter.test("pass");

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile("test");
        patterns[1] = Pattern.compile("test2");
        final MultiRegex filter = new MultiRegex(patterns);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.MultiRegex\",%n" +
                "  \"value\" : [ {%n" +
                "    \"java.util.regex.Pattern\" : \"test\"%n" +
                "  }, {%n" +
                "    \"java.util.regex.Pattern\" : \"test2\"%n" +
                "  } ]%n" +
                "}"), json);

        // When 2
        final MultiRegex deserialisedFilter = JsonSerialiser.deserialise(json, MultiRegex.class);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(patterns[0].pattern(), deserialisedFilter.getPatterns()[0].pattern());
        assertEquals(patterns[1].pattern(), deserialisedFilter.getPatterns()[1].pattern());
    }

    @Override
    protected MultiRegex getInstance() {
        Pattern[] patterns = new Pattern[2];
        patterns[0] = Pattern.compile("NOTHING");
        patterns[1] = Pattern.compile("[t,T].*[t,T]");
        MultiRegex multi = new MultiRegex(patterns);
        return multi;
    }

    @Override
    protected Class<MultiRegex> getPredicateClass() {
        return MultiRegex.class;
    }
}