/*
 * Copyright 2017-2019 Crown Copyright
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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.base.Charsets;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.nio.charset.Charset;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * A <code>ToBytes</code> is a {@link java.util.function.Function} that takes in
 * a string and extracts the bytes using the provided charset.
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Since("1.8.0")
@Summary("Extracts the bytes from a string")
public class ToBytes extends KorypheFunction<String, byte[]> {
    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Charset charset;

    public ToBytes() {
        setCharset(DEFAULT_CHARSET);
    }

    public ToBytes(final String charsetString) {
        setCharset(Charset.forName(charsetString));
    }

    public ToBytes(final Charset charset) {
        setCharset(charset);
    }

    @SuppressFBWarnings(value = "PZLA_PREFER_ZERO_LENGTH_ARRAYS", justification = "Returning null means the input was null")
    @Override
    public byte[] apply(final String string) {
        if (isNull(string)) {
            return null;
        }
        return string.getBytes(charset);
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(final Charset charset) {
        this.charset = nonNull(charset) ? charset : DEFAULT_CHARSET;
    }

    @JsonSetter("charset")
    public void setCharset(final String charsetString) {
        setCharset(nonNull(charsetString) ? Charset.forName(charsetString) : DEFAULT_CHARSET);
    }

    @JsonGetter("charset")
    public String getCharsetAsString() {
        return charset.name();
    }
}
