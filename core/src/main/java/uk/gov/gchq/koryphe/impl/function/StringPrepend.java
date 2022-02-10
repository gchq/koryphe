/*
 * Copyright 2020 Crown Copyright
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import static java.util.Objects.requireNonNullElse;

/**
 * A {@code StringPrepend} is a {@link java.util.function.Function} which takes a input {@link String} and returns the
 * same string, prepended with a prefix.
 */
@Since("1.9.0")
@Summary("Prepends a string with the provided prefix.")
public class StringPrepend extends KorypheFunction<String, String> {

    private String prefix;

    public StringPrepend() {
    }

    public StringPrepend(final String prefix) {
        setPrefix(prefix);
    }

    @Override
    public String apply(final String input) {
        if (null == input) {
            return null;
        }
        if (null == prefix) {
            return input;
        }
        return prefix + input;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        this.prefix = requireNonNullElse(prefix, StringUtils.EMPTY);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.equals(o)) {
            return false; // Does exact equals and class checking
        }

        StringPrepend that = (StringPrepend) o;
        return new EqualsBuilder()
                .append(prefix, that.prefix)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(63, 37)
                .appendSuper(super.hashCode())
                .append(prefix)
                .toHashCode();
    }
}
