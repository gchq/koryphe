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
package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.impl.predicate.IsLessThan;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.ReferenceArrayTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.function.KorypheFunction2;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static uk.gov.gchq.koryphe.util.Util.project;
import static uk.gov.gchq.koryphe.util.Util.select;

public class IfTest extends FunctionTest<If> {

    @Override
    protected If<Object, Object> getInstance() {
        return new If<>()
                .condition(true)
                .then(new SetValue("value1"))
                .otherwise(new SetValue("value2"));
    }

    @Override
    protected Iterable<If> getDifferentInstances() {
        return Arrays.asList(
                new If<>()
                        .condition(false)
                        .then(new SetValue("value1"))
                        .otherwise(new SetValue("value2")),
                new If<>()
                        .condition(true)
                        .then(new SetValue("differentThenValue"))
                        .otherwise(new SetValue("value2")),
                new If<>()
                        .condition(true)
                        .then(new SetValue("value1"))
                        .otherwise(new SetValue("differentOtherwiseValue")),
                getAltInstance()
        );
    }

    private If<Comparable, Comparable> getAltInstance() {
        return new If<Comparable, Comparable>()
                .predicate(new IsA(Integer.class))
                .then(new SetValue("value2"))
                .otherwise(new SetValue("value3"));
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return If.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Object.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final If ifPredicate = getInstance();

        // When
        final String json = JsonSerialiser.serialise(ifPredicate);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.If\",%n" +
                "  \"condition\" : true,%n" +
                "  \"then\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.function.SetValue\",%n" +
                "    \"value\" : \"value1\"%n" +
                "  },%n" +
                "  \"otherwise\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.function.SetValue\",%n" +
                "    \"value\" : \"value2\"%n" +
                "  }%n" +
                "}"), json);

        // When 2
        final If deserialised = JsonSerialiser.deserialise(json, If.class);

        // Then 2
        assertNotNull(deserialised);
        assertTrue(deserialised.getCondition());
        assertEquals("value1", ((SetValue) deserialised.getThen()).getValue());
        assertEquals("value2", ((SetValue) deserialised.getOtherwise()).getValue());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseAlternative() throws IOException {
        // Given
        final If ifAltPredicate = getAltInstance();

        // When
        final String json = JsonSerialiser.serialise(ifAltPredicate);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.If\",%n" +
                "  \"predicate\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.Integer\"%n" +
                "  },%n" +
                "  \"then\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.function.SetValue\",%n" +
                "    \"value\" : \"value2\"%n" +
                "  },%n" +
                "  \"otherwise\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.function.SetValue\",%n" +
                "    \"value\" : \"value3\"%n" +
                "  }%n" +
                "}"), json);

        // When 2
        final If deserialised = JsonSerialiser.deserialise(json, If.class);

        // Then 2
        assertNotNull(deserialised);
        assertEquals(Integer.class.getName(), ((IsA) deserialised.getPredicate()).getType());
        assertEquals("value2", ((SetValue) deserialised.getThen()).getValue());
        assertEquals("value3", ((SetValue) deserialised.getOtherwise()).getValue());
    }

    @Test
    public void shouldReturnInputWithNullFunctions() {
        // Given
        final Object input = "testValue";
        final If<Object, Object> function = new If<>();

        // When
        final Object result = function.apply(input);

        // Then
        assertEquals(input, result);
    }

    @Test
    public void shouldApplyPredicateAndCallThen() {
        // Given
        final Object input = mock(Object.class);
        final Predicate<Object> predicate = mock(Predicate.class);
        final Function<Object, Object> then = mock(Function.class);
        final Function<Object, Object> otherwise = mock(Function.class);

        final If<Object, Object> function = new If<>().predicate(predicate).then(then).otherwise(otherwise);

        given(predicate.test(input)).willReturn(true);
        given(then.apply(input)).willReturn(0);

        // When
        final Object result = function.apply(input);

        // Then
        assertEquals(0, result);
        verify(predicate).test(input);
        verify(then).apply(input);
        verify(otherwise, never()).apply(input);
    }

    @Test
    public void shouldApplyPredicateAndCallOtherwise() {
        // Given
        final Object input = mock(Object.class);
        final Predicate<Object> predicate = mock(Predicate.class);
        final Function<Object, Object> then = mock(Function.class);
        final Function<Object, Object> otherwise = mock(Function.class);

        final If<Object, Object> function = new If<>().predicate(predicate).then(then).otherwise(otherwise);

        given(predicate.test(input)).willReturn(false);
        given(otherwise.apply(input)).willReturn(0);

        // When
        final Object result = function.apply(input);

        // Then
        assertEquals(0, result);
        verify(predicate).test(input);
        verify(then, never()).apply(input);
        verify(otherwise).apply(input);
    }

    @Test
    public void shouldDelegateNullInputToFunctions() {
        // Given
        final Comparable input = null;
        final Predicate<Comparable> predicate = mock(Predicate.class);
        final Function<Comparable, Comparable> then = mock(Function.class);
        final Function<Comparable, Comparable> otherwise = mock(Function.class);
        final If<Comparable, Comparable> function = new If<Comparable, Comparable>()
                .predicate(predicate)
                .then(then)
                .otherwise(otherwise);

        // When
        final Object result = function.apply(input);

        // Then
        assertEquals(input, result);
        verify(predicate).test(input);
        verify(then, never()).apply(input);
        verify(otherwise).apply(input);
    }

    @Test
    public void shouldReturnInputForNullConditionalPredicate() {
        // Given
        final Object input = "testValue";
        final Predicate<Object> predicate = null;
        final Function<Object, Object> then = mock(Function.class);

        final If<Object, Object> function = new If<>().predicate(predicate).then(then);

        // When
        final Object result = function.apply(input);

        // Then
        assertEquals(input, result);
        verify(then, never()).apply(input);
    }

    @Test
    public void shouldReturnInputForNullThenOrOtherwise() {
        // Given
        final Object input = "testValue";
        final Predicate<Object> predicate = mock(Predicate.class);
        final Function<Object, Object> then = null;
        final Function<Object, Object> otherwise = null;

        final If<Object, Object> function = new If<>().predicate(predicate).then(then).otherwise(otherwise);

        given(predicate.test(input)).willReturn(true);

        // When
        final Object result = function.apply(input);

        // Then
        assertEquals(input, result);
        verify(predicate).test(input);
    }

    @Test
    public void shouldApplyTuplePredicateAndFunction() {
        // Given
        final Integer firstVal = 6;
        final Integer secondVal = 2;
        final Integer thirdVal = 4;
        final ArrayTuple input = new ArrayTuple(firstVal, secondVal, thirdVal);
        final Predicate<Integer> predicate = mock(Predicate.class);
        final Function<Integer, Integer> then = mock(Function.class);
        final Function<Integer, Integer> otherwise = mock(Function.class);

        final If<Tuple<Integer>, Tuple<Integer>> function = new If<Tuple<Integer>, Tuple<Integer>>()
                .predicate(0, predicate)
                .then(1, then)
                .otherwise(2, otherwise);

        given(predicate.test(firstVal)).willReturn(true);
        given(then.apply(secondVal)).willReturn(1);

        // When
        final Object result = function.apply(input);

        // Then
        final ArrayTuple expectedResult = new ArrayTuple(firstVal, 1, thirdVal);
        assertEquals(expectedResult, result);
        verify(predicate).test(firstVal);
        verify(then).apply(secondVal);
    }

    @Test
    public void shouldApplyFunctionsToMultipleSelections() {
        // Given
        final Integer firstInput = 2;
        final Integer secondInput = 7;
        final Integer thirdInput = 1;
        final ArrayTuple input = new ArrayTuple(firstInput, secondInput, thirdInput);
        final ReferenceArrayTuple<Integer> refTuple = new ReferenceArrayTuple<>(input, new Integer[]{1, 2});
        final Predicate<Integer> predicate = mock(Predicate.class);
        final KorypheFunction2<Integer, Integer, Integer> then = mock(KorypheFunction2.class);
        final KorypheFunction2<Integer, Integer, Integer> otherwise = mock(KorypheFunction2.class);
        final If<Tuple<Integer>, Tuple<Integer>> function = new If<Tuple<Integer>, Tuple<Integer>>()
                .predicate(0, predicate)
                .then(select(1, 2), then, project(1))
                .otherwise(select(1, 2), otherwise, project(1));

        given(predicate.test(firstInput)).willReturn(true);
        given(then.apply(refTuple)).willReturn(1);

        // When
        final Object result = function.apply(input);

        // Then
        final ArrayTuple expectedResult = new ArrayTuple(firstInput, 1, thirdInput);
        assertEquals(expectedResult, result);
        verify(predicate).test(firstInput);
        verify(then).apply(refTuple);
        verify(otherwise, never()).apply(refTuple);
    }

    @Test
    public void shouldUseCorrectInputOnEachUse() {
        // Given
        If<Integer, Integer> function = new If<Integer, Integer>()
                .predicate(new IsLessThan(2))
                .then(new SetValue(1))
                .otherwise(new SetValue(2));

        // When / Then
        assertEquals(new Integer(1), function.apply(1));
        assertEquals(new Integer(2), function.apply(3));
    }
}
