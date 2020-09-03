/*
 * Copyright 2020 Crown Copyright
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

/**
 * A {@code DefaultIfNull} is a {@link java.util.function.Function} which supplies a default value if the input is null.
 */
@Since("1.9.0")
@Summary("Provides a default value if the input is null.")
public class DefaultIfNull extends KorypheFunction<Object, Object> {

    private Object defaultValue;

    public DefaultIfNull() {
    }

    public DefaultIfNull(final Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Object apply(final Object input) {
        return null == input ? defaultValue : input;
    }

    public void setDefaultValue(final Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.classEquals(o)) {
            return false; // Does class checking
        }

        DefaultIfNull that = (DefaultIfNull) o;
        return new EqualsBuilder()
                .append(defaultValue, that.defaultValue)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(5, 51)
                .appendSuper(super.hashCode())
                .append(defaultValue)
                .toHashCode();
    }
}
