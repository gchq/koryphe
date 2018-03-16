/*
 * Copyright 2017-2018 Crown Copyright
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

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A utility class for Dates.
 */
public final class DateUtil {
    public static final double MICROSECONDS_TO_MILLISECONDS = 0.001;
    public static final long SECONDS_TO_MILLISECONDS = 1000L;
    public static final long MINUTES_TO_MILLISECONDS = 60L * SECONDS_TO_MILLISECONDS;
    public static final long HOURS_TO_MILLISECONDS = 60L * MINUTES_TO_MILLISECONDS;
    public static final long DAYS_TO_MILLISECONDS = 24L * HOURS_TO_MILLISECONDS;

    private static final Pattern TIMESTAMP_MILLISECONDS = Pattern.compile("^\\d*$");
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

    /**
     * <p>
     * Parse the provided date.
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
    public static Date parse(final String dateString) {
        if (null == dateString) {
            return null;
        }

        if (TIMESTAMP_MILLISECONDS.matcher(dateString).matches()) {
            try {
                return new Date(Long.parseLong(dateString));
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException(String.format(ERROR_MSG, dateString), e);
            }
        }

        final String formatedDateString = CHARS_TO_STRIP.matcher(dateString).replaceAll("");

        for (final Map.Entry<Pattern, String> entry : FORMATS.entrySet()) {
            if (entry.getKey().matcher(formatedDateString).matches()) {
                try {
                    return DateUtils.parseDate(formatedDateString, Locale.getDefault(), entry.getValue());
                } catch (final ParseException e) {
                    throw new IllegalArgumentException(String.format(ERROR_MSG, dateString), e);
                }
            }
        }
        throw new IllegalArgumentException(String.format(ERROR_MSG, dateString));
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
        final Date date = parse(dateString);
        return null != date ? date.getTime() : null;
    }
}
