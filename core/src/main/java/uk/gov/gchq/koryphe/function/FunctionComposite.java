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

import com.fasterxml.jackson.annotation.JsonProperty;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.composite.Composite;

import java.util.List;
import java.util.function.Function;

/**
 * A {@link Composite} {@link Function} that applies each function in turn, supplying the result of each function
 * as the input of the next, and returning the result of the last function.
 *
 * @param <I> Input type
 * @param <O> Output type
 * @param <C> Type of BinaryOperator components
 */
@Since("1.0.0")
public class FunctionComposite<I, O, C extends Function> extends Composite<C> implements Function<I, O> {
    /**
     * Default - for serialisation.
     */
    public FunctionComposite() {
        super();
    }

    public FunctionComposite(final List<C> functions) {
        super(functions);
    }

    @JsonProperty("functions")
    public List<C> getComponents() {
        return super.getComponents();
    }

    /**
     * Apply the Function components in turn, returning the result of the last.
     *
     * @param input Input
     * @return Output
     */
    @Override
    public O apply(final I input) {
        Object result = input;
        for (final Function function : this.components) {
            // Assume the output of one is the input of the next
            result = function.apply(result);
        }
        return (O) result;
    }
}
