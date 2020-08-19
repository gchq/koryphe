/*
 * Copyright 2018-2020 Crown Copyright
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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

/**
 * A <code>SetValue</code> is a {@link java.util.function.Function} that takes
 * an input object and returns a set value.
 * <p>
 * The resulting object is what is returned from the method.
 */
@Since("1.5.0")
@Summary("Returns a set value from a given Object")
public class SetValue extends KorypheFunction<Object, Object> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    private Object value;

    public SetValue() {
    }

    public SetValue(final Object returnValue) {
        this.value = returnValue;
    }

    @Override
    public Object apply(final Object o) {
        return value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(final Object identity) {
        this.value = identity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.equals(o)) {
            return false; // Does exact equals and class checking
        }

        SetValue that = (SetValue) o;
        return new EqualsBuilder()
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 47)
                .appendSuper(super.hashCode())
                .append(value)
                .toHashCode();
    }
}
