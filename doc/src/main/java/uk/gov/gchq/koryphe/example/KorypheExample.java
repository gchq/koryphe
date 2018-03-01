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

package uk.gov.gchq.koryphe.example;

import com.google.common.collect.Lists;

import uk.gov.gchq.koryphe.example.annotation.Example;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.util.stream.Stream;

public abstract class KorypheExample<I, O> {
    public abstract Stream<I> getInput();

    private String getName() {
        String name = null;
        if (getClass().isAnnotationPresent(Example.class)) {
            Example exampleAnnotation = getClass().getDeclaredAnnotation(Example.class);
            name = exampleAnnotation.name();
        }
        if (name == null) {
            name = this.getClass().getSimpleName();
        }
        return name;
    }

    private String getDescription() {
        if (getClass().isAnnotationPresent(Example.class)) {
            Example exampleAnnotation = getClass().getDeclaredAnnotation(Example.class);
            return exampleAnnotation.description();
        }
        return null;
    }

    public void printInput(final I input) {
        printValue(input);
    }

    public void printOutput(final O output) {
        printValue(output);
    }

    public void printValue(final Object value) {
        if (value instanceof Tuple) {
            printTuple((Tuple) value);
        } else {
            System.out.println(value);
        }
    }

    protected void printTuple(final Tuple tuple) {
        System.out.print("{");
        boolean first = true;
        for (final Object value : tuple) {
            if (!first) {
                System.out.print(",");
            } else {
                first = false;
            }
            System.out.print(value);
        }
        System.out.println("}");
    }

    protected <T> void printIterable(final Iterable<T> iterable) {
        System.out.println(Lists.newArrayList(iterable));
    }

    protected Tuple<String> createMapTuple(final Object valueA, final Object valueB, final Object valueC) {
        Tuple<String> values = new MapTuple<>();
        values.put("A", valueA);
        values.put("B", valueB);
        values.put("C", valueC);
        return values;
    }

    public final void execute() throws Exception {
        System.out.print("---------- Start example: ");
        System.out.print(getName());
        System.out.println(" ----------");
        System.out.println();
        String description = getDescription();
        if (description != null) {
            System.out.print("Description: ");
            System.out.println(description);
            System.out.println();
        }
        executeExample();
        System.out.print("----------- End example: ");
        System.out.print(getName());
        System.out.println(" -----------");
        System.out.println();
    }

    protected abstract void executeExample() throws Exception;
}
