/*
 * Copyright 2019-2020 Crown Copyright
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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Parses a date string and returns the {@link Date}.
 */
@Since("1.8.0")
@Summary("Parses a date string")
public class ParseDate extends KorypheFunction<String, Date> {
    private String format;
    private TimeZone timeZone;
    private boolean microseconds;

    public ParseDate() {
        setTimeZone((TimeZone) null);
    }

    @Override
    public Date apply(final String dateString) {
        if (isNull(dateString)) {
            return null;
        }

        try {
            final Date date;
            if (isNull(format)) {
                date = DateUtil.parse(dateString, timeZone, microseconds);
            } else {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                if (nonNull(timeZone)) {
                    simpleDateFormat.setTimeZone(timeZone);
                }
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

    @JsonGetter("timeZone")
    public String getTimeZoneAsString() {
        return nonNull(timeZone) ? timeZone.getID() : null;
    }

    @JsonSetter
    public void setTimeZone(final String timeZone) {
        setTimeZone(nonNull(timeZone) ? TimeZone.getTimeZone(timeZone) : null);
    }

    public void setTimeZone(final TimeZone timeZone) {
        if (isNull(timeZone)) {
            this.timeZone = DateUtil.getTimeZoneDefault();
        } else {
            this.timeZone = timeZone;
        }
    }

    public ParseDate timeZone(final String timeZone) {
        setTimeZone(timeZone);
        return this;
    }

    public ParseDate timeZone(final TimeZone timeZone) {
        setTimeZone(timeZone);
        return this;
    }

    public boolean isMicroseconds() {
        return microseconds;
    }

    @JsonSetter
    public void setMicroseconds(final boolean microseconds) {
        this.microseconds = microseconds;
    }

    public ParseDate microseconds(final boolean microseconds) {
        setMicroseconds(microseconds);
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.equals(o)) {
            return false; // Does exact equals and class checking
        }

        ParseDate that = (ParseDate) o;
        return new EqualsBuilder()
                .append(timeZone, that.timeZone)
                .append(format, that.format)
                .append(microseconds, that.microseconds)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(73, 41)
                .appendSuper(super.hashCode())
                .append(timeZone)
                .append(format)
                .append(microseconds)
                .toHashCode();
    }
}
