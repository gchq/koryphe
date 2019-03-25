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

package uk.gov.gchq.koryphe.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;

import uk.gov.gchq.koryphe.serialisation.json.SimpleClassNameIdResolver;

import java.io.IOException;
import java.util.Map;

public class JsonSerialiser {
    private static final ObjectMapper BASIC__MAPPER = new ObjectMapper();
    private static ObjectMapper mapper = createObjectMapper();

    public static void resetMapper() {
        mapper = createObjectMapper();
    }

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleClassNameIdResolver.configureObjectMapper(mapper);
        return mapper;
    }

    public static String serialise(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T deserialise(String json, Class<T> type) throws IOException {
        return mapper.readValue(json, type);
    }

    public static <T> T deserialise(String json, TypeReference<T> typeReference) throws IOException {
        return mapper.readValue(json, typeReference);
    }


    public static void assertEquals(final String expectedJson, final String actualJson) {
        try {
            final Map expectedSchemaMap = BASIC__MAPPER.readValue(expectedJson, Map.class);
            final Map actualSchemaMap = BASIC__MAPPER.readValue(actualJson, Map.class);
            Assert.assertEquals(expectedSchemaMap, actualSchemaMap);
        } catch (final IOException e) {
            throw new AssertionError(expectedJson + "is not equal to " + actualJson, e);
        }
    }

    public static void assertEquals(final byte[] expectedJson, final byte[] actualJson) {
        assertEquals(new String(expectedJson), new String(actualJson));
    }
}
