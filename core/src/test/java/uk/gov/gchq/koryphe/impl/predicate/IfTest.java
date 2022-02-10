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
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.ReferenceArrayTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.predicate.KoryphePredicate2;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class IfTest extends PredicateTest<If> {

    @Override
    protected If<Object> getInstance() {
        return new If<>(true, new IsA(String.class), new IsA(Integer.class));
    }

    @Override
    protected Iterable<If> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new If<>(false, new IsA(String.class), new IsA(Integer.class)),
                new If<>(true, new IsMoreThan(5L), new IsA(Integer.class)),
                new If<>(true, new IsA(String.class), new IsA(Long.class)),
                new If<>()
        );
    }

    private If<Comparable> getAltInstance() {
        return new If<>(new IsA(Integer.class), new IsLessThan(3), new IsA(String.class));
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
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.If\",%n" +
                "  \"condition\" : true,%n" +
                "  \"then\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.String\"%n" +
                "  },%n" +
                "  \"otherwise\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.Integer\"%n" +
                "  }%n" +
                "}"), json);

        // When 2
        final If deserialised = JsonSerialiser.deserialise(json, If.class);

        // Then 2
        assertNotNull(deserialised);
        assertTrue(deserialised.getCondition());
        assertEquals(String.class.getName(), ((IsA) deserialised.getThen()).getType());
        assertEquals(Integer.class.getName(), ((IsA) deserialised.getOtherwise()).getType());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseAlternative() throws IOException {
        // Given
        final If ifAltPredicate = getAltInstance();

        // When
        final String json = JsonSerialiser.serialise(ifAltPredicate);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.If\",%n" +
                "  \"predicate\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.Integer\"%n" +
                "  },%n" +
                "  \"then\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsLessThan\",%n" +
                "    \"value\" : 3,%n" +
                "    \"orEqualTo\" : false%n" +
                "  },%n" +
                "  \"otherwise\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.String\"%n" +
                "  }%n" +
                "}"), json);

        // When 2
        final If deserialised = JsonSerialiser.deserialise(json, If.class);

        // Then 2
        assertNotNull(deserialised);
        assertEquals(Integer.class.getName(), ((IsA) deserialised.getPredicate()).getType());
        assertEquals(3, ((IsLessThan) deserialised.getThen()).getControlValue());
        assertEquals(String.class.getName(), ((IsA) deserialised.getOtherwise()).getType());
    }

    @Test
    public void shouldReturnTrueWithSuccessfulThen() {
        // Given
        final Object input = "testValue";
        final Predicate then = mock(Predicate.class);
        final Predicate otherwise = mock(Predicate.class);

        final If<Object> filter = new If<>(true, then, otherwise);

        given(then.test(input)).willReturn(true);

        // When
        final boolean accepted = filter.test(input);

        // Then
        assertTrue(accepted);
        verify(otherwise, never()).test(input);
    }

    @Test
    public void shouldReturnFalseFromFailedOtherwise() {
        // Given
        final Object input = 6;
        final Predicate then = mock(Predicate.class);
        final Predicate otherwise = mock(Predicate.class);

        final If<Object> filter = new If<>(false, then, otherwise);

        given(otherwise.test(input)).willReturn(false);

        // When
        final boolean denied = filter.test(input);

        // Then
        assertFalse(denied);
        verify(then, never()).test(input);
    }

    @Test
    public void shouldRejectValueWithNullFunctions() {
        // Given
        final Object input = "testValue";
        final If<Object> filter = new If<>();

        // When
        final boolean denied = filter.test(input);

        // Then
        assertFalse(denied);
    }

    @Test
    public void shouldApplyPredicateAndPassBothConditions() {
        // Given
        final Object input = mock(Object.class);
        final Predicate predicate = mock(Predicate.class);
        final Predicate then = mock(Predicate.class);
        final Predicate otherwise = mock(Predicate.class);

        final If<Object> filter = new If<>(predicate, then, otherwise);

        given(predicate.test(input)).willReturn(true);
        given(then.test(input)).willReturn(true);

        // When
        final boolean result = filter.test(input);

        // Then
        assertTrue(result);
        verify(predicate).test(input);
        verify(then).test(input);
        verify(otherwise, never()).test(input);
    }

    @Test
    public void shouldApplyPredicateAndPassSecondCondition() {
        // Given
        final Object input = mock(Object.class);
        final Predicate predicate = mock(Predicate.class);
        final Predicate then = mock(Predicate.class);
        final Predicate otherwise = mock(Predicate.class);

        final If<Object> filter = new If<>(predicate, then, otherwise);

        given(predicate.test(input)).willReturn(false);
        given(otherwise.test(input)).willReturn(true);

        // When
        final boolean result = filter.test(input);

        // Then
        assertTrue(result);
        verify(predicate).test(input);
        verify(then, never()).test(input);
        verify(otherwise).test(input);
    }

    @Test
    public void shouldApplyPredicateAndPassFirstButNotSecondCondition() {
        // Given
        final Object input = mock(Object.class);
        final Predicate predicate = mock(Predicate.class);
        final Predicate then = mock(Predicate.class);
        final Predicate otherwise = mock(Predicate.class);

        final If<Object> filter = new If<>(predicate, then, otherwise);

        given(predicate.test(input)).willReturn(true);
        given(then.test(input)).willReturn(false);

        // When
        final boolean result = filter.test(input);

        // Then
        assertFalse(result);
        verify(predicate).test(input);
        verify(then).test(input);
        verify(otherwise, never()).test(input);
    }

    @Test
    public void shouldApplyPredicateButFailBothConditions() {
        // Given
        final Object input = mock(Object.class);
        final Predicate predicate = mock(Predicate.class);
        final Predicate then = mock(Predicate.class);
        final Predicate otherwise = mock(Predicate.class);

        final If<Object> filter = new If<>(predicate, then, otherwise);

        given(predicate.test(input)).willReturn(false);
        given(otherwise.test(input)).willReturn(false);

        // When
        final boolean result = filter.test(input);

        // Then
        assertFalse(result);
        verify(predicate).test(input);
        verify(then, never()).test(input);
        verify(otherwise).test(input);
    }

    @Test
    public void shouldDelegateNullInputToPredicates() {
        // Given
        final Comparable input = null;
        final Predicate predicate = mock(Predicate.class);
        final Predicate then = mock(Predicate.class);
        final Predicate otherwise = mock(Predicate.class);
        final If<Comparable> filter = new If<>(predicate, then, otherwise);

        // When
        final boolean result = filter.test(input);

        // Then
        assertFalse(result);
        verify(predicate).test(input);
        verify(then, never()).test(input);
        verify(otherwise).test(input);
    }

    @Test
    public void shouldReturnFalseForNullConditionalPredicate() {
        // Given
        final Object input = "testValue";
        final Predicate predicate = null;
        final Predicate then = mock(Predicate.class);

        final If<Object> filter = new If<>(predicate, then);

        // When
        final boolean result = filter.test(input);

        // Then
        assertFalse(result);
        verify(then, never()).test(input);
    }

    @Test
    public void shouldReturnFalseForNullThenOrOtherwise() {
        // Given
        final Object input = "testValue";
        final Predicate predicate = mock(Predicate.class);
        final Predicate then = null;
        final Predicate otherwise = null;

        final If<Object> filter = new If<>(predicate, then, otherwise);

        given(predicate.test(input)).willReturn(true);

        // When
        final boolean result = filter.test(input);

        // Then
        assertFalse(result);
        verify(predicate).test(input);
    }

    @Test
    public void shouldApplyPredicatesToSelectionWithIfBuilder() {
        // Given
        final Integer firstVal = 6;
        final Integer secondVal = 2;
        final Integer thirdVal = 4;
        final ArrayTuple input = new ArrayTuple(firstVal, secondVal, thirdVal);
        final Predicate predicate = mock(Predicate.class);
        final Predicate then = mock(Predicate.class);
        final Predicate otherwise = mock(Predicate.class);

        final If<Tuple<Integer>> filter = new If.SelectedBuilder()
                .predicate(predicate, 0)
                .then(then, 1)
                .otherwise(otherwise, 2)
                .build();

        given(predicate.test(firstVal)).willReturn(true);
        given(then.test(secondVal)).willReturn(true);

        // When
        final boolean result = filter.test(input);

        // Then
        assertTrue(result);
        verify(predicate).test(firstVal);
        verify(then).test(secondVal);
    }

    @Test
    public void shouldApplyPredicatesToMultipleSelectionsWithIfBuilder() {
        // Given
        final Integer firstInput = 2;
        final Integer secondInput = 7;
        final Integer thirdInput = 1;
        final ArrayTuple input = new ArrayTuple(firstInput, secondInput, thirdInput);
        final ReferenceArrayTuple<Integer> refTuple = new ReferenceArrayTuple<>(input, new Integer[]{1, 2});
        final Predicate predicate = mock(Predicate.class);
        final KoryphePredicate2 then = mock(KoryphePredicate2.class);
        final KoryphePredicate2 otherwise = mock(KoryphePredicate2.class);
        final If<Tuple<Integer>> filter = new If.SelectedBuilder()
                .predicate(predicate, 0)
                .then(then, 1, 2)
                .otherwise(otherwise, 1, 2)
                .build();

        given(predicate.test(firstInput)).willReturn(true);
        given(then.test(refTuple)).willReturn(true);

        // When
        final boolean result = filter.test(input);

        // Then
        assertTrue(result);
        verify(predicate).test(firstInput);
        verify(then).test(refTuple);
        verify(otherwise, never()).test(refTuple);
    }

    @Test
    public void shouldUseCorrectInputOnEachUse() {
        // Given
        If filter = new If<>(new IsLessThan(2), new IsLessThan(0), new IsLessThan(4));

        // When / Then
        assertFalse(filter.test(1));
        assertTrue(filter.test(-1));
        assertTrue(filter.test(3));
    }
}
