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

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateObjectTest extends FunctionTest<CreateObject> {

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
        final Exception exception = assertThrows(RuntimeException.class, () -> function.apply(value));
        final String expected = "Unable to create a new instance of java.util.ArrayList. No constructors were found " +
                "that accept a java.util.HashMap";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfPrivateConstructor() {
        // Given
        final CreateObject function = new CreateObject(TestClass.class);

        // When / Then
        final Exception exception = assertThrows(RuntimeException.class, () -> function.apply(null));
        final String expected = "Unable to create a new instance of " +
                "uk.gov.gchq.koryphe.impl.function.CreateObjectTest$TestClass using the no-arg constructor";
        assertEquals(expected, exception.getMessage());
    }

    @Test
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
    protected CreateObject getInstance() {
        return new CreateObject();
    }

    @Override
    protected Iterable<CreateObject> getDifferentInstances() {
        return Collections.singletonList(new CreateObject(Integer.class));
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Object.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Object.class};
    }

    private static final class TestClass {
        private TestClass() {
        }
    }
}
