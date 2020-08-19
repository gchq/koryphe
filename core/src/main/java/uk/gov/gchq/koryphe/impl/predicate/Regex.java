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
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;

import java.util.regex.Pattern;

/**
 * A {@link Regex} is a {@link KoryphePredicate} that returns true if an input
 * string matches a supplied regex pattern, false otherwise.
 */
@Since("1.0.0")
@Summary("Checks if a string matches a pattern")
public class Regex extends KoryphePredicate<String> {
    private Pattern controlValue;

    public Regex() {
        // Required for serialisation
    }

    public Regex(final String controlValue) {
        this(Pattern.compile(controlValue));
    }

    public Regex(final Pattern controlValue) {
        this.controlValue = controlValue;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    @JsonProperty("value")
    public Pattern getControlValue() {
        return controlValue;
    }

    public void setControlValue(final Pattern controlValue) {
        this.controlValue = controlValue;
    }

    @Override
    public boolean test(final String input) {
        return !(null == input || input.getClass() != String.class)
                && controlValue.matcher(input).matches();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final Regex regex = (Regex) obj;

        return new EqualsBuilder()
                .append(controlValue != null ? controlValue.toString() : null,
                        regex.controlValue != null ? regex.controlValue.toString(): null)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                // Pattern does not override hashCode()
                .append(controlValue != null ? controlValue.toString() : null)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                // Pattern does not override equals()
                .append("controlValue", controlValue.toString())
                .toString();
    }
}
