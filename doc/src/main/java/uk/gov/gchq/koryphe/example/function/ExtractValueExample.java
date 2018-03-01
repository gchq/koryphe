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
import uk.gov.gchq.koryphe.impl.function.ExtractValue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ExtractValueExample extends KorypheFunctionExample<Map<String, Integer>, Integer> {
    @Override
    public Function<Map<String, Integer>, Integer> getFunction() {
        return new ExtractValue<>("second");
    }

    @Override
    public Stream<Map<String, Integer>> getInput() {
        final Map<String, Integer> first = new HashMap<>();
        first.put("first", 1);
        first.put("second", 2);
        first.put("third", 3);

        final Map<String, Integer> second = new HashMap<>();
        second.put("second", 10);
        second.put("first", 20);
        second.put("third", 30);

        return Stream.of(first, second);
    }
}
