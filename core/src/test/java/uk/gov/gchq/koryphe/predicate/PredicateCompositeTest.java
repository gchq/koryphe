/*
 * Copyright 2020-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.predicate;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.predicate.Exists;
import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.impl.predicate.IsFalse;
import uk.gov.gchq.koryphe.impl.predicate.IsLessThan;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.tuple.n.Tuple1;
import uk.gov.gchq.koryphe.tuple.predicate.TupleAdaptedPredicate;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class PredicateCompositeTest extends PredicateTest<PredicateComposite> {
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        PredicateComposite predicateComposite = getInstance();
        String json =
                "{" +
                    "\"predicates\": [" +
                        "{" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.predicate.Exists\"" +
                        "}," +
                        "{" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.predicate.IsA\"," +
                            "\"type\": \"java.lang.Long\"" +
                        "}" +
                    "]" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(predicateComposite);
        PredicateComposite deserialised = JsonSerialiser.deserialise(json, PredicateComposite.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(predicateComposite, deserialised);

    }

    @Override
    protected PredicateComposite getInstance() {
        return new PredicateComposite(Arrays.asList(
                new Exists(),
                new IsA(Long.class)
        ));
    }

    @Override
    protected Iterable<PredicateComposite> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new PredicateComposite(Arrays.asList(
                        new Exists(),
                        new IsA(Integer.class))),
                new PredicateComposite(),
                new PredicateComposite(Arrays.asList(
                        new IsA(Long.class),
                        new IsMoreThan(5)))
        );
    }

    @Test
    public void shouldPassInputToTupleAdaptedPredicateIfAutomaticallyUnpacked() {
        // Given
        Integer unpackedInput = 5;
        Tuple1<Integer> input = new Tuple1<>(unpackedInput);

        // When
        PredicateComposite predicateComposite = new PredicateComposite<>(
                Arrays.asList(new TupleAdaptedPredicate<>(new IsLessThan(10), new Integer[]{0}))
        );

        boolean unpackedTest = predicateComposite.test(unpackedInput);
        boolean test = predicateComposite.test(input);

        // Then
        assertTrue(unpackedTest);
        assertTrue(test);
    }

    @Test
    public void shouldThrowExceptionIfInputClassesDontMatchInputAndPredicateIsNotTupleAdapted() {
        // Given
        String input = "test";

        // When
        PredicateComposite predicateComposite = new PredicateComposite(Arrays.asList(new IsA(String.class), new IsFalse()));

        // Then
        try {
            predicateComposite.test(input);
            fail("Expected an exception");
        } catch (final ClassCastException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void shouldFailIfOneOfThePredicatesDontPass() {
        // Given
        Long notAnInteger = 3L;

        // When
        PredicateComposite predicateComposite = new PredicateComposite(Arrays.asList(
                new Exists(),
                new IsA(Integer.class),
                new IsLessThan(10)));

        boolean result = predicateComposite.test(notAnInteger);

        // Then
        assertFalse(result);
    }

    @Test
    public void shouldPassIfAllThePredicatesPass() {
        // Given
        Integer input = 3;

        // When
        PredicateComposite predicateComposite = new PredicateComposite(Arrays.asList(
                new Exists(),
                new IsA(Integer.class),
                new IsLessThan(10)));

        boolean result = predicateComposite.test(input);

        // Then
        assertTrue(result);
    }
}