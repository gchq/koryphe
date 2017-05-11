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
import uk.gov.gchq.koryphe.tuple.predicate.IntegerTupleAdaptedPredicate;
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

public class OrTest extends PredicateTest {

    @Test
    public void shouldAcceptWhenOneFunctionsAccepts() {
        // Given
        final Predicate<String> func1 = mock(Predicate.class);
        final Predicate<String> func2 = mock(Predicate.class);
        final Predicate<String> func3 = mock(Predicate.class);
        final Or<String> or = new Or<>(func1, func2, func3);

        given(func1.test("value")).willReturn(false);
        given(func2.test("value")).willReturn(true);
        given(func3.test("value")).willReturn(true);

        // When
        boolean accepted = or.test("value");

        // Then
        assertTrue(accepted);
        verify(func1).test("value");
        verify(func2).test("value");
        verify(func3, never()).test("value");
    }

    @Test
    public void shouldRejectWhenNoFunctions() {
        // Given
        final Or or = new Or();

        // When
        boolean accepted = or.test(new String[]{"test"});

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldRejectWhenNoFunctionsOrNullInput() {
        // Given
        final Or<String> or = new Or<>();

        // When
        boolean accepted = or.test(null);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldRejectWhenAllFunctionsReject() {
        // Given
        final Predicate<String> func1 = mock(Predicate.class);
        final Predicate<String> func2 = mock(Predicate.class);
        final Predicate<String> func3 = mock(Predicate.class);
        final Or<String> or = new Or<>(func1, func2, func3);

        given(func1.test("value")).willReturn(false);
        given(func2.test("value")).willReturn(false);
        given(func3.test("value")).willReturn(false);

        // When
        boolean accepted = or.test("value");

        // Then
        assertFalse(accepted);
        verify(func1).test("value");
        verify(func2).test("value");
        verify(func3).test("value");
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Or filter = new Or(new IsA());

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.Or\",%n" +
                "  \"predicates\" : [ {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\"%n" +
                "  } ]%n" +
                "}"), json);

        // When 2
        final Or deserialisedFilter = JsonSerialiser.deserialise(json, Or.class);

        // Then 2
        assertNotNull(deserialisedFilter);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseComplex() throws IOException {
        // Given
        final Or predicate = new Or.Builder()
                .select(0)
                .execute(new IsMoreThan(1))
                .select(1)
                .execute(new IsLessThan(10.0))
                .build();

        // When
        final String json = JsonSerialiser.serialise(predicate);

        // Then
        JsonSerialiser.assertEquals("{" +
                "\"class\":\"uk.gov.gchq.koryphe.impl.predicate.Or\"," +
                "\"predicates\":[{" +
                "\"class\":\"uk.gov.gchq.koryphe.tuple.predicate.IntegerTupleAdaptedPredicate\"," +
                "\"predicate\":{" +
                "\"class\":\"uk.gov.gchq.koryphe.impl.predicate.IsMoreThan\"," +
                "\"orEqualTo\":false," +
                "\"value\":1}," +
                "\"selection\":[0]" +
                "}," +
                "{\"class\":\"uk.gov.gchq.koryphe.tuple.predicate.IntegerTupleAdaptedPredicate\"," +
                "\"predicate\":{" +
                "\"class\":\"uk.gov.gchq.koryphe.impl.predicate.IsLessThan\"," +
                "\"orEqualTo\":false," +
                "\"value\":10.0" +
                "}," +
                "\"selection\":[1]" +
                "}" +
                "]}", json);

        // When 2
        final Or deserialisedFilter = JsonSerialiser.deserialise(json, Or.class);

        // Then 2
        assertNotNull(deserialisedFilter);
    }

    @Test
    public void shouldCheckInputClass() {
        Or<?> predicate = new Or<>(new IsMoreThan(1), new IsLessThan(10));
        assertTrue(predicate.isInputValid(Integer.class).isValid());
        assertFalse(predicate.isInputValid(Double.class).isValid());
        assertFalse(predicate.isInputValid(Integer.class, Integer.class).isValid());

        predicate = new Or<>(new IsMoreThan(1.0), new IsLessThan(10.0));
        assertTrue(predicate.isInputValid(Double.class).isValid());
        assertFalse(predicate.isInputValid(Integer.class).isValid());

        predicate = new Or<>(new IsMoreThan(1), new IsLessThan(10.0));
        assertFalse(predicate.isInputValid(Integer.class).isValid());
        assertFalse(predicate.isInputValid(Integer.class, Double.class).isValid());

        predicate = new Or<>(
                new IntegerTupleAdaptedPredicate(new IsMoreThan(1), 0),
                new IntegerTupleAdaptedPredicate(new IsLessThan(10.0), 1)
        );
        assertTrue(predicate.isInputValid(Integer.class, Double.class).isValid());
        assertFalse(predicate.isInputValid(Integer.class).isValid());
        assertFalse(predicate.isInputValid(Double.class, Integer.class).isValid());

        predicate = new Or.Builder()
                .select(0)
                .execute(new IsMoreThan(1))
                .select(1)
                .execute(new IsLessThan(10.0))
                .build();
        assertTrue(predicate.isInputValid(Integer.class, Double.class).isValid());
        assertFalse(predicate.isInputValid(Integer.class).isValid());
        assertFalse(predicate.isInputValid(Double.class, Integer.class).isValid());
    }

    @Override
    protected Class<Or> getPredicateClass() {
        return Or.class;
    }

    @Override
    protected Or getInstance() {
        return new Or();
    }
}
