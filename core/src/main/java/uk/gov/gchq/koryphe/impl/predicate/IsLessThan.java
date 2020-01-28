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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;
import uk.gov.gchq.koryphe.signature.InputValidator;

/**
 * An <code>IsLessThan</code> is a {@link java.util.function.Predicate} that checks that the input
 * {@link Comparable} is less than a control value. There is also an orEqualTo flag that can be set to allow
 * the input value to be less than or equal to the control value.
 */
@Since("1.0.0")
@Summary("Checks if a comparable is less than a provided value")
public class IsLessThan extends KoryphePredicate<Comparable> implements InputValidator {
    private Comparable controlValue;
    private boolean orEqualTo;

    public IsLessThan() {
        // Required for serialisation
    }

    public IsLessThan(final Comparable<?> controlValue) {
        this(controlValue, false);
    }

    public IsLessThan(final Comparable controlValue, final boolean orEqualTo) {
        this.controlValue = controlValue;
        this.orEqualTo = orEqualTo;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    @JsonProperty("value")
    public Comparable getControlValue() {
        return controlValue;
    }

    public void setControlValue(final Comparable controlValue) {
        this.controlValue = controlValue;
    }

    public boolean getOrEqualTo() {
        return orEqualTo;
    }

    public void setOrEqualTo(final boolean orEqualTo) {
        this.orEqualTo = orEqualTo;
    }

    @Override
    public boolean test(final Comparable input) {
        if (null == input || !controlValue.getClass().isAssignableFrom(input.getClass())) {
            return false;
        }

        final int compareVal = controlValue.compareTo(input);
        if (orEqualTo) {
            return compareVal >= 0;
        }

        return compareVal > 0;
    }

    @Override
    public ValidationResult isInputValid(final Class<?>... arguments) {
        final ValidationResult result = new ValidationResult();
        if (null == controlValue) {
            result.addError("Control value has not been set");
            return result;
        }

        if (null == arguments || 1 != arguments.length || null == arguments[0]) {
            result.addError("Incorrect number of arguments for " + getClass().getName() + ". 1 argument is required.");
            return result;
        }

        if (!controlValue.getClass().isAssignableFrom(arguments[0])) {
            result.addError("Control value class " + controlValue.getClass().getName() + " is not compatible with the input type: " + arguments[0]);
        }

        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final IsLessThan that = (IsLessThan) obj;
        return new EqualsBuilder()
                .append(orEqualTo, that.orEqualTo)
                .append(controlValue, that.controlValue)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(controlValue)
                .append(orEqualTo)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("controlValue", controlValue)
                .append("orEqualTo", orEqualTo)
                .toString();
    }
}
