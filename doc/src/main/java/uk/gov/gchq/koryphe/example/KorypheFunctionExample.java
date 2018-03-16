/*
 * Copyright 2017-2018 Crown Copyright
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

package uk.gov.gchq.koryphe.example;

import uk.gov.gchq.koryphe.example.util.JsonSerialiser;

import java.util.function.Function;

public abstract class KorypheFunctionExample<I, O> extends KorypheExample<I, O> {
    public abstract Function<I, O> getFunction();

    @Override
    protected void executeExample() throws Exception {
        System.out.println("Function json: ");
        System.out.println(JsonSerialiser.serialise(getFunction()));
        System.out.println();
        System.out.println("Function inputs: ");
        getInput().forEach(this::printInput);
        System.out.println();
        System.out.println("Function outputs: ");
        getInput().map(getFunction()).forEach(this::printOutput);
        System.out.println();
    }
}
