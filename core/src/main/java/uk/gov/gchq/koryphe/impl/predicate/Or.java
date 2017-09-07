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

package uk.gov.gchq.koryphe.impl.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.predicate.PredicateComposite;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.predicate.IntegerTupleAdaptedPredicate;
import uk.gov.gchq.koryphe.tuple.predicate.TupleAdaptedPredicate;

import java.util.List;
import java.util.function.Predicate;

/**
 * A composite {@link java.util.function.Predicate} that returns true if any of it's predicates return true, otherwise false.
 *
 * @param <I> Type of input to be validated.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public class Or<I> extends PredicateComposite<I, Predicate<I>> {
    public Or() {
        super();
    }

    public Or(final Predicate<?>... predicates) {
        this(Lists.newArrayList(predicates));
    }

    public Or(final List<Predicate> predicates) {
        super((List) predicates);
    }

    @Override
    public boolean test(final I input) {
        for (final Predicate<I> predicate : components) {
            try {
                if (predicate.test(input)) {
                    return true;
                }
            } catch (final ClassCastException e) {
                // This may occur if the predicate was given a tuple1 and the tuple1 was automatically unpacked.
                if (predicate instanceof TupleAdaptedPredicate && !(input instanceof Tuple)) {
                    if (((TupleAdaptedPredicate) predicate).getPredicate().test(input)) {
                        return true;
                    }
                } else {
                    throw e;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(this)
                .toString();
    }

    public static class Builder<I> {
        private final Or<I> or;

        public Builder() {
            this(new Or<>());
        }

        private Builder(final Or<I> or) {
            this.or = or;
        }

        public Or.SelectedBuilder<I> select(final Integer... selection) {
            final IntegerTupleAdaptedPredicate current = new IntegerTupleAdaptedPredicate();
            current.setSelection(selection);
            return new Or.SelectedBuilder<>(or, current);
        }

        public Or<I> build() {
            return or;
        }
    }

    public static final class SelectedBuilder<I> {
        private final Or<I> or;
        private final IntegerTupleAdaptedPredicate current;

        private SelectedBuilder(final Or<I> or, final IntegerTupleAdaptedPredicate current) {
            this.or = or;
            this.current = current;
        }

        public Or.Builder<I> execute(final Predicate function) {
            current.setPredicate(function);
            or.getComponents().add((Predicate) current);
            return new Or.Builder<>(or);
        }
    }
}
