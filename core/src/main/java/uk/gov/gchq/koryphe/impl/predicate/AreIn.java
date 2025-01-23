/*
 * Copyright 2017-2022 Crown Copyright
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * An <code>AreIn</code> is a {@link java.util.function.BiPredicate}
 * that checks if a provided {@link java.util.Collection} contains all the provided input values.
 *
 * An optional nullOrEmptyAllowedValuesAccepted flag (defaults to true) can determine
 * whether the provided allowedValues collection can be null or empty. When used
 * the flag sets the result for any input test.
 */
@Since("1.0.0")
@Summary("Checks if a provided collection contains all the provided input values")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class AreIn extends KoryphePredicate<Collection<?>> {
    private Collection<?> allowedValues;
    private boolean nullOrEmptyAllowedValuesAccepted = true;

    public AreIn() {
        // Required for serialisation
    }

    public AreIn(final Collection<?> allowedValues) {
        this.allowedValues = allowedValues;
    }

    public AreIn(final Collection<?> allowedValues, final boolean nullOrEmptyAllowedValuesAccepted) {
        this.allowedValues = allowedValues;
        this.nullOrEmptyAllowedValuesAccepted = nullOrEmptyAllowedValuesAccepted;
    }

    public AreIn(final Object... allowedValues) {
        this.allowedValues = Sets.newHashSet(allowedValues);
    }

    @JsonIgnore
    public Collection<?> getValues() {
        return allowedValues;
    }

    public void setValues(final Collection<?> allowedValues) {
        this.allowedValues = allowedValues;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    @JsonProperty("values")
    public Object[] getAllowedValuesArray() {
        return null != allowedValues ? allowedValues.toArray() : new Object[0];
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    @JsonProperty("values")
    public void setAllowedValues(final Object[] allowedValuesArray) {
        if (null != allowedValuesArray) {
            allowedValues = new HashSet<>(Arrays.asList(allowedValuesArray));
        } else {
            allowedValues = new HashSet<>(0);
        }
    }

    @JsonProperty("nullOrEmptyValuesAccepted")
    public boolean getNullOrEmptyAllowedValuesAccepted() {
        return nullOrEmptyAllowedValuesAccepted;
    }

    @JsonProperty("nullOrEmptyValuesAccepted")
    public void setNullOrEmptyAllowedValuesAccepted(final boolean nullOrEmptyAllowedValuesAccepted) {
        this.nullOrEmptyAllowedValuesAccepted = nullOrEmptyAllowedValuesAccepted;
    }

    @Override
    public boolean test(final Collection<?> input) {
        if (null == allowedValues || allowedValues.isEmpty()) {
            return nullOrEmptyAllowedValuesAccepted;
        }

        return (null != input && allowedValues.containsAll(input));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final AreIn that = (AreIn) obj;
        return new EqualsBuilder()
                .append(allowedValues, that.allowedValues)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(allowedValues)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("allowedValues", allowedValues)
                .toString();
    }
}
