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
package uk.gov.gchq.koryphe.impl.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.predicate.IntegerTupleAdaptedPredicate;
import uk.gov.gchq.koryphe.tuple.predicate.TupleAdaptedPredicate;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNullElse;

/**
 * An {@code If} is a {@link Predicate} that conditionally applies one of two predicates to a provided input.
 * <p> Note that the <code>If</code> has both a number of constructors as well as a <code>Builder</code>.
 * The use case for constructors would generally be for testing a single input. </p>
 * <p> The use case for the Builder allows greater flexibility,
 * mainly for allowing multiple inputs such as an Array of objects,
 * and control over which of these objects is tested by each predicate. </p>
 * <p> For example,
 * Given an input array of 3 objects, one may wish to test the first object in the array against the initial predicate,
 * then pass both the second and third objects to the resulting predicate, based on the outcome of the initial test.
 * This would require use of the <code>Builder</code>, passing a selection of 0 along with the first predicate,
 * and a selection of 1, 2 with the other predicates. </p>
 * This would look something like:
 * <pre>
 *     final If ifPredicate = new If.SelectedBuilder()
 *          .predicate(firstPredicate, 0)
 *          .then(thenPredicate, 1, 2)
 *          .otherwise(otherwisePredicate, 1, 2)
 *          .build();
 * </pre>
 *
 * @param <I> the type of input to be validated
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "class")
@Since("1.3.0")
@Summary("Conditionally applies a predicate")
public class If<I> extends KoryphePredicate<I> {

    private Boolean condition;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "class", defaultImpl = TupleAdaptedPredicate.class)
    private Predicate<? super I> predicate;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "class", defaultImpl = TupleAdaptedPredicate.class)
    private Predicate<? super I> then;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "class", defaultImpl = TupleAdaptedPredicate.class)
    private Predicate<? super I> otherwise;

    public If() {
        // Empty
    }

    /**
     * Constructs a new <code>If</code> object, with a boolean condition,
     * and one predicate that will test the input, if the boolean condition resolves to true.
     *
     * @param condition a boolean condition
     * @param then      the predicate to apply if true
     */
    public If(final boolean condition, final Predicate<? super I> then) {
        this(condition, then, null);
    }

    /**
     * Constructs a new <code>If</code> object, with a boolean condition,
     * one predicate to test the input if the condition is true,
     * and another predicate to test the input if the condition is false.
     *
     * @param condition a boolean condition
     * @param then      the predicate to apply if true
     * @param otherwise the predicate to apply if false
     */
    public If(final boolean condition, final Predicate<? super I> then, final Predicate<? super I> otherwise) {
        this.condition = condition;
        this.then = then;
        this.otherwise = otherwise;
    }

    /**
     * Constructs a new <code>If</code> object, with an initial <code>predicate</code>,
     * and another predicate to test the input if the initial predicate resolves to true.
     *
     * @param predicate the initial predicate applied to the input
     * @param then      the predicate to apply if true
     */
    public If(final Predicate<? super I> predicate, final Predicate<? super I> then) {
        this(predicate, then, null);
    }

    /**
     * Constructs a new <code>If</code> object, with an initial <code>predicate</code>,
     * a second predicate to test the input if the initial predicate resolves to true,
     * and a third predicate to test the input if the initial predicate resolves to false.
     *
     * @param predicate the initial predicate applied to the input
     * @param then      the predicate to apply if true
     * @param otherwise the predicate to apply if false
     */
    public If(final Predicate<? super I> predicate, final Predicate<? super I> then, final Predicate<? super I> otherwise) {
        this.predicate = predicate;
        this.then = then;
        this.otherwise = otherwise;
    }

    /**
     * If the condition is not being used or has not been set,
     * then the provided predicate will test the input (assuming it is also not null).
     * If this resolves to true, the <code>then</code> predicate will test the input,
     * else the <code>otherwise</code> predicate will be used.
     * The result of either of these being applied to the input is finally returned.
     *
     * @param input the input to be tested
     * @return true if the input passes the predicate, otherwise false
     */
    @Override
    public boolean test(final I input) {
        if (requireNonNullElse(condition, null != predicate && predicate.test(input))) {
            return null != then && then.test(input);
        }

        return null != otherwise && otherwise.test(input);
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
