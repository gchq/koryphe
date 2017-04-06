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

package uk.gov.gchq.koryphe.binaryoperator;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

/**
 * Applies an {@link java.util.function.BinaryOperator} to the values of an input {@link java.util.Map}, updating the output {@link java.util.Map} with the current
 * state.
 *
 * @param <K> Type of key
 * @param <T> Type of input/output value
 */
public class BinaryOperatorMap<K, T> implements BinaryOperator<Map<K, T>> {

    private BinaryOperator<T> function;

    public BinaryOperatorMap() {
    }

    public BinaryOperatorMap(final BinaryOperator<T> function) {
        setFunction(function);
    }

    public void setFunction(final BinaryOperator<T> function) {
        this.function = function;
    }

    public BinaryOperator<T> getFunction() {
        return function;
    }

    @Override
    public Map<K, T> apply(final Map<K, T> input, final Map<K, T> state) {
        if (input == null) {
            return state;
        } else {
            Map<K, T> output = state == null ? new HashMap<>() : state;
            for (final Map.Entry<K, T> entry : input.entrySet()) {
                T currentState = output.get(entry.getKey());
                output.put(entry.getKey(), function.apply(entry.getValue(), currentState));
            }
            return output;
        }
    }
}