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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.serialisation.json.SimpleClassNameIdResolver;

import java.io.IOException;
import java.io.Serializable;

import static java.util.Objects.isNull;

@Since("1.8.0")
@Summary("Parses a JSON string in java objects")
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class DeserialiseJson<T> extends KorypheFunction<String, T> implements Serializable {
    private static final long serialVersionUID = 5432036264979648528L;
    private static final ObjectMapper MAPPER = createObjectMapper();

    private Class<T> outputClass;

    public DeserialiseJson() {
        this((Class<T>) Object.class);
    }

    public DeserialiseJson(final Class<T> outputClass) {
        setOutputClass(outputClass);
    }

    @Override
    public T apply(final String json) {
        if (isNull(json)) {
            return null;
        }

        try {
            return MAPPER.readValue(json, outputClass);
        } catch (final IOException e) {
            throw new RuntimeException("Failed to deserialise JSON", e);
        }
    }

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        SimpleClassNameIdResolver.configureObjectMapper(mapper);
        return mapper;
    }

    public Class<T> getOutputClass() {
        return outputClass;
    }

    public DeserialiseJson<T> outputClass(final Class<T> outputClass) {
        setOutputClass(outputClass);
        return this;
    }

    public void setOutputClass(final Class<T> outputClass) {
        if (isNull(outputClass)) {
            this.outputClass = (Class<T>) Object.class;
        } else {
            this.outputClass = outputClass;
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (!super.equals(o)) {
            return false; // Does exact equals and Class checking
        }

        DeserialiseJson that = (DeserialiseJson) o;
        return new EqualsBuilder()
                .append(outputClass, that.outputClass)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(43, 31)
                .append(super.hashCode())
                .append(outputClass)
                .toHashCode();
    }
}
