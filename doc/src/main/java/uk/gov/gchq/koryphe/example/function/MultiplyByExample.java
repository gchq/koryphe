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
import uk.gov.gchq.koryphe.impl.function.MultiplyBy;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This example demonstrates a simple function that has a single input and output. It uses the
 * <code>MultiplyBy</code> function that accepts a single <code>Integer</code> input and multiplies it by a
 * control value to produce the result.
 *
 * The input values <code>{1, 2, 3}</code> with a control value of <code>2</code> produces the output
 * <code>{2, 4, 6}</code>.
 */
@Example(name = "Single input, single output function",
         description = "Applies a single input, single output function to a stream of single value inputs, " +
         "producing a stream of single value outputs.")
public class MultiplyByExample extends KorypheFunctionExample<Integer, Integer> {
    @Override
    public Stream<Integer> getInput() {
        return Arrays.asList(1, 2, 3).stream();
    }

    @Override
    public Function<Integer, Integer> getFunction() {
        return new MultiplyBy(2);
    }
}
