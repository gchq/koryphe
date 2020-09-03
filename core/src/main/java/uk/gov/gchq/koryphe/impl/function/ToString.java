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
package uk.gov.gchq.koryphe.impl.function;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.common.base.Charsets;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.nio.charset.Charset;
import java.util.Arrays;

import static java.util.Objects.nonNull;

/**
 * A <code>ToString</code> is a {@link java.util.function.Function} that takes in
 * an object (null or otherwise), and calls toString on it.
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Since("1.0.0")
@Summary("Calls ToString on an object")
public class ToString extends KorypheFunction<Object, String> {
    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;

    private Charset charset;

    public ToString() {
        setCharset(DEFAULT_CHARSET);
    }

    public ToString(final String charsetString) {
        setCharset(Charset.forName(charsetString));
    }

    public ToString(final Charset charset) {
        setCharset(charset);
    }

    @Override
    public String apply(final Object o) {
        if (null == o) {
            return null;
        }
        if (o instanceof byte[]) {
            return new String(((byte[]) o), charset);
        }
        if (o instanceof Object[]) {
            return Arrays.toString((Object[]) o);
        }
        return String.valueOf(o);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.classEquals(o)) {
            return false; // Does exact equals and class checking
        }

        ToString that = (ToString) o;
        return new EqualsBuilder()
                .append(charset, that.charset)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(43, 67)
                .appendSuper(super.hashCode())
                .append(charset)
                .toHashCode();
    }
}
