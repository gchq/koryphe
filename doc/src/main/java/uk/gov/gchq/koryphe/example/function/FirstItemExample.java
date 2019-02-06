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
package uk.gov.gchq.koryphe.example.function;

import uk.gov.gchq.koryphe.example.KorypheFunctionExample;
import uk.gov.gchq.koryphe.impl.function.FirstItem;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class FirstItemExample extends KorypheFunctionExample<Iterable<Integer>, Integer> {
    @Override
    public Function<Iterable<Integer>, Integer> getFunction() {
        return new FirstItem<>();
    }

    @Override
    public Stream<Iterable<Integer>> getInput() {
        final List<Integer> first = Arrays.asList(2, 3, 5, 7);
        final List<Integer> second = Arrays.asList(11, 13, 17, 19);
        final List<Integer> third = Arrays.asList(23, 29, 31, 37);

        return Stream.of(first, second, third);
    }
}
