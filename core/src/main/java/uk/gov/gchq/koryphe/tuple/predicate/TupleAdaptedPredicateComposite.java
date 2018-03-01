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

package uk.gov.gchq.koryphe.tuple.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.predicate.PredicateComposite;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.util.List;
import java.util.function.Predicate;

/**
 * A {@link uk.gov.gchq.koryphe.composite.Composite} {@link TupleAdaptedPredicate}, allowing different
 * {@link Predicate}s to be applied to different fields in tuples as a single Predicate.
 *
 * @param <R> Reference type used by tuples
 */
@Since("1.0.0")
public class TupleAdaptedPredicateComposite<R> extends PredicateComposite<Tuple<R>, TupleAdaptedPredicate<R, ? extends Object>> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    public List<TupleAdaptedPredicate<R, ? extends Object>> getComponents() {
        return super.getComponents();
    }

    public static class Builder<R> {
        private final TupleAdaptedPredicateComposite<R> composite;

        public Builder() {
            this(new TupleAdaptedPredicateComposite<R>());
        }

        private Builder(final TupleAdaptedPredicateComposite<R> composite) {
            this.composite = composite;
        }

        public SelectedBuilder<R> select(final R[] selection) {
            final TupleAdaptedPredicate<R, ?> current = new TupleAdaptedPredicate<>();
            current.setSelection(selection);
            return new SelectedBuilder<R>(composite, current);
        }

        public TupleAdaptedPredicateComposite<R> build() {
            return composite;
        }
    }

    public static final class SelectedBuilder<R> {
        private final TupleAdaptedPredicateComposite filter;
        private final TupleAdaptedPredicate<R, ?> current;

        private SelectedBuilder(final TupleAdaptedPredicateComposite filter, final TupleAdaptedPredicate<R, ?> current) {
            this.filter = filter;
            this.current = current;
        }

        public Builder<R> execute(final Predicate function) {
            current.setPredicate(function);
            filter.components.add(current);
            return new Builder(filter);
        }
    }
}
