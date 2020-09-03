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

/**
 * A {@code StringAppend} is a {@link java.util.function.Function} which takes a input {@link String} and returns the
 * same string, appended with a suffix.
 */
@Since("1.9.0")
@Summary("Appends a provided suffix to a string.")
public class StringAppend extends KorypheFunction<String, String> {

    private String suffix;

    public StringAppend() {
    }

    public StringAppend(final String suffix) {
        setSuffix(suffix);
    }

    @Override
    public String apply(final String value) {
        if (null == value) {
            return null;
        }

        if (null == suffix) {
            return value;
        }

        return value + suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(final String suffix) {
        if (null == suffix) {
            this.suffix = StringUtils.EMPTY;
        } else {
            this.suffix = suffix;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.classEquals(o)) {
            return false; // Does exact equals and class checking
        }

        StringAppend that = (StringAppend) o;
        return new EqualsBuilder()
                .append(suffix, that.suffix)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 37)
                .appendSuper(super.hashCode())
                .append(suffix)
                .toHashCode();
    }
}
