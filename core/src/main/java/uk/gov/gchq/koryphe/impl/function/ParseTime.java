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
import uk.gov.gchq.koryphe.util.TimeUnit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Parses a date string and returns the timestamp as a {@link Long}.
 */
@Since("1.8.0")
@Summary("Parses a date string into a timestamp")
public class ParseTime extends KorypheFunction<String, Long> {
    private String format;
    private TimeZone timeZone;
    private TimeUnit timeUnit;

    public ParseTime() {
        setTimeZone((TimeZone) null);
        setTimeUnit((TimeUnit) null);
    }

    @Override
    public Long apply(final String dateString) {
        if (isNull(dateString)) {
            return null;
        }

        try {
            final Long time;
            if (isNull(format)) {
                time = DateUtil.parseTime(dateString, timeZone);
            } else {
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                if (nonNull(timeZone)) {
                    simpleDateFormat.setTimeZone(timeZone);
                }
                time = simpleDateFormat.parse(dateString).getTime();
            }
            return timeUnit.fromMilliSeconds(time);
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

    public ParseTime format(final String format) {
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

    public ParseTime timeZone(final String timeZone) {
        setTimeZone(timeZone);
        return this;
    }

    public ParseTime timeZone(final TimeZone timeZone) {
        setTimeZone(timeZone);
        return this;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @JsonSetter
    public void setTimeUnit(final TimeUnit timeUnit) {
        if (isNull(timeUnit)) {
            this.timeUnit = TimeUnit.MILLISECOND;
        } else {
            this.timeUnit = timeUnit;
        }
    }

    public void setTimeUnit(final String timeUnit) {
        setTimeUnit(nonNull(timeUnit) ? TimeUnit.valueOf(timeUnit) : null);
    }

    public ParseTime timeUnit(final String timeUnit) {
        setTimeUnit(timeUnit);
        return this;
    }

    public ParseTime timeUnit(final TimeUnit timeUnit) {
        setTimeUnit(timeUnit);
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (!super.equals(o)) {
            return false; // Does exact equals and class checking
        }

        ParseTime that = (ParseTime) o;
        return new EqualsBuilder()
                .append(timeZone, that.timeZone)
                .append(format, that.format)
                .append(timeUnit, that.timeUnit)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(29, 61)
                .append(super.hashCode())
                .append(timeZone)
                .append(format)
                .append(timeUnit)
                .toHashCode();
    }

}
