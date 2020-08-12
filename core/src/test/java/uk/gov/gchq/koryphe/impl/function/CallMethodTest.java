/*
 * Copyright 2018-2020 Crown Copyright
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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CallMethodTest extends FunctionTest {

    private static final String TEST_METHOD = "testMethod";

    @Test
    public void shouldCallMethod() {
        // Given
        final CallMethod function = new CallMethod(TEST_METHOD);

        // When
        final Object output = function.apply(this);

        // Then
        assertEquals(5, output);
    }

    @Test
    public void shouldGetMethodFromCache() throws Exception {
        // Given
        final Map<Object, Object> expectedCache = new HashMap<>();
        expectedCache.put(getClass(), getClass().getMethod(TEST_METHOD));

        final CallMethod function = new CallMethod(TEST_METHOD);
        final Map<Class, Method> initialCache = function.getCache();
        // When
        Object output = function.apply(this);

        // Then - check the cache has been updated
        final Map<Class, Method> interimCache = function.getCache();
        assertNotSame(initialCache, interimCache);
        assertEquals(expectedCache, interimCache);
        assertEquals(5, output);

        // When
        Object output2 = function.apply(this);

        // Then - check the cache hasn't changed
        final Map<Class, Method> finalCache = function.getCache();
        assertSame(interimCache, finalCache);
        assertEquals(expectedCache, finalCache);
        assertEquals(5, output2);
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final CallMethod function = new CallMethod(TEST_METHOD);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.CallMethod\",%n" +
                "  \"method\" : \"testMethod\"%n" +
                "}"), json);

        // When 2
        final CallMethod deserialisedCallMethod = JsonSerialiser.deserialise(json, CallMethod.class);

        // Then 2
        assertNotNull(deserialisedCallMethod);
        assertEquals(TEST_METHOD, deserialisedCallMethod.getMethod());
    }

    @Override
    protected Function getInstance() {
        return new CallMethod();
    }

    @Override
    protected Iterable<Function> getDifferentInstances() {
        return Collections.singletonList(new CallMethod("toString"));
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return CallMethod.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Object.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Object.class};
    }

    // Test method for use when testing the CallMethod function
    public int testMethod() {
        return 5;
    }
}
