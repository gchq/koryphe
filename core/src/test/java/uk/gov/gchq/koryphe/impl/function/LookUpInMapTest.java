/*
 * Copyright 2019 Crown Copyright
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

import org.junit.Before;
import org.junit.Test;
import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LookUpInMapTest extends FunctionTest {

    private Map<String, Integer> lookUpMap = new HashMap<>();;
    private LookUpInMap<String, Integer> lookUpInMap;

    @Before
    public void setup() {
        lookUpMap.put("one", 1);
        lookUpMap.put("two", 2);
        lookUpInMap = new LookUpInMap<>(lookUpMap);
    }

    @Test
    public void apply() {
        assertEquals(1, (int)lookUpInMap.apply("one"));
        assertEquals(2, (int)lookUpInMap.apply("two"));
    }

    @Override
    protected Function getInstance() {
        return new LookUpInMap();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return LookUpInMap.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // When
        final String json = JsonSerialiser.serialise(lookUpInMap);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.LookUpInMap\"," +
                "   \"lookUpMap\" : {\"one\" : 1, \"two\" : 2}" +
                "}"), json);

        // When 2
        final LookUpInMap deserialised = JsonSerialiser.deserialise(json, LookUpInMap.class);

        // Then
        assertNotNull(deserialised);
    }
}