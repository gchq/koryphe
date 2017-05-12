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
import uk.gov.gchq.koryphe.impl.function.MultiplyBy;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.function.TupleAdaptedFunctionComposite;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This example shows a composite function that operates on a complex input object. It uses a
 * <code>TupleAdaptedFunctionComposite</code> containing 2 <code>MultiplyBy</code> functions, one that creates
 * the field 'E' by multiplying the field 'A' by <code>2</code> and another that creates the field 'D' by
 * multiplying the field 'B' by <code>3</code>.
 *
 * The input values <code>{{A:1, B:2, C:3}, {A:4, B:5, C:6}, {A:7, B:8, C:9}}</code> produces the result
 * <code>{{A:1, B:2, C:3, D:6, E:2}, {A:4, B:5, C:6, D:15, E:8}, {A:7, B:8, C:9, D:24, E:14}}</code>.
 */
@Example(name = "Complex object composite function",
         description = "Applies a composite of adapted functions to multiple fields in a stream of input tuples, " +
                 "producing a stream of output tuples containing the results of the functions.")
public class TupleCompositeMultiplyByExample extends KorypheFunctionExample<Tuple<String>, Tuple<String>> {
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
        return new TupleAdaptedFunctionComposite.Builder<String>()
                .select("A").execute(new MultiplyBy(2)).project("E")
                .select("B").execute(new MultiplyBy(3)).project("D")
                .build();
    }
}
