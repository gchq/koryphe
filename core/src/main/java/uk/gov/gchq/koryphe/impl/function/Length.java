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
package uk.gov.gchq.koryphe.impl.function;

import com.google.common.collect.Iterables;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.signature.InputValidator;
import uk.gov.gchq.koryphe.util.IterableUtil;

import java.util.Map;

/**
 * A {@link Length} is a {@link KorypheFunction} that returns the length for a provided object.
 */
@Since("1.3.0")
@Summary("Attempts to returns the length of an object")
public class Length extends KorypheFunction<Object, Integer> implements InputValidator {

    private Integer maxLength;

    public Length() {
        // For Serialisation
    }

    public Length(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public Integer apply(final Object value) {
        final int length;

        if (null == value) {
            length = 0;
        } else if (value instanceof String) {
            length = ((String) value).length();
        } else if (value instanceof Object[]) {
            length = ((Object[]) value).length;
        } else if (value instanceof Iterable) {
            if (null != maxLength) {
                length = Iterables.size(IterableUtil.limit((Iterable) value, 0, maxLength, true));
            } else {
                length = Iterables.size((Iterable) value);
            }
        } else if (value instanceof Map) {
            length = ((Map) value).size();
        } else {
            throw new IllegalArgumentException("Could not determine the size of the provided value");
        }

        if (null == maxLength) {
            return length;
        }

        return Math.min(length, maxLength);
    }

    @Override
    public ValidationResult isInputValid(final Class<?>... arguments) {
        final ValidationResult result = new ValidationResult();
        if (null == arguments || 1 != arguments.length || null == arguments[0]) {
            result.addError("Incorrect number of arguments for " + getClass().getName() + ". One (1) argument is required.");
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

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false; // Does exact equals and Class checking

        Length that = (Length) o;
        return new EqualsBuilder()
                .append(maxLength, that.maxLength)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(43, 67)
                .append(super.hashCode())
                .append(maxLength)
                .toHashCode();
    }
}
