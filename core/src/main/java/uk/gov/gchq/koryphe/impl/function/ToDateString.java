/*
 * Copyright 2019 Crown Copyright
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

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.Objects.isNull;

/**
 * A <code>ToDateString</code> is a {@link java.util.function.Function} that
 * converts a {@link Date} into a {@link String} using the provided format.
 */
@Since("1.8.0")
@Summary("Converts a date to a string")
public class ToDateString extends KorypheFunction<Date, String> {
    private String format;

    public ToDateString() {
    }

    public ToDateString(final String format) {
        this.format = format;
    }

    @Override
    public String apply(final Date date) {
        if (isNull(date)) {
            return null;
        }

        if (isNull(format)) {
            return date.toInstant().toString();
        }

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }
}
