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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.base.Charsets;
import org.apache.commons.codec.binary.Base64;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.nio.charset.Charset;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * A <code>Base64Decode</code> is a {@link java.util.function.Function} that takes
 * a Base 64 encoded string and decodes it into a String using the provided charset.
 * The default charset is UTF-8.
 */
@Since("1.6.0")
@Summary("Decodes a base64 encoded string")
public class Base64Decode extends KorypheFunction<String, String> {
    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Charset charset;

    public Base64Decode() {
        setCharset(DEFAULT_CHARSET);
    }

    public Base64Decode(final String charsetString) {
        setCharset(Charset.forName(charsetString));
    }

    public Base64Decode(final Charset charset) {
        setCharset(charset);
    }

    @Override
    public String apply(final String base64Encoded) {
        if (isNull(base64Encoded)) {
            return null;
        }

        final byte[] bytes = Base64.decodeBase64(base64Encoded);
        return new String(bytes, charset);
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
