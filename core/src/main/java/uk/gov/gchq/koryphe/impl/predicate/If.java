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
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;
import uk.gov.gchq.koryphe.signature.InputValidator;
import uk.gov.gchq.koryphe.signature.Signature;

import java.util.function.Predicate;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public class If<I> extends KoryphePredicate<I> implements InputValidator {

    private boolean condition;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    private Predicate<I> then;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    private Predicate<I> otherwise;

    public If() {

    }

    public If(final boolean condition, final Predicate<I> then, final Predicate<I> otherwise) {
        this.condition = condition;
        this.then = then;
        this.otherwise = otherwise;
    }

    public boolean getCondition() {
        return condition;
    }

    public Predicate<I> getThen() {
        return then;
    }

    public Predicate<I> getOtherwise() {
        return otherwise;
    }

    @Override
    public boolean test(final I input) {
        if (condition) {
            return null != then && then.test(input);
        } else {
            return null != otherwise && otherwise.test(input);
        }
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
                .append(then, ifPredicate.then)
                .append(otherwise, ifPredicate.otherwise)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("condition", condition)
                .append("then", then)
                .append("otherwise", otherwise)
                .toString();
    }

    @Override
    public ValidationResult isInputValid(final Class<?>... args) {
        if (null == then || null == otherwise) {
            return new ValidationResult();
        }

        final ValidationResult result = Signature.getInputSignature(then).assignable(args);
        result.add(Signature.getInputSignature(otherwise).assignable(args));
        return result;
    }
}
