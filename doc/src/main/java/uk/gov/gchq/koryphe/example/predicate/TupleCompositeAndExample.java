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

package uk.gov.gchq.koryphe.example.predicate;

import uk.gov.gchq.koryphe.example.KoryphePredicateExample;
import uk.gov.gchq.koryphe.example.annotation.Example;
import uk.gov.gchq.koryphe.impl.predicate.IsLessThan;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.predicate.TupleAdaptedPredicateComposite;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Example(name = "Complex object composite predicate",
        description = "Applies a composite of adapted predicates to multiple fields in a stream of input tuples, " +
                "producing a stream of outputs containing those tuples that pass all of the predicate tests.")
public class TupleCompositeAndExample extends KoryphePredicateExample<Tuple<String>> {
    @Override
    public Stream<Tuple<String>> getInput() {
        List<Tuple<String>> inputTuples = Arrays.asList(
                createMapTuple(1, 2, 3),
                createMapTuple(4, 5, 6),
                createMapTuple(7, 8, 9));
        return inputTuples.stream();
    }

    @Override
    public Predicate<Tuple<String>> getPredicate() {
        return new TupleAdaptedPredicateComposite.Builder<String>()
                .select(new String[]{"A"}).execute(new IsMoreThan(2))
                .select(new String[]{"C"}).execute(new IsLessThan(8))
                .build();
    }
}
