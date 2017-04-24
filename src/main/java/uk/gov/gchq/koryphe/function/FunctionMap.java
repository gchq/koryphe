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

package uk.gov.gchq.koryphe.function;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Applies a {@link Function} to the values of an input {@link java.util.Map}, to produce a new output map.
 *
 * @param <K> Type of key
 * @param <I> Input type
 * @param <O> Output type
 */
public class FunctionMap<K, I, O> implements Function<Map<K, I>, Map<K, O>> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    private Function<I, O> function;

    /**
     * Default - for serialisation.
     */
    public FunctionMap() {
    }

    public FunctionMap(final Function<I, O> function) {
        setFunction(function);
    }

    public void setFunction(final Function<I, O> function) {
        this.function = function;
    }

    public Function<I, O> getFunction() {
        return function;
    }

    /**
     * Iterate through the values of an input map, applying the wrapped <code>Function</code>.
     *
     * @param input Input map.
     * @return Output map.
     */
    @Override
    public Map<K, O> apply(final Map<K, I> input) {
        if (input == null) {
            return null;
        } else {
            final Map<K, O> output = new HashMap<>(input.size());
            for (final Map.Entry<K, I> entry : input.entrySet()) {
                output.put(entry.getKey(), function.apply(entry.getValue()));
            }
            return output;
        }
    }
}
