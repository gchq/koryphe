/*
 * Copyright 2016-2020 Crown Copyright
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
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DateUtilTest {

    @Test
    public void shouldParseTimestampInMilliseconds() {
        // Given
        final long timestamp = System.currentTimeMillis();

        // When
        final Long result = DateUtil.parseTime(Long.toString(timestamp));

        // Then
        assertThat((long) result).isEqualTo(timestamp);
    }

    @Test
    public void shouldParseTimestampInMillisecondsWithTimeZone() {
        // Given
        final long timestamp = System.currentTimeMillis();

        // When
        final Long result = DateUtil.parseTime(Long.toString(timestamp), TimeZone.getTimeZone("Etc/GMT+6"));

        // Then
        assertThat((long) result).isEqualTo(timestamp);
    }

    @Test
    public void shouldParseDates() throws ParseException {
        // When / Then
        assertDate("2017-01", "2017-01", "yyyy-MM");
        assertDate("2017-01", "2017 01", "yyyy-MM");
        assertDate("2017-01", "2017/01", "yyyy-MM");
        assertDate("2017-01", "2017.01", "yyyy-MM");
        assertDate("2017-01-02", "2017-01-02", "yyyy-MM-dd");
        assertDate("2017-01-02", "2017/01.02", "yyyy-MM-dd");
        assertDate("2017-01-02", "2017-01 02", "yyyy-MM-dd");
        assertDate("2017-01-02 01", "2017-01-02 01", "yyyy-MM-dd HH");
        assertDate("2017-01-02 01", "2017.01.02/01", "yyyy-MM-dd HH");
        assertDate("2017-01-02 01:02", "2017.01.02 01:02", "yyyy-MM-dd HH:mm");
        assertDate("2017-01-02 01:02", "2017.01.02 01 02", "yyyy-MM-dd HH:mm");
        assertDate("2017-01-02 01:02", "2017/01/02 01 02", "yyyy-MM-dd HH:mm");
        assertDate("2017-01-02 01:02:30", "2017/01/02 01:02:30", "yyyy-MM-dd HH:mm:ss");
        assertDate("2017-01-02 01:02:30", "2017-01-02-01:02:30", "yyyy-MM-dd HH:mm:ss");
        assertDate("2017-01-02 01:02:30.123", "2017-01-02-01:02:30.123", "yyyy-MM-dd HH:mm:ss.SSS");
    }

    @Test
    public void shouldParseDatesTimeZone() throws ParseException {
        // Given
        final String dateString = "2017-01-02 01:02:30.123";
        final String timeZone = "Etc/GMT+6";

        // When
        final Date result = DateUtil.parse(dateString, TimeZone.getTimeZone(timeZone));

        // Then
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        final Date expected = sdf.parse(dateString);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void shouldNotParseInvalidDate() {
        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> DateUtil.parse("2017/1"))
                .withMessage("The provided date string 2017/1 could not be parsed. Please use a timestamp in " +
                "milliseconds or one of the following formats: [yyyy/MM, yyyy/MM/dd, yyyy/MM/dd HH, yyyy/MM/dd HH:mm, " +
                "yyyy/MM/dd HH:mm:ss, yyyy/MM/dd HH:mm:ss.SSS]. You can use a space, '-', '/', '_', ':', '|', or '.' " +
                "to separate the parts.");
    }

    private void assertDate(final String expected, final String testDate, final String format) throws ParseException {
        final Date expectedDate = DateUtils.parseDate(expected, Locale.getDefault(), format);
        assertThat(DateUtil.parse(testDate))
                .isEqualTo(expectedDate)
                .withFailMessage("Failed to parse date: %s", testDate);
    }
}
