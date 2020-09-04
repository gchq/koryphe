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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.iterable.CloseableIterable;
import uk.gov.gchq.koryphe.util.IterableUtil;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Since("1.8.0")
@Summary("Parses CSV lines into Maps")
@JsonPropertyOrder(value = {"header", "firstRow", "delimiter", "quoted", "quoteChar"},
        alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CsvLinesToMaps extends KorypheFunction<Iterable<String>, Iterable<Map<String, Object>>> implements Serializable {
    private static final long serialVersionUID = -4225921410795200955L;
    private List<String> header = new ArrayList<>();
    private int firstRow = 0;
    private char delimiter = ',';
    private boolean quoted = false;
    private char quoteChar = '\"';

    @Override
    public Iterable<Map<String, Object>> apply(final Iterable<String> csvStrings) {
        if (isNull(csvStrings)) {
            return null;
        }

        final CloseableIterable<String> csvRecords = IterableUtil.limit(csvStrings, firstRow, null, false);
        return IterableUtil.map(csvRecords, (item) -> createMap((String) item));
    }

    private Map<String, Object> createMap(final String csvItem) {
        return extractMap(parseCsv(csvItem));
    }

    private Map<String, Object> extractMap(final CSVRecord csvRecord) {
        final Iterator<String> columnNamesItr = header.iterator();
        final Map<String, Object> map = new HashMap<>();
        for (final String columnValue : csvRecord) {
            map.put(columnNamesItr.next(), columnValue);
        }
        return map;
    }

    private CSVRecord parseCsv(final String csv) {
        final CSVRecord csvRecord;
        try {
            csvRecord = new CSVParser(new StringReader(csv), getCsvFormat()).iterator().next();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        if (csvRecord.size() != header.size()) {
            throw new IllegalArgumentException(
                    "CSV has " + csvRecord.size()
                            + " columns, but there are " + header.size()
                            + " provided column names"
            );
        }
        return csvRecord;
    }

    private CSVFormat getCsvFormat() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(delimiter);
        if (quoted) {
            format = format.withQuote(quoteChar);
        }
        return format;
    }

    public List<String> getHeader() {
        return header;
    }

    public void setHeader(final List<String> header) {
        this.header.clear();
        this.header.addAll(header);
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(final int firstRow) {
        this.firstRow = firstRow;
    }

    public CsvLinesToMaps firstRow(final int firstRow) {
        this.firstRow = firstRow;
        return this;
    }

    public CsvLinesToMaps header(final String... header) {
        Collections.addAll(this.header, header);
        return this;
    }

    public CsvLinesToMaps header(final Collection<String> header) {
        this.header.addAll(header);
        return this;
    }

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(final char delimiter) {
        this.delimiter = delimiter;
    }

    public CsvLinesToMaps delimiter(final char delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public boolean isQuoted() {
        return quoted;
    }

    public void setQuoted(final boolean quoted) {
        this.quoted = quoted;
    }

    public CsvLinesToMaps quoted() {
        this.quoted = true;
        return this;
    }

    public CsvLinesToMaps quoted(final boolean quoted) {
        this.quoted = quoted;
        return this;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    public void setQuoteChar(final char quoteChar) {
        this.quoteChar = quoteChar;
    }

    public CsvLinesToMaps quoteChar(final char quoteChar) {
        this.quoteChar = quoteChar;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.equals(o)) {
            return false; // Does class checking
        }

        CsvLinesToMaps that = (CsvLinesToMaps) o;
        return new EqualsBuilder()
                .append(header, that.header)
                .append(quoted, that.quoted)
                .append(quoteChar, that.quoteChar)
                .append(firstRow, that.firstRow)
                .append(delimiter, that.delimiter)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 19)
                .appendSuper(super.hashCode())
                .append(header)
                .append(quoted)
                .append(quoteChar)
                .append(firstRow)
                .append(delimiter)
                .toHashCode();
    }
}
