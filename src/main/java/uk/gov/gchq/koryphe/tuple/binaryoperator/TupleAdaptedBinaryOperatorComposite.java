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

package uk.gov.gchq.koryphe.tuple.binaryoperator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorComposite;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.util.List;
import java.util.function.BinaryOperator;

/**
 * A {@link uk.gov.gchq.koryphe.composite.Composite} {@link TupleAdaptedBinaryOperator}, allowing different
 * {@link BinaryOperator}s to be applied to different fields in tuples as a single BinaryOperator.
 *
 * @param <R> Reference type used by tuples
 */
public class TupleAdaptedBinaryOperatorComposite<R> extends BinaryOperatorComposite<Tuple<R>, TupleAdaptedBinaryOperator<R, ? extends Object>> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    public List<TupleAdaptedBinaryOperator<R, ? extends Object>> getComponents() {
        return super.getComponents();
    }

    public static class Builder<R> {
        private final TupleAdaptedBinaryOperatorComposite<R> binaryOperator;

        public Builder() {
            this(new TupleAdaptedBinaryOperatorComposite<R>());
        }

        private Builder(final TupleAdaptedBinaryOperatorComposite<R> binaryOperator) {
            this.binaryOperator = binaryOperator;
        }

        public SelectedBuilder<R> select(final R... selection) {
            final TupleAdaptedBinaryOperator<R, ?> current = new TupleAdaptedBinaryOperator<>();
            current.setSelection(selection);
            return new SelectedBuilder(binaryOperator, current);
        }

        public TupleAdaptedBinaryOperatorComposite<R> build() {
            return binaryOperator;
        }
    }

    public static final class SelectedBuilder<R> {
        private final TupleAdaptedBinaryOperatorComposite<R> binaryOperator;
        private final TupleAdaptedBinaryOperator<R, ?> current;

        private SelectedBuilder(final TupleAdaptedBinaryOperatorComposite<R> binaryOperator, final TupleAdaptedBinaryOperator<R, ?> current) {
            this.binaryOperator = binaryOperator;
            this.current = current;
        }

        public Builder<R> execute(final BinaryOperator function) {
            current.setBinaryOperator(function);
            binaryOperator.components.add(current);
            return new Builder(binaryOperator);
        }
    }
}
