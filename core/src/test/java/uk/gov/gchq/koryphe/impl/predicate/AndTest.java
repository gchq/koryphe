/*
 * Copyright 2017-2020 Crown Copyright
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

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.signature.InputValidatorAssert;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.predicate.IntegerTupleAdaptedPredicate;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class AndTest extends PredicateTest<And> {

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

        // When / Then
        assertThat(and).accepts("value");
    }

    @Test
    public void shouldAcceptWhenNoFunctions() {
        // Given
        final And and = new And();

        // When / Then
        assertThat(and).accepts(new String[] {"test"});
    }

    @Test
    public void shouldAcceptWhenNoFunctionsAndNullInput() {
        // Given
        final And and = new And();

        // When / Then
        assertThat(and).accepts((Object) null);
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

        // When / Then
        assertThat(and).rejects("value");
        verify(func1).test("value");
        verify(func2).test("value");
        verify(func3, never()).test("value");
    }

    @Test
    public void shouldHandlePredicateWhenConstructedWithASingleSelection() {
        // Given
        final And and = new And<>(
                new Exists(),
                new IsA(String.class),
                new IsEqual("test")
        );

        // When / Then
        assertThat(and).accepts("test");
    }

    @Test
    public void shouldHandlePredicateWhenBuiltAndWithASingleSelection() {
        // Given
        final And and = new And.Builder<String>()
                .select(0)
                .execute(new Exists())
                .select(0)
                .execute(new IsA(String.class))
                .select(0)
                .execute(new IsEqual("test"))
                .build();

        // When / Then
        assertThat(and)
                .accepts("test")
                .accepts(new ArrayTuple("test"));
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
        assertThat(deserialisedFilter).isNotNull();
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseComplex() throws IOException {
        // Given
        final And predicate = new And.Builder()
                .select(0)
                .execute(new IsMoreThan(1))
                .select(1)
                .execute(new IsLessThan(10.0))
                .build();

        // When
        final String json = JsonSerialiser.serialise(predicate);

        // Then
        JsonSerialiser.assertEquals("{" +
                "\"class\":\"uk.gov.gchq.koryphe.impl.predicate.And\"," +
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
        final And deserialisedFilter = JsonSerialiser.deserialise(json, And.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
    }

    @Test
    public void shouldCheckInputClass() {
        // When
        And<?> predicate = new And<>(new IsMoreThan(1), new IsLessThan(10));
        // Then
        InputValidatorAssert.assertThat(predicate)
                .acceptsInput(Integer.class)
                .rejectsInput(Double.class)
                .rejectsInput(Integer.class, Integer.class);

        // When
        predicate = new And<>(new IsMoreThan(1.0), new IsLessThan(10.0));
        // Then
        InputValidatorAssert.assertThat(predicate)
                .acceptsInput(Double.class)
                .rejectsInput(Integer.class);

        // When
        predicate = new And<>(new IsMoreThan(1), new IsLessThan(10.0));
        // Then
        InputValidatorAssert.assertThat(predicate)
                .rejectsInput(Integer.class)
                .rejectsInput(Integer.class, Double.class);

        // When
        predicate = new And<>(
                new IntegerTupleAdaptedPredicate(new IsMoreThan(1), 0),
                new IntegerTupleAdaptedPredicate(new IsLessThan(10.0), 1)
        );
        // Then
        InputValidatorAssert.assertThat(predicate)
                .acceptsInput(Integer.class, Double.class)
                .rejectsInput(Integer.class)
                .rejectsInput(Double.class, Integer.class);

        // When
        predicate = new And.Builder()
                .select(0)
                .execute(new IsMoreThan(1))
                .select(1)
                .execute(new IsLessThan(10.0))
                .build();
        // Then
        InputValidatorAssert.assertThat(predicate)
                .acceptsInput(Integer.class, Double.class)
                .rejectsInput(Integer.class)
                .rejectsInput(Double.class, Integer.class);
    }

    @Override
    protected And getInstance() {
        return new And();
    }

    @Override
    protected Iterable<And> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new And(new Exists(), new IsMoreThan(10L)),
                new And(new Exists())
        );
    }
}
