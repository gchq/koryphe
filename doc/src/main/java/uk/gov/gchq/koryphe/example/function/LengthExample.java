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
import uk.gov.gchq.koryphe.impl.function.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class LengthExample extends KorypheFunctionExample<Object, Integer> {
    @Override
    public Function<Object, Integer> getFunction() {
        return new Length();
    }

    @Override
    public Stream<Object> getInput() {
        final List<Object> input = new ArrayList<>();
        input.add("testString");
        input.add(new Object[5]);
        return Stream.of(input);
    }
}
