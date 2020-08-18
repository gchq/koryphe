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
 * A {@code StringReplace} is a {@link java.util.function.Function} which searches an input {@link String} for all
 * occurrences of a search string, and replaces those matches with an alternative string.
 */
@Since("1.9.0")
@Summary("Replace all portions of a string which match a regular expression.")
public class StringReplace extends KorypheFunction<String, String> {

    private String replacement;
    private String searchString;

    public StringReplace() {
    }

    public StringReplace(final String searchString, final String replacement) {
        this.searchString = searchString;
        this.replacement = replacement;
    }

    @Override
    public String apply(final String input) {
        // If null, StringUtils will return null.
        return StringUtils.replace(input, searchString, replacement);
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(final String replacement) {
        this.replacement = replacement;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(final String searchString) {
        this.searchString = searchString;
    }

    @Override
    public boolean equals(final Object o) {
        if (!super.equals(o)) {
            return false; // Does exact equals and class checking
        }

        StringReplace that = (StringReplace) o;
        return new EqualsBuilder()
                .append(searchString, that.searchString)
                .append(replacement, that.replacement)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(71, 53)
                .append(super.hashCode())
                .append(searchString)
                .append(replacement)
                .toHashCode();
    }
}
