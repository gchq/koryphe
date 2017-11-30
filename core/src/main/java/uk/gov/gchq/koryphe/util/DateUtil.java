/*
 * Copyright 2017 Crown Copyright
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
    private static final Map<Pattern, String> FORMATS = new LinkedHashMap<>();

    static {
        FORMATS.put(Pattern.compile("^\\d{4}$"), "yyyy");
        FORMATS.put(Pattern.compile("^\\d{6}$"), "yyyyMM");
        FORMATS.put(Pattern.compile("^\\d{8}$"), "yyyyMMdd");
        FORMATS.put(Pattern.compile("^\\d{10}$"), "yyyyMMddHH");
        FORMATS.put(Pattern.compile("^\\d{12}$"), "yyyyMMddHHmm");
        FORMATS.put(Pattern.compile("^\\d{14}$"), "yyyyMMddHHmmss");
    }

    private static final Pattern CHARS_TO_STRIP = Pattern.compile("[/_.:\\-| ]");
    private static final String ERROR_MSG = "The provided date string %s could not be parsed. " +
            "Please use one of the following formats: "
            + FORMATS.values().toString()
            + ". You can also use a space, '-', '/', '_', ':', '|', or '.' to separate the parts.";

    private DateUtil() {
    }

    /**
     * Parse the provided date.
     *
     * @param dateString The date string to parse
     * @return parsed date
     */
    public static Date parse(final String dateString) {
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
}
