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
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.function.TupleAdaptedFunction;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Example(name = "Complex object single input, single output function",
        description = "Applies an adapted single input, single output function to a field selected from a stream of " +
                "input tuples, producing a stream of output tuples containing the result of the function.")
public class TupleMultiplyByExample extends KorypheFunctionExample<Tuple<String>, Tuple<String>> {
    @Override
    public Stream<Tuple<String>> getInput() {
        List<Tuple<String>> inputTuples = Arrays.asList(
                createMapTuple(1, 2, 3),
                createMapTuple(4, 5, 6),
                createMapTuple(7, 8, 9));
        return inputTuples.stream();
    }

    @Override
    public Function<Tuple<String>, Tuple<String>> getFunction() {
        TupleAdaptedFunction<String, Integer, Integer> tupleMultiplyBy = new TupleAdaptedFunction<>();
        tupleMultiplyBy.setFunction(new MultiplyBy(2));
        tupleMultiplyBy.setSelection(new String[]{"A"});
        tupleMultiplyBy.setProjection(new String[]{"D"});
        return tupleMultiplyBy;
    }
}
