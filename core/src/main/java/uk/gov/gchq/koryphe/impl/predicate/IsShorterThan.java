/*
 * Copyright 2017-2018 Crown Copyright
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.impl.function.Length;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;
import uk.gov.gchq.koryphe.signature.InputValidator;

import java.util.Map;

/**
 * An <code>IsShorterThan</code> is a {@link java.util.function.Predicate} that checks that the input
 * object has a length less than a maximum length. There is also an orEqualTo flag that can be set to allow
 * the input length to be less than or equal to the maximum length.
 * <p>
 * Allowed object types are {@link String}s, arrays, {@link java.util.Collection}s and {@link java.util.Map}s.
 * Additional object types can easily be added by modifying the getLength(Object) method.
 */
@Since("1.0.0")
@Summary("Checks if the length of an input is more than than a value")
public class IsShorterThan extends KoryphePredicate<Object> implements InputValidator {
    private int maxLength;
    private boolean orEqualTo;

    private final Length delegate = new Length();

    // Default constructor for serialisation
    public IsShorterThan() {
    }

    public IsShorterThan(final int maxLength) {
        this(maxLength, false);
    }

    public IsShorterThan(final int maxLength, final boolean orEqualTo) {
        setMaxLength(maxLength);
        this.orEqualTo = orEqualTo;
    }

    public IsShorterThan maxLength(final int maxLength) {
        setMaxLength(maxLength);
        return this;
    }

    public IsShorterThan orEqualTo(final boolean orEqualTo) {
        this.orEqualTo = orEqualTo;
        return this;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(final int maxLength) {
        this.maxLength = maxLength;
        if (maxLength < Integer.MAX_VALUE) {
            delegate.setMaxLength(maxLength + 1);
        } else {
            delegate.setMaxLength(null);
        }
    }

    public boolean isOrEqualTo() {
        return orEqualTo;
    }

    public void setOrEqualTo(final boolean orEqualTo) {
        this.orEqualTo = orEqualTo;
    }

    @Override
    public boolean test(final Object input) {
        if (null == input) {
            return true;
        }

        if (orEqualTo) {
            return getLength(input) <= maxLength;
        } else {
            return getLength(input) < maxLength;
        }
    }

    private int getLength(final Object value) {
        return delegate.apply(value);
    }

    @Override
    public ValidationResult isInputValid(final Class<?>... arguments) {
        final ValidationResult result = new ValidationResult();
        if (null == arguments || 1 != arguments.length || null == arguments[0]) {
            result.addError("Incorrect number of arguments for " + getClass().getName() + ". 1 argument is required.");
            return result;
        }

        if (!String.class.isAssignableFrom(arguments[0])
                && !Object[].class.isAssignableFrom(arguments[0])
                && !Iterable.class.isAssignableFrom(arguments[0])
                && !Map.class.isAssignableFrom(arguments[0])) {
            result.addError("Input class " + arguments[0].getName() + " must be one of the following: "
                    + String.class.getName() + ", "
                    + Object[].class.getName() + ", "
                    + Iterable.class.getName() + ", "
                    + Map.class.getName());
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

        final IsShorterThan that = (IsShorterThan) obj;
        return new EqualsBuilder()
                .append(maxLength, that.maxLength)
                .append(orEqualTo, that.orEqualTo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(maxLength)
                .append(orEqualTo)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("maxLength", maxLength)
                .append("orEqualTo", orEqualTo)
                .toString();
    }
}
