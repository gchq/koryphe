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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

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

@Since("1.6.0")
@Summary("Parses a CSV into Maps")
@JsonPropertyOrder(value = {"header", "firstRow", "delimiter", "quoted", "quoteChar"},
        alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ParseCsv extends KorypheFunction<String, Iterable<Map<String, Object>>> implements Serializable {
    private static final long serialVersionUID = 891938487168606844L;
    private List<String> header = new ArrayList<>();
    private int firstRow = 0;
    private char delimiter = ',';
    private boolean quoted = false;
    private char quoteChar = '\"';
    private boolean skipInvalid = false;

    @Override
    public Iterable<Map<String, Object>> apply(final String csv) {
        if (isNull(csv)) {
            return null;
        }

        try {
            final CSVParser csvParser = new CSVParser(new StringReader(csv), getCsvFormat());
            final CloseableIterable<CSVRecord> csvRecords = IterableUtil.limit(csvParser.getRecords(), firstRow, null, false);
            return IterableUtil.map(csvRecords, (item) -> extractMap((CSVRecord) item));
        } catch (final IOException e) {
            throw new RuntimeException("Unable to parse csv", e);
        }
    }

    private Map<String, Object> extractMap(final CSVRecord csvRecord) {
        final Iterator<String> columnNamesItr = header.iterator();
        final Map<String, Object> map = new HashMap<>();
        for (final String columnValue : csvRecord) {
            map.put(columnNamesItr.next(), columnValue);
        }
        return map;
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

    public ParseCsv firstRow(final int firstRow) {
        this.firstRow = firstRow;
        return this;
    }

    public ParseCsv header(final String... header) {
        Collections.addAll(this.header, header);
        return this;
    }

    public ParseCsv header(final Collection<String> header) {
        this.header.addAll(header);
        return this;
    }

    public boolean isSkipInvalid() {
        return skipInvalid;
    }

    public void setSkipInvalid(final boolean skipInvalid) {
        this.skipInvalid = skipInvalid;
    }

    public ParseCsv skipInvalid() {
        this.skipInvalid = true;
        return this;
    }

    public ParseCsv skipInvalid(final boolean skipInvalid) {
        this.skipInvalid = skipInvalid;
        return this;
    }

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(final char delimiter) {
        this.delimiter = delimiter;
    }

    public ParseCsv delimiter(final char delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public boolean isQuoted() {
        return quoted;
    }

    public void setQuoted(final boolean quoted) {
        this.quoted = quoted;
    }

    public ParseCsv quoted() {
        this.quoted = true;
        return this;
    }

    public ParseCsv quoted(final boolean quoted) {
        this.quoted = quoted;
        return this;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    public void setQuoteChar(final char quoteChar) {
        this.quoteChar = quoteChar;
    }

    public ParseCsv quoteChar(final char quoteChar) {
        this.quoteChar = quoteChar;
        return this;
    }
}
