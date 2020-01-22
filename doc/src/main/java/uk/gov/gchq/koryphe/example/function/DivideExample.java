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

package uk.gov.gchq.koryphe.example.function;

import uk.gov.gchq.koryphe.example.KorypheFunctionExample;
import uk.gov.gchq.koryphe.example.annotation.Example;
import uk.gov.gchq.koryphe.impl.function.Divide;
import uk.gov.gchq.koryphe.tuple.n.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This example demonstrates a function that accepts more than one input value and returns more than one result value.
 * It uses the <code>Divide</code> function that accepts 2 <code>Integer</code> inputs, dividing the first by the second
 * and produces the division result and modulus.
 * <p>
 * The input values <code>{{8, 1}, {8, 2}, {8, 3}}</code> produces the output <code>{{8, 0}, {4, 0}, {2, 2}}</code>.
 */
@Example(name = "Multiple input, multiple output function",
        description = "Applies a multiple input, multiple output function to a stream of multiple value inputs, " +
                "producing a stream of multiple value outputs.")
public class DivideExample extends KorypheFunctionExample<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> {
    @Override
    public Stream<Tuple2<Integer, Integer>> getInput() {
        List<Tuple2<Integer, Integer>> inputTuples = Arrays.asList(
                new Tuple2<>(8, 1),
                new Tuple2<>(8, 2),
                new Tuple2<>(8, 3));
        return inputTuples.stream();
    }

    @Override
    public Function<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> getFunction() {
        return new Divide();
    }
}
