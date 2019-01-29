/*
 * Copyright 2017-2019 Crown Copyright
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
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Example(name = "Single input predicate",
         description = "Applies a single input predicate to a stream of input values, producing a stream of " +
         "those input values that pass the test.")
public class IsMoreThanExample extends KoryphePredicateExample<Comparable> {
    @Override
    public Stream<Comparable> getInput() {
        return Arrays.asList((Comparable) 2, (Comparable) 4, (Comparable) 6).stream();
    }

    @Override
    public Predicate<Comparable> getPredicate() {
        return new IsMoreThan(3);
    }
}
