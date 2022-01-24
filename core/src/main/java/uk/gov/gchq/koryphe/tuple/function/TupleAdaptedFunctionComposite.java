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

package uk.gov.gchq.koryphe.tuple.function;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.FunctionComposite;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.util.List;
import java.util.function.Function;

/**
 * A {@link uk.gov.gchq.koryphe.composite.Composite} {@link TupleAdaptedFunction}, allowing different
 * {@link Function}s to be applied to different fields in tuples as a single Function.
 *
 * @param <R> Reference type used by tuples
 */
@Since("1.0.0")
@Summary("Applies multiple functions and adapts the input/outputs")
public class TupleAdaptedFunctionComposite<R>
        extends FunctionComposite<Tuple<R>, Tuple<R>, TupleAdaptedFunction<R, ? extends Object, ? extends Object>> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    public List<TupleAdaptedFunction<R, ? extends Object, ? extends Object>> getComponents() {
        return super.getComponents();
    }

    public static class Builder<R> {
        private final TupleAdaptedFunctionComposite<R> transformer;

        public Builder() {
            this(new TupleAdaptedFunctionComposite<>());
        }

        private Builder(final TupleAdaptedFunctionComposite<R> transformer) {
            this.transformer = transformer;
        }

        public SelectedBuilder<R> select(final R[] selection) {
            final TupleAdaptedFunction<R, ?, ?> current = new TupleAdaptedFunction<>();
            current.setSelection(selection);
            return new SelectedBuilder<>(transformer, current);
        }

        public TupleAdaptedFunctionComposite<R> build() {
            return transformer;
        }
    }

    public static final class SelectedBuilder<R> {
        private final TupleAdaptedFunctionComposite<R> transformer;
        private final TupleAdaptedFunction<R, ?, ?> current;

        private SelectedBuilder(final TupleAdaptedFunctionComposite<R> transformer, final TupleAdaptedFunction<R, ?, ?> current) {
            this.transformer = transformer;
            this.current = current;
        }

        public ExecutedBuilder<R> execute(final Function function) {
            current.setFunction(function);
            return new ExecutedBuilder<>(transformer, current);
        }
    }

    public static final class ExecutedBuilder<R> {
        private final TupleAdaptedFunctionComposite<R> transformer;
        private final TupleAdaptedFunction<R, ?, ?> current;

        private ExecutedBuilder(final TupleAdaptedFunctionComposite<R> transformer, final TupleAdaptedFunction<R, ?, ?> current) {
            this.transformer = transformer;
            this.current = current;
        }

        public Builder<R> project(final R[] projection) {
            current.setProjection(projection);
            transformer.components.add(current);
            return new Builder<>(transformer);
        }
    }
}
