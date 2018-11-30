/*
 * Copyright 2017-2018 Crown Copyright
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

package uk.gov.gchq.koryphe.binaryoperator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.function.BinaryOperator;

/**
 * Abstract superclass provided for convenience.
 *
 * @param <T> Input/Output type
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public abstract class KorypheBinaryOperator<T> implements BinaryOperator<T> {
    /**
     * Apply the operator after completing null checks.
     *
     * @param state Current state
     * @param input New input
     * @return New state
     */
    @Override
    public T apply(final T state, final T input) {
        if (null == state) {
            return input;
        }

        if (null == input) {
            return state;
        }

        return _apply(state, input);
    }

    protected abstract T _apply(final T a, final T b);

    @Override
    public boolean equals(final Object obj) {
        return this == obj || classEquals(obj);
    }

    protected boolean classEquals(final Object other) {
        return null != other && getClass().equals(other.getClass());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
