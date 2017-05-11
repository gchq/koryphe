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
import uk.gov.gchq.koryphe.tuple.predicate.IntegerTupleAdaptedPredicate;
import java.util.List;
import java.util.function.Predicate;

/**
 * A composite {@link java.util.function.Predicate} that returns true if all of it's predicates return true, otherwise false.
 *
 * @param <I> Type of input to be validated
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public class And<I> extends PredicateComposite<I, Predicate<I>> {
    public And() {
        super();
    }

    public And(final Predicate<?>... predicates) {
        this(Lists.newArrayList(predicates));
    }

    public And(final List<Predicate> predicates) {
        super((List) predicates);
    }

    @Override
    public boolean test(final I input) {
        if (components == null || components.isEmpty()) {
            return true;
        } else {
            return super.test(input);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(this)
                .toString();
    }

    public static class Builder {
        private final And andComposite;

        public Builder() {
            this(new And());
        }

        private Builder(final And andComposite) {
            this.andComposite = andComposite;
        }

        public And.SelectedBuilder select(final Integer... selection) {
            final IntegerTupleAdaptedPredicate current = new IntegerTupleAdaptedPredicate();
            current.setSelection(selection);
            return new And.SelectedBuilder(andComposite, current);
        }

        public And build() {
            return andComposite;
        }
    }

    public static final class SelectedBuilder {
        private final And andComposite;
        private final IntegerTupleAdaptedPredicate current;

        private SelectedBuilder(final And andComposite, final IntegerTupleAdaptedPredicate current) {
            this.andComposite = andComposite;
            this.current = current;
        }

        public And.Builder execute(final Predicate function) {
            current.setPredicate(function);
            andComposite.getComponents().add(current);
            return new And.Builder(andComposite);
        }
    }
}
