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
import uk.gov.gchq.koryphe.util.IterableUtil;

/**
 * A {@code StringJoin} is a {@link java.util.function.Function} which joins together all items in the provided Iterable
 * into a single output string, separated by a supplied delimiter.
 * <p>
 * The {@link ToString} function is delegated to in order to create a string representation of each item in the Iterable.
 *
 * @param <I_ITEM> the type of object contained in the iterable
 */
@Since("1.9.0")
@Summary("Joins together all strings in an iterable using the supplied delimiter.")
public class StringJoin<I_ITEM> extends KorypheFunction<Iterable<I_ITEM>, String> {

    private String delimiter;

    public StringJoin() {
    }

    public StringJoin(final String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String apply(final Iterable<I_ITEM> items) {
        // If null, StringUtils will return null.
        return StringUtils.join(IterableUtil.map(items, new ToString()), delimiter);
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

        StringJoin that = (StringJoin) o;
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
