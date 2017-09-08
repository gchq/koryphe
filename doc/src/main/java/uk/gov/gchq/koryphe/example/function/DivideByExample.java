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
import uk.gov.gchq.koryphe.impl.function.DivideBy;
import uk.gov.gchq.koryphe.tuple.n.Tuple2;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This example demonstrates a function that returns more than one result value. It uses the <code>DivideBy</code>
 * function that accepts a single <code>Integer</code> input, using a control value to produce the division
 * result and modulus.
 *
 * The input values <code>{3, 6, 10}</code> with a control value of <code>3</code> produces the output
 * <code>{{1, 0}, {2, 0}, {3, 1}}</code>.
 */
@Example(name = "Single input, multiple output function",
         description = "Applies a single input, multiple output function to a stream of single value inputs, producing " +
         "a stream of multiple value outputs.")
public class DivideByExample extends KorypheFunctionExample<Integer, Tuple2<Integer, Integer>> {
    @Override
    public Stream<Integer> getInput() {
        return Arrays.asList(3, 6, 10).stream();
    }

    @Override
    public Function<Integer, Tuple2<Integer, Integer>> getFunction() {
        return new DivideBy(3);
    }
}
