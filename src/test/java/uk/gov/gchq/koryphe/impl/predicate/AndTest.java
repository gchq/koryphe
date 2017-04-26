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
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class AndTest extends PredicateTest {

    @Test
    public void shouldAcceptWhenAllFunctionsAccept() {
        // Given
        final Predicate<String> func1 = mock(Predicate.class);
        final Predicate<String> func2 = mock(Predicate.class);
        final Predicate<String> func3 = mock(Predicate.class);
        final And<String> and = new And<>(func1, func2, func3);

        given(func1.test("value")).willReturn(true);
        given(func2.test("value")).willReturn(true);
        given(func3.test("value")).willReturn(true);

        // When
        boolean accepted = and.test("value");

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldAcceptWhenNoFunctions() {
        // Given
        final And and = new And();

        // When
        boolean accepted = and.test(new String[]{"test"});

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldAcceptWhenNoFunctionsAndNullInput() {
        // Given
        final And and = new And();

        // When
        boolean accepted = and.test(null);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectWhenOneFunctionRejects() {
        // Given
        final Predicate<String> func1 = mock(Predicate.class);
        final Predicate<String> func2 = mock(Predicate.class);
        final Predicate<String> func3 = mock(Predicate.class);
        final And<String> and = new And<>(func1, func2, func3);

        given(func1.test("value")).willReturn(true);
        given(func2.test("value")).willReturn(false);
        given(func3.test("value")).willReturn(true);

        // When
        boolean accepted = and.test("value");

        // Then
        assertFalse(accepted);
        verify(func1).test("value");
        verify(func2).test("value");
        verify(func3, never()).test("value");
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final And filter = new And(new IsA(String.class));

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.And\",%n" +
                "  \"predicates\" : [ {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.String\"%n" +
                "  } ]%n" +
                "}"), json);


        // When 2
        final And deserialisedFilter = JsonSerialiser.deserialise(json, And.class);

        // Then 2
        assertNotNull(deserialisedFilter);
    }

    @Override
    protected Class<And> getPredicateClass() {
        return And.class;
    }

    @Override
    protected And getInstance() {
        return new And();
    }
}
