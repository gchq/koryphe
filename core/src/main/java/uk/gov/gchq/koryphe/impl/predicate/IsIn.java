/*
 * Copyright 2017-2025 Crown Copyright
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
import java.util.Set;

/**
 * <code>IsIn</code> is a {@link java.util.function.Predicate} that checks that the input is
 * in a set of allowed values.
 */
@Since("1.0.0")
@Summary("Checks if an input is in a set of allowed values")
public class IsIn extends KoryphePredicate<Object> {
    private Set<Object> allowedValues;

    public IsIn() {
        // Required for serialisation
    }

    public IsIn(final Collection<Object> controlData) {
        this.allowedValues = new HashSet<>(controlData);
    }

    public IsIn(final Object... controlData) {
        this.allowedValues = Sets.newHashSet(controlData);
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

    @JsonIgnore
    public Set<Object> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(final Set<Object> allowedValues) {
        this.allowedValues = allowedValues;
    }

    @Override
    public boolean test(final Object input) {
        return null != allowedValues && allowedValues.contains(input);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final IsIn isIn = (IsIn) obj;
        return new EqualsBuilder()
                .append(allowedValues, isIn.allowedValues)
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
