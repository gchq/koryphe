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

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

/**
 * A {@link StringTruncate} is a {@link java.util.function.Function} which truncates an input {@link String} to a
 * specified length, optionally appending ellipsis characters to denote that the string was truncated.
 */
@Since("1.9.0")
@Summary("Truncates a string, with optional ellipses.")
public class StringTruncate extends KorypheFunction<String, String> {
    private static final String ELLIPSES = "...";
    private int length;
    private boolean ellipses;

    public StringTruncate() {
    }

    public StringTruncate(final int length) {
        this.length = length;
    }

    public StringTruncate(final int length, final boolean ellipses) {
        this.length = length;
        this.ellipses = ellipses;
    }

    @Override
    public String apply(final String input) {
        if (null == input) {
            return null;
        }

        String truncated = input.substring(0, length);

        if (ellipses) {
            truncated += ELLIPSES;
        }

        return truncated;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
    }

    public boolean isEllipses() {
        return ellipses;
    }

    public void setEllipses(final boolean ellipses) {
        this.ellipses = ellipses;
    }
}
