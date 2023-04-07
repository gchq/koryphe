/*
 * Copyright 2019-2023 Crown Copyright
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DictionaryLookupTest extends FunctionTest<DictionaryLookup<?, ?>> {

    private Map<String, Integer> dictionary = new HashMap<>();

    private DictionaryLookup<String, Integer> dictionaryLookUp;

    @BeforeEach
    public void setup() {
        dictionary.put("one", 1);
        dictionary.put("two", 2);
        dictionaryLookUp = new DictionaryLookup<>(dictionary);
    }

    @Test
    public void shouldReturnExistingValueInDictionary() {
        assertThat((int) dictionaryLookUp.apply("one"))
                .isEqualTo(1);
        assertThat((int) dictionaryLookUp.apply("two"))
                .isEqualTo(2);
    }

    @Test
    public void shouldReturnNullIfNullKeyIsSupplied() {
        assertThat(dictionaryLookUp.apply(null)).isNull();
    }

    @Test
    public void shouldReturnNullIfItemDoesntExistInDictionary() {
        assertThat(dictionaryLookUp.apply("three")).isNull();
    }

    @Test
    public void shouldThrowExceptionIfDictionaryIsSetToNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new DictionaryLookup<>().apply("four"))
                .withMessage("The uk.gov.gchq.koryphe.impl.function.DictionaryLookup KorypheFunction has not been provided with a dictionary");
    }

    @Override
    protected DictionaryLookup<String, Integer> getInstance() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);

        return new DictionaryLookup(map);
    }

    @Override
    protected Iterable<DictionaryLookup<?, ?>> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new DictionaryLookup<>(null),
                new DictionaryLookup<>(new HashMap<>())
        );
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Object.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Object.class};
    }

    @Test
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
        assertThat(deserialised).isNotNull();
    }
}
