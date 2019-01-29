/*
 * Copyright 2019 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.function;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Lists;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.FunctionComposite;
import uk.gov.gchq.koryphe.tuple.function.TupleAdaptedFunctionComposite;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A composite {@link Function} that applies the given functions consecutively, returning the final output.
 *
 * @param <I> Type of input
 * @param <O> Type of output
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
@Since("1.6.0")
@Summary("Applies the given functions consecutively")
public class And<I, O> extends FunctionComposite<I, O, Function<I, O>> {
    public And() {
        super();
    }

    public And(final Function... functions) {
        this(Lists.newArrayList(functions));
    }

    public And(final List<Function> functions) {
        super((List) functions);
    }

    public static class Builder<I, O> {
        private final List<Function<I, O>> components;

        public Builder() {
            this.components = new ArrayList<>();
        }

        public Builder<I, O> execute(final Function function) {
            components.add(function);
            return this;
        }

        public <R> Builder<I, O> execute(final R[] selection, final Function function, final R[] projection) {
            components.add(
                    new TupleAdaptedFunctionComposite.Builder()
                            .select(selection)
                            .execute(function)
                            .project(projection)
                            .build()
            );
            return this;
        }

        public And<I, O> build() {
            return new And<>(new ArrayList<>(components));
        }
    }
}
