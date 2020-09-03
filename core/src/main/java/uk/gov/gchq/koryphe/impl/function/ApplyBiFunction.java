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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.WrappedBiFunction;
import uk.gov.gchq.koryphe.tuple.function.KorypheFunction2;

import java.util.function.BiFunction;

import static java.util.Objects.nonNull;

/**
 * A {@link KorypheFunction2} that applies a given {@link BiFunction} (or {@link java.util.function.BinaryOperator}).
 * <p>
 * This class is simply a wrapped around a {@link BiFunction} to allow you to execute it as a {@link KorypheFunction2}.
 *
 * @param <T> Type of first input
 * @param <U> Type of second input
 * @param <R> Type of output
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
@Since("1.8.0")
@Summary("Applies the given BiFunction")
public class ApplyBiFunction<T, U, R> extends KorypheFunction2<T, U, R> implements WrappedBiFunction<T, U, R> {
    private BiFunction<T, U, R> function;

    public ApplyBiFunction() {
    }

    public ApplyBiFunction(final BiFunction<T, U, R> function) {
        this.function = function;
    }

    @Override
    public R apply(final T t, final U u) {
        return nonNull(function) ? function.apply(t, u) : null;
    }

    @Override
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public BiFunction<T, U, R> getFunction() {
        return function;
    }

    public void setFunction(final BiFunction<T, U, R> function) {
        this.function = function;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.classEquals(o)) {
            return false; // Does class checking
        }

        ApplyBiFunction<?, ?, ?> that = (ApplyBiFunction<?, ?, ?>) o;
        return new EqualsBuilder()
                .append(function, that.function)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 89)
                .appendSuper(super.hashCode())
                .append(function)
                .toHashCode();
    }
}
