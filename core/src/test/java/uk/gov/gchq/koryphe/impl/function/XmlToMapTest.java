/*
 * Copyright 2017-2018 Crown Copyright
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

import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlToMapTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new XmlToMap();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return XmlToMap.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final XmlToMap function = new XmlToMap();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.XmlToMap\"%n" +
                "}"), json);
    }

    @Test
    public void shouldParseXml() {
        // Given
        final XmlToMap function = new XmlToMap();
        final String input = "<root><element1><element2>value1</element2></element1><element1><element2>value2</element2></element1></root>";

        // When
        Map<String, Object> result = function.apply(input);

        // Then
        Map<String, Object> element2aMap = new HashMap<>();
        element2aMap.put("element2", "value1");
        Map<String, Object> element2bMap = new HashMap<>();
        element2bMap.put("element2", "value2");
        HashMap<Object, Object> element1Map = new HashMap<>();
        element1Map.put("element1", Arrays.asList(element2aMap, element2bMap));
        HashMap<Object, Object> rootMap = new HashMap<>();
        rootMap.put("root", element1Map);
        assertEquals(rootMap, result);
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final XmlToMap function = new XmlToMap();
        final String input = null;

        // When
        Map<String, Object> result = function.apply(input);

        // Then
        assertNull(result);
    }
}
