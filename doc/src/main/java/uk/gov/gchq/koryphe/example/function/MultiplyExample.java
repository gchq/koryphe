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

package uk.gov.gchq.koryphe.example.function;

import uk.gov.gchq.koryphe.example.KorypheFunctionExample;
import uk.gov.gchq.koryphe.example.annotation.Example;
import uk.gov.gchq.koryphe.impl.function.Multiply;
import uk.gov.gchq.koryphe.tuple.n.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This example demonstrates a function that accepts more than one input value and returns a single result value.
 * It uses the <code>Multiply</code> function that accepts 2 <code>Integer</code> inputs, multiplying the first by the
 * second to produce the result.
 * <p>
 * The input values <code>{{1, 1}, {1, 2}, {2, 2}}</code> produces the output <code>{1, 2, 4}</code>.
 */
@Example(name = "Multiple input, single output function",
        description = "Applies a multiple input, single output function to a stream of multiple value inputs, " +
                "producing a stream of single value outputs.")
public class MultiplyExample extends KorypheFunctionExample<Tuple2<Integer, Integer>, Integer> {
    @Override
    public Stream<Tuple2<Integer, Integer>> getInput() {
        List<Tuple2<Integer, Integer>> inputTuples = Arrays.asList(
                new Tuple2<>(1, 1),
                new Tuple2<>(1, 2),
                new Tuple2<>(2, 2));
        return inputTuples.stream();
    }

    @Override
    public Function<Tuple2<Integer, Integer>, Integer> getFunction() {
        return new Multiply();
    }
}
