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
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TupleAdaptedPredicateTest extends PredicateTest<TupleAdaptedPredicate> {

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        TupleAdaptedPredicate predicate = getInstance();
        String json = "" +
                "{" +
                "   \"selection\": [ \"input\" ]," +
                "   \"predicate\": {" +
                "       \"class\": \"uk.gov.gchq.koryphe.impl.predicate.IsA\"," +
                "       \"type\": \"java.lang.String\"" +
                "   }" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(predicate);
        TupleAdaptedPredicate deserialised = JsonSerialiser.deserialise(json, TupleAdaptedPredicate.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(predicate, deserialised);
    }

    @Override
    protected TupleAdaptedPredicate getInstance() {
        return new TupleAdaptedPredicate(new IsA(String.class), new String[] { "input" });
    }

    @Override
    protected Iterable<TupleAdaptedPredicate> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new TupleAdaptedPredicate(new IsA(Long.class), new String[] { "input" }),
                new TupleAdaptedPredicate(new IsMoreThan(5), new String[] { "input" }),
                new TupleAdaptedPredicate(new IsA(String.class), new String[] { "Different" })
        );
    }
}