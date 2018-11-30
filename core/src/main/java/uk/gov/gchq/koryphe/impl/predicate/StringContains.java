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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;

/**
 * A <code>StringContains</code> is a {@link java.util.function.Predicate} that checks if a provided {@link String}
 * contains some {@link String} value.
 * The {@link java.util.function.Predicate} is case-sensitive by default, this can be changed with
 * <code>setIgnoreCase(true)</code>.
 */
@Since("1.0.0")
@Summary("Checks if a string contains some value")
public class StringContains extends KoryphePredicate<String> {
    private String value;
    private boolean ignoreCase;

    public StringContains() {
        // Required for serialisation
    }

    public StringContains(final String value) {
        this(value, false);
    }

    public StringContains(final String value, final boolean ignoreCase) {
        this.value = value;
        this.ignoreCase = ignoreCase;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public boolean getIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(final boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean test(final String input) {
        if (null == input || null == value) {
            return false;
        }
        if (ignoreCase) {
            return StringUtils.containsIgnoreCase(input, value);
        }
        return input.contains(value);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final StringContains that = (StringContains) obj;

        return new EqualsBuilder()
                .append(value, that.value)
                .append(ignoreCase, that.ignoreCase)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 53)
                .append(value)
                .append(ignoreCase)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .append("ignoreCase", ignoreCase)
                .toString();
    }
}
