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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * An {@link java.util.function.BinaryOperator} that applies a {@link java.util.function.Function} to the input and output so that an aggregator can be applied
 * in a different context.
 *
 * @param <T>  Type of value to be aggregated
 * @param <FT> Type of value expected by aggregator
 */
public class AdaptedBinaryOperator<T, FT> implements BinaryOperator<T> {
    protected BinaryOperator<FT> function;
    protected Function<T, FT> inputAdapter;
    protected BiFunction<FT, T, T> outputAdapter;
    private Function<T, FT> reverseOutputAdapter;

    public AdaptedBinaryOperator() {
    }

    public AdaptedBinaryOperator(final BinaryOperator<FT> function,
                                 final Function<T, FT> inputAdapter,
                                 final BiFunction<FT, T, T> outputAdapter,
                                 final Function<T, FT> reverseOutputAdapter) {
        setFunction(function);
        setInputAdapter(inputAdapter);
        setOutputAdapter(outputAdapter);
        setReverseOutputAdapter(reverseOutputAdapter);
    }

    public AdaptedBinaryOperator(final BinaryOperator<FT> function,
                                 final Function<T, FT> inputAdapter,
                                 final Function<FT, T> outputAdapter,
                                 final Function<T, FT> reverseOutputAdapter) {
        this(function, inputAdapter, (fo, o) -> outputAdapter.apply(fo), reverseOutputAdapter);
    }

    @Override
    public T apply(final T input, final T state) {
        return adaptOutput(function.apply(adaptInput(input), reverseOutput(state)), state);
    }

    /**
     * Adapt the input value to the type expected by the function. If no input adapter has been specified, this method
     * assumes no transformation is required and simply casts the input to the transformed type.
     *
     * @param input Input to be transformed
     * @return Transformed input
     */
    protected FT adaptInput(final T input) {
        return inputAdapter == null ? (FT) input : inputAdapter.apply(input);
    }

    /**
     * Adapt the output value from the type produced by the function. If no output adapter has been specified, this
     * method assumes no transformation is required and simply casts the output to the transformed type.
     *
     * @param output the output to be transformed
     * @param state  the state of the transformation
     * @return Transformed output
     */
    protected T adaptOutput(final FT output, final T state) {
        return outputAdapter == null ? (T) output : outputAdapter.apply(output, state);
    }

    public void setInputAdapter(final Function<T, FT> inputAdapter) {
        this.inputAdapter = inputAdapter;
    }

    public Function<T, FT> getInputAdapter() {
        return inputAdapter;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public BinaryOperator<FT> getFunction() {
        return function;
    }

    public void setFunction(final BinaryOperator<FT> function) {
        this.function = function;
    }

    public void setOutputAdapter(final BiFunction<FT, T, T> outputAdapter) {
        this.outputAdapter = outputAdapter;
    }

    public BiFunction<FT, T, T> getOutputAdapter() {
        return outputAdapter;
    }

    public Function<T, FT> getReverseOutputAdapter() {
        return reverseOutputAdapter;
    }

    public void setReverseOutputAdapter(final Function<T, FT> outputReverse) {
        this.reverseOutputAdapter = outputReverse;
    }

    private FT reverseOutput(final T input) {
        return reverseOutputAdapter == null ? (FT) input : reverseOutputAdapter.apply(input);
    }
}
