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
import uk.gov.gchq.koryphe.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Objects.isNull;

/**
 * Parses a date string and returns the {@link Date}.
 */
@Since("1.6.0")
@Summary("Parses a date string")
public class ParseDate extends KorypheFunction<String, Date> {
    private String format;
    private TimeZone timeZone;

    @Override
    public Date apply(final String dateString) {
        try {
            final Date date;
            if (isNull(format)) {
                date = DateUtil.parse(dateString, timeZone);
            } else {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                simpleDateFormat.setTimeZone(timeZone);
                date = simpleDateFormat.parse(dateString);
            }
            return date;
        } catch (final ParseException e) {
            throw new IllegalArgumentException("Date string could not be parsed: " + dateString, e);
        }
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public ParseDate format(final String format) {
        setFormat(format);
        return this;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(final String timeZone) {
        setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    public void setTimeZone(final TimeZone timeZone) {
        if (isNull(timeZone)) {
            this.timeZone = DateUtil.getTimeZoneDefault();
        }
        this.timeZone = timeZone;
    }

    public ParseDate timeZone(final String timeZone) {
        setTimeZone(timeZone);
        return this;
    }

    public ParseDate timeZone(final TimeZone timeZone) {
        setTimeZone(timeZone);
        return this;
    }
}
