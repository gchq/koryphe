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

package uk.gov.gchq.koryphe.function;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import uk.gov.gchq.koryphe.adapted.Adapted;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An {@link Adapted} {@link Function}.
 *
 * @param <I>  Input type
 * @param <FI> Adapted input type for function
 * @param <FO> Function output to be adapted
 * @param <O>  Output type
 */
public abstract class AdaptedFunction<I, FI, FO, O> extends Adapted<I, FI, FO, O, I> implements Function<I, O> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    protected Function<FI, FO> function;

    /**
     * Default - for serialisation.
     */
    public AdaptedFunction() {
    }

    public AdaptedFunction(final Function<FI, FO> function,
                           final Function<I, FI> inputAdapter,
                           final BiFunction<I, FO, O> outputAdapter) {
        setInputAdapter(inputAdapter);
        setFunction(function);
        setOutputAdapter(outputAdapter);
    }

    public AdaptedFunction(final Function<FI, FO> function,
                           final Function<I, FI> inputAdapter,
                           final Function<FO, O> outputAdapter) {
        setInputAdapter(inputAdapter);
        setFunction(function);
        setOutputAdapter(outputAdapter);
    }

    /**
     * Apply the Function by adapting the input and outputs.
     *
     * @param input Input to adapt and apply function to
     * @return Adapted output
     */
    @Override
    public O apply(final I input) {
        return adaptOutput(function.apply(adaptInput(input)), input);
    }

    public Function<FI, FO> getFunction() {
        return function;
    }

    public void setFunction(final Function<FI, FO> function) {
        this.function = function;
    }
}
