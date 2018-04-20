/*
 * Copyright 2018 Crown Copyright
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
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CallMethodTest extends FunctionTest {

    @Test
    public void shouldCallMethod() throws Exception {
        // Given
        final CallMethod function = new CallMethod("testMethod");

        // When
        Object output = function.apply(this);

        // Then
        assertEquals(5, output);
    }

    @Test
    public void shouldGetMethodFromCache() throws Exception {
        // Given
        final CallMethod function = mock(CallMethod.class);
        given(function.getMethod()).willReturn("testMethod");

        // When
        Object output = function.apply(this);

        // Then
        verify(function, times(1)).getMethodFromClass(any());
        assertEquals(5, output);

        // When
        Object output2 = function.apply(this);

        // Then
        verify(function, times(1)).getMethodFromClass(any());
        assertEquals(5, output2);
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final CallMethod function = new CallMethod("testMethod");

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
        assertEquals("testMethod", deserialisedCallMethod.getMethod());
    }

    @Override
    protected Function getInstance() {
        return new CallMethod();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return CallMethod.class;
    }

    // Test method for use when testing the CallMethod function
    public int testMethod() {
        return 5;
    }
}
