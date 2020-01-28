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

package uk.gov.gchq.koryphe.example;

import uk.gov.gchq.koryphe.example.util.JsonSerialiser;

import java.util.function.Predicate;

public abstract class KoryphePredicateExample<I> extends KorypheExample<I, I> {
    public abstract Predicate<I> getPredicate();

    @Override
    protected void executeExample() throws Exception {
        System.out.println("Predicate json: ");
        System.out.println(JsonSerialiser.serialise(getPredicate()));
        System.out.println();
        System.out.println("Predicate inputs: ");
        getInput().forEach(this::printInput);
        System.out.println();
        System.out.println("Predicate outputs: ");
        getInput().filter(getPredicate()).forEach(this::printOutput);
        System.out.println();
    }
}
