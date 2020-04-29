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

package uk.gov.gchq.koryphe.impl.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DictionaryLookupTest extends FunctionTest {

    private Map<String, Integer> dictionary = new HashMap<>();;
    private DictionaryLookup<String, Integer> dictionaryLookUp;

    @Before
    public void setup() {
        dictionary.put("one", 1);
        dictionary.put("two", 2);
        dictionaryLookUp = new DictionaryLookup<>(dictionary);
    }

    @Test
    public void shouldReturnExistingValueInDictionary() {
        assertEquals(1, (int) dictionaryLookUp.apply("one"));
        assertEquals(2, (int) dictionaryLookUp.apply("two"));
    }

    @Test
    public void shouldReturnNullIfNullKeyIsSupplied() {
        assertNull(dictionaryLookUp.apply(null));
    }

    @Test
    public void shouldReturnNullIfItemDoesntExistInDictionary() {
        assertNull(dictionaryLookUp.apply("three"));
    }

    @Test
    public void shouldThrowExceptionIfDictionaryIsSetToNull() {
        try {
            new DictionaryLookup<>().apply("four");
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("The uk.gov.gchq.koryphe.impl.function.DictionaryLookup KorypheFunction has not been provided with a dictionary", e.getMessage());
        }
    }

    @Override
    protected Function getInstance() {
        return new DictionaryLookup();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return DictionaryLookup.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // When
        final String json = JsonSerialiser.serialise(dictionaryLookUp);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.DictionaryLookup\"," +
                "   \"dictionary\" : {\"one\" : 1, \"two\" : 2}" +
                "}"), json);

        // When 2
        final DictionaryLookup deserialised = JsonSerialiser.deserialise(json, DictionaryLookup.class);

        // Then
        assertNotNull(deserialised);
    }
}
