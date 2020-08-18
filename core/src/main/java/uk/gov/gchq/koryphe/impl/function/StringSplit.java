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

import java.util.Arrays;
import java.util.List;

/**
 * A {@code StringSplit} is a {@link java.util.function.Function} which splits a {@link String} based on a supplied
 * delimiter.
 */
@Since("1.9.0")
@Summary("Split a string using the provided regular expression.")
public class StringSplit extends KorypheFunction<String, List<String>> {

    private String delimiter;

    public StringSplit() {
    }

    public StringSplit(final String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public List<String> apply(final String input) {
        // If null, StringUtils will return null.
        final String[] arr = StringUtils.split(input, delimiter);

        if (null == arr) {
            return null;
        }

        return Arrays.asList(arr);
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(final String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public boolean equals(final Object o) {
        if (!super.equals(o)) {
            return false; // Does exact equals and class checking
        }

        StringSplit that = (StringSplit) o;
        return new EqualsBuilder()
                .append(delimiter, that.delimiter)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(53, 73)
                .append(super.hashCode())
                .append(delimiter)
                .toHashCode();
    }
}
