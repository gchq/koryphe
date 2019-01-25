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

import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CreateObjectTest extends FunctionTest {

    @Test
    public void shouldCreateNewObjectUsingNoArgConstructor() {
        // Given
        final CreateObject function = new CreateObject(ArrayList.class);

        // When
        Object output = function.apply(null);

        // Then
        assertEquals(new ArrayList<>(), output);
    }

    @Test
    public void shouldCreateNewObjectUsingSingleArgConstructor() {
        // Given
        final CreateObject function = new CreateObject(ArrayList.class);
        List<Integer> value = Arrays.asList(1, 2, 3);

        // When
        Object output = function.apply(value);

        // Then
        assertEquals(value, output);
        assertNotSame(value, output);
    }

    @Test
    public void shouldThrowExceptionIfNoConstructorWithArgTypeIsFound() {
        // Given
        final CreateObject function = new CreateObject(ArrayList.class);
        Map<String, String> value = new HashMap<>();

        // When / Then
        try {
            function.apply(value);
            fail("Exception expected");
        } catch (final RuntimeException e) {
            assertTrue(e.getMessage().contains("Unable to create a new instance"));
        }
    }

    @Test
    public void shouldThrowExceptionIfPrivateConstructor() {
        // Given
        final CreateObject function = new CreateObject(TestClass.class);

        // When / Then
        try {
            function.apply(null);
            fail("Exception expected");
        } catch (final RuntimeException e) {
            assertTrue(e.getMessage().contains("Unable to create a new instance"));
        }
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final CreateObject function = new CreateObject(ArrayList.class);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.CreateObject\",%n" +
                "  \"objectClass\" : \"" + ArrayList.class.getName() + "\"%n" +
                "}"), json);

        // When 2
        final CreateObject deserialised = JsonSerialiser.deserialise(json, CreateObject.class);

        // Then 2
        assertNotNull(deserialised);
        assertEquals(ArrayList.class, deserialised.getObjectClass());
    }

    @Override
    protected Function getInstance() {
        return new CreateObject();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return CreateObject.class;
    }

    private static final class TestClass {
        private TestClass() {
        }
    }
}
