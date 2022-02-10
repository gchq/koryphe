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

package uk.gov.gchq.koryphe.tuple.predicate;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.impl.predicate.IsEqual;
import uk.gov.gchq.koryphe.impl.predicate.IsLessThan;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TupleAdaptedPredicateCompositeTest extends PredicateTest<TupleAdaptedPredicateComposite> {

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        TupleAdaptedPredicateComposite instance = getInstance();

        String json = "" +
                "{" +
                "   \"predicates\": [" +
                "       {" +
                "           \"selection\": [ \"test\" ]," +
                "           \"predicate\": {" +
                "               \"class\": \"uk.gov.gchq.koryphe.impl.predicate.IsMoreThan\"," +
                "               \"value\": {" +
                "                   \"java.lang.Long\": 10" +
                "               }," +
                "               \"orEqualTo\": false" +
                "           }" +
                "       }," +
                "       {" +
                "           \"selection\": [ \"differentTest\" ]," +
                "           \"predicate\": {" +
                "               \"class\": \"uk.gov.gchq.koryphe.impl.predicate.IsA\"," +
                "               \"type\": \"java.lang.String\"" +
                "           }" +
                "       }" +
                "   ]" +
                "}";
        // When
        String serialised = JsonSerialiser.serialise(instance);
        TupleAdaptedPredicateComposite deserialised = JsonSerialiser.deserialise(json, TupleAdaptedPredicateComposite.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(instance, deserialised);
    }

    @Override
    protected TupleAdaptedPredicateComposite getInstance() {
        return new TupleAdaptedPredicateComposite.Builder<String>()
                .select(new String[]{ "test" })
                .execute(new IsMoreThan(10L))
                .select(new String[] { "differentTest"})
                .execute(new IsA(String.class))
                .build();
    }

    @Override
    protected Iterable<TupleAdaptedPredicateComposite> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new TupleAdaptedPredicateComposite.Builder<String>()
                        .select(new String[]{ "test" })
                        .execute(new IsMoreThan(10L))
                        .select(new String[] { "differentTest"})
                        .execute(new IsA(Long.class))
                        .build(),
                new TupleAdaptedPredicateComposite.Builder<String>()
                        .select(new String[]{ "test" })
                        .execute(new IsMoreThan(10L))
                        .select(new String[] { "differentTest"})
                        .execute(new IsLessThan(5))
                        .build(),
                new TupleAdaptedPredicateComposite.Builder<String>()
                    .select(new String[]{ "test" })
                    .execute(new IsMoreThan(10L))
                    .select(new String[] { "differentTest1"})
                    .execute(new IsA(String.class))
                    .build(),
                new TupleAdaptedPredicateComposite.Builder<String>()
                        .select(new String[]{ "test1" })
                        .execute(new IsMoreThan(10L))
                        .select(new String[] { "differentTest"})
                        .execute(new IsA(Long.class))
                        .build(),
                new TupleAdaptedPredicateComposite.Builder<String>()
                        .select(new String[]{ "test" })
                        .execute(new IsMoreThan(5L))
                        .select(new String[] { "differentTest"})
                        .execute(new IsA(Long.class))
                        .build(),
                new TupleAdaptedPredicateComposite.Builder<String>()
                        .select(new String[]{ "test" })
                        .execute(new IsEqual(10L))
                        .select(new String[] { "differentTest"})
                        .execute(new IsA(Long.class))
                        .build(),
                new TupleAdaptedPredicateComposite()
        );
    }

    @Test
    public void shouldFailFast() {
        // Given
        TupleAdaptedPredicateComposite<String> composite = new TupleAdaptedPredicateComposite.Builder<String>()
                .select(new String[]{"fail"})
                .execute(new IsA(String.class))
                .select(new String[]{"error"})
                .execute(new IsMoreThan(10L))
                .build();

        MapTuple<String> objects = new MapTuple<>();
        objects.put("fail", 5L); // not a string will cause IsA check to fail
        objects.put("error", new IsLessThan()); // IsLessThan does not implement Comparable so will error.

        // When
        boolean result = composite.test(objects); // no error

        // Then
        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueIfAllTestsPass() {
        // Given
        TupleAdaptedPredicateComposite instance = getInstance();
        MapTuple<String> objects = new MapTuple<>();

        objects.put("test", 100L);
        objects.put("differentTest", "pass");
        
        // When
        boolean test = instance.test(objects);

        // Then
        assertTrue(test);
    }


}