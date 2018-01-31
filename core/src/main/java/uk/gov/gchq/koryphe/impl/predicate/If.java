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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;
import uk.gov.gchq.koryphe.signature.InputValidator;
import uk.gov.gchq.koryphe.signature.Signature;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.predicate.IntegerTupleAdaptedPredicate;

import java.util.function.Predicate;

/**
 * An {@code If} is a {@link Predicate} that conditionally applies one of two predicates to a provided input.
 *
 * @param <I> the type of input to be validated
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public class If<I> extends KoryphePredicate<I> implements InputValidator {

    private Boolean condition;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    private Predicate<? super I> predicate;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    private Predicate<? super I> then;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    private Predicate<? super I> otherwise;

    public If() {
    }

    public If(final boolean condition, final Predicate<? super I> then) {
        this(condition, then, null);
    }

    public If(final boolean condition, final Predicate<? super I> then, final Predicate<? super I> otherwise) {
        this.condition = condition;
        this.then = then;
        this.otherwise = otherwise;
    }

    public If(final Predicate<? super I> predicate, final Predicate<? super I> then) {
        this(predicate, then, null);
    }

    public If(final Predicate<? super I> predicate, final Predicate<? super I> then, final Predicate<? super I> otherwise) {
        this.predicate = predicate;
        this.then = then;
        this.otherwise = otherwise;
    }

    public Boolean getCondition() {
        return condition;
    }

    public void setCondition(final boolean condition) {
        this.condition = condition;
    }

    public Predicate<? super I> getThen() {
        return then;
    }

    public void setThen(final Predicate<? super I> then) {
        this.then = then;
    }

    public Predicate<? super I> getOtherwise() {
        return otherwise;
    }

    public void setOtherwise(final Predicate<? super I> otherwise) {
        this.otherwise = otherwise;
    }

    public Predicate<? super I> getPredicate() {
        return predicate;
    }

    public void setPredicate(final Predicate<? super I> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(final I input) {
        if (null == condition) {
            condition = null != predicate && predicate.test(input);
        }

        if (condition) {
            return null != then && then.test(input);
        }

        return null != otherwise && otherwise.test(input);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }

        final If ifPredicate = (If) obj;

        return new EqualsBuilder()
                .append(condition, ifPredicate.condition)
                .append(predicate, ifPredicate.predicate)
                .append(then, ifPredicate.then)
                .append(otherwise, ifPredicate.otherwise)
                .build();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 73)
                .append(condition)
                .append(predicate)
                .append(then)
                .append(otherwise)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("condition", condition)
                .append("predicate", predicate)
                .append("then", then)
                .append("otherwise", otherwise)
                .toString();
    }

    @Override
    public ValidationResult isInputValid(final Class<?>... args) {
        final ValidationResult result = new ValidationResult();
        if (null != predicate) {
            result.add(Signature.getInputSignature(predicate).assignable(args));
        }

        if (null != then) {
            result.add(Signature.getInputSignature(then).assignable(args));
        }

        if (null != otherwise) {
            result.add(Signature.getInputSignature(otherwise).assignable(args));
        }

        return result;
    }

    public static class SelectedBuilder {
        private final If<Tuple<Integer>> ifPredicate;

        public SelectedBuilder() {
            this(new If<>());
        }

        private SelectedBuilder(final If<Tuple<Integer>> ifPredicate) {
            this.ifPredicate = ifPredicate;
        }

        public If.SelectedBuilder condition(final boolean condition) {
            ifPredicate.setCondition(condition);
            return new If.SelectedBuilder(ifPredicate);
        }

        public If.SelectedBuilder predicate(final Predicate<Tuple<Integer>> predicate) {
            ifPredicate.setPredicate(predicate);
            return new If.SelectedBuilder(ifPredicate);
        }

        public If.SelectedBuilder predicate(final Predicate<?> predicate, final Integer... selection) {
            final IntegerTupleAdaptedPredicate current = new IntegerTupleAdaptedPredicate(predicate, selection);
            ifPredicate.setPredicate(current);
            return new If.SelectedBuilder(ifPredicate);
        }

        public If.SelectedBuilder then(final Predicate<Tuple<Integer>> then) {
            ifPredicate.setThen(then);
            return new If.SelectedBuilder(ifPredicate);
        }

        public If.SelectedBuilder then(final Predicate<?> then, final Integer... selection) {
            final IntegerTupleAdaptedPredicate current = new IntegerTupleAdaptedPredicate(then, selection);
            ifPredicate.setThen(current);
            return new If.SelectedBuilder(ifPredicate);
        }

        public If.SelectedBuilder otherwise(final Predicate<Tuple<Integer>> otherwise) {
            ifPredicate.setOtherwise(otherwise);
            return new If.SelectedBuilder(ifPredicate);
        }

        public If.SelectedBuilder otherwise(final Predicate<?> otherwise, final Integer... selection) {
            final IntegerTupleAdaptedPredicate current = new IntegerTupleAdaptedPredicate(otherwise, selection);
            ifPredicate.setOtherwise(current);
            return new If.SelectedBuilder(ifPredicate);
        }

        public If<Tuple<Integer>> build() {
            return ifPredicate;
        }
    }
}
