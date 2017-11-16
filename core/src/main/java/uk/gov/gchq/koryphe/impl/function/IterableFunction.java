/*
 * Copyright 2017 Crown Copyright
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
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.util.IterableUtil;

import java.util.function.Function;

/**
 * An {@code IterableFunction} is a {@link KorypheFunction} which lazily applies a
 * supplied {@link Function} to each object in the input {@link Iterable}, returning
 * an output {@link Iterable}.
 * @param <I_ITEM>  the type of objects in the input iterable
 * @param <O_ITEM>  the type of objects in the output iterable
 */
public class IterableFunction<I_ITEM, O_ITEM> extends KorypheFunction<Iterable<I_ITEM>, Iterable<O_ITEM>> {
    private Function<I_ITEM, O_ITEM> delegateFunction;

    public IterableFunction() {
        // empty
    }

    public IterableFunction(final Function<I_ITEM, O_ITEM> delegateFunction) {
        this.delegateFunction = delegateFunction;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public Function<I_ITEM, O_ITEM> getDelegateFunction() {
        return delegateFunction;
    }

    public void setDelegateFunction(final Function<I_ITEM, O_ITEM> func) {
        this.delegateFunction = func;
    }

    @Override
    public Iterable<O_ITEM> apply(final Iterable<I_ITEM> items) {
        return IterableUtil.applyFunction(items, delegateFunction);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }

        final IterableFunction func = (IterableFunction) obj;

        return new EqualsBuilder()
                .append(delegateFunction, func.delegateFunction)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 71)
                .append(delegateFunction)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("delegateFunction", delegateFunction)
                .toString();
    }
}