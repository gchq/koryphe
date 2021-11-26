/*
 * Copyright 2017-2020 Crown Copyright
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
package uk.gov.gchq.koryphe.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.util.concurrent.TimeUnit.MICROSECONDS;

/**
 * A utility class for Dates.
 */
public final class DateUtil {
    public static final double MICROSECONDS_TO_MILLISECONDS = 0.001;
    public static final long SECONDS_TO_MILLISECONDS = 1000L;
    public static final long MINUTES_TO_MILLISECONDS = 60L * SECONDS_TO_MILLISECONDS;
    public static final long HOURS_TO_MILLISECONDS = 60L * MINUTES_TO_MILLISECONDS;
    public static final long DAYS_TO_MILLISECONDS = 24L * HOURS_TO_MILLISECONDS;

    public static final String TIME_ZONE = "koryphe.timezone.default";
    private static final TimeZone TIME_ZONE_DEFAULT = getTimeZoneDefault();

    private static final Pattern TIMESTAMP_EPOCH = Pattern.compile("^\\d*$");
    private static final Map<Pattern, String> FORMATS = new LinkedHashMap<>();

    static {
        FORMATS.put(Pattern.compile("^\\d{6}$"), "yyyyMM");
        FORMATS.put(Pattern.compile("^\\d{8}$"), "yyyyMMdd");
        FORMATS.put(Pattern.compile("^\\d{10}$"), "yyyyMMddHH");
        FORMATS.put(Pattern.compile("^\\d{12}$"), "yyyyMMddHHmm");
        FORMATS.put(Pattern.compile("^\\d{14}$"), "yyyyMMddHHmmss");
        FORMATS.put(Pattern.compile("^\\d{17}$"), "yyyyMMddHHmmssSSS");
    }

    private static final Pattern CHARS_TO_STRIP = Pattern.compile("[/_.:\\-| ]");
    private static final String ERROR_MSG = "The provided date string %s could not be parsed. " +
            "Please use a timestamp in milliseconds or one of the following formats: "
            + "[yyyy/MM, yyyy/MM/dd, yyyy/MM/dd HH, yyyy/MM/dd HH:mm, yyyy/MM/dd HH:mm:ss, yyyy/MM/dd HH:mm:ss.SSS]"
            + ". You can use a space, '-', '/', '_', ':', '|', or '.' to separate the parts.";

    private DateUtil() {
    }

    public static TimeZone getTimeZoneDefault() {
        return null != System.getProperty(TIME_ZONE) ? TimeZone.getTimeZone(System.getProperty(TIME_ZONE)) : null;
    }

    /**
     * <p>
     * Parse the provided date.
     * </p>
     * The date string can be in any of the following formats:
     * <ul>
     * <li>timestamp in milliseconds</li>
     * <li>timestamp in microseconds</li>
     * <li>yyyy/MM</li>
     * <li>yyyy/MM/dd</li>
     * <li>yyyy/MM/dd HH</li>
     * <li>yyyy/MM/dd HH:mm</li>
     * <li>yyyy/MM/dd HH:mm:ss</li>
     * <li>yyyy/MM/dd HH:mm:ss.SSS</li>
     * </ul>
     * You can use a space, '-', '/', '_', ':', '|', or '.' to separate the parts.
     *
     * @param dateString The date string to parse
     * @return parsed date
     */
    public static Date parse(final String dateString) {
        return parse(dateString, null, false);
    }

    public static Date parse(final String dateString, final TimeZone timeZone, final boolean microseconds) {
        if (null == dateString) {
            return null;
        }

        if (TIMESTAMP_EPOCH.matcher(dateString).matches()) {
            try {
                if (!microseconds) {
                    return new Date(Long.parseLong(dateString));
                } else {
                    final long epochMicroseconds = Long.parseLong(dateString);

                    final long epochSeconds = MICROSECONDS.toSeconds(epochMicroseconds);
                    final long nanos = MICROSECONDS.toNanos(Math.floorMod(epochMicroseconds, TimeUnit.SECONDS.toMicros(1)));

                    return Date.from(Instant.ofEpochSecond(epochSeconds, nanos));
                }
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException(String.format(ERROR_MSG, dateString), e);
            }
        }

        final String formattedDateString = CHARS_TO_STRIP.matcher(dateString).replaceAll("");

        for (final Map.Entry<Pattern, String> entry : FORMATS.entrySet()) {
            if (entry.getKey().matcher(formattedDateString).matches()) {
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat(entry.getValue());
                    if (null != timeZone) {
                        sdf.setTimeZone(timeZone);
                    }
                    return sdf.parse(formattedDateString);
                } catch (final ParseException e) {
                    throw new IllegalArgumentException(String.format(ERROR_MSG, dateString), e);
                }
            }
        }
        throw new IllegalArgumentException(String.format(ERROR_MSG, dateString));
    }

    public static Date parse(final String dateString, final TimeZone timeZone) {
        return parse(dateString, timeZone, false);
    }

    /**
     * <p>
     * Parse the provided date and returns the time in milliseconds.
     * </p>
     * The date string can be in any of the following formats:
     * <ul>
     * <li>timestamp in milliseconds</li>
     * <li>yyyy/MM</li>
     * <li>yyyy/MM/dd</li>
     * <li>yyyy/MM/dd HH</li>
     * <li>yyyy/MM/dd HH:mm</li>
     * <li>yyyy/MM/dd HH:mm:ss</li>
     * <li>yyyy/MM/dd HH:mm:ss.SSS</li>
     * </ul>
     * You can use a space, '-', '/', '_', ':', '|', or '.' to separate the parts.
     *
     * @param dateString The date string to parse
     * @return parsed date
     */
    public static Long parseTime(final String dateString) {
        return parseTime(dateString, TIME_ZONE_DEFAULT);
    }

    public static Long parseTime(final String dateString, final TimeZone timeZone) {
        final Date date = parse(dateString, timeZone);
        return null != date ? date.getTime() : null;
    }
}
