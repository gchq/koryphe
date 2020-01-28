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
import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;
import uk.gov.gchq.koryphe.signature.InputValidator;
import uk.gov.gchq.koryphe.signature.Signature;

import java.util.function.Predicate;

/**
 * A {@link java.util.function.Predicate} that returns the inverse of the wrapped predicate.
 *
 * @param <I> Type of input to be validated
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
@Since("1.0.0")
@Summary("Returns the inverse of a predicate")
public class Not<I> extends KoryphePredicate<I> implements InputValidator {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    private Predicate<I> predicate;

    public Not() {
    }

    public Not(final Predicate<I> predicate) {
        this.predicate = predicate;
    }

    public void setPredicate(final Predicate<I> predicate) {
        this.predicate = predicate;
    }

    public Predicate<I> getPredicate() {
        return predicate;
    }

    @Override
    public boolean test(final I input) {
        return null != predicate && !predicate.test(input);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final Not not = (Not) obj;

        return new EqualsBuilder()
                .append(predicate, not.predicate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(predicate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("predicate", predicate)
                .toString();
    }

    @Override
    public ValidationResult isInputValid(final Class<?>... arguments) {
        if (null == predicate) {
            return new ValidationResult();
        }

        return Signature.getInputSignature(predicate).assignable(arguments);
    }
}
