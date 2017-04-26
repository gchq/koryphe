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
import uk.gov.gchq.koryphe.adapted.Adapted;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * An {@link Adapted} {@link BinaryOperator}.
 *
 * @param <T> Input/Output type
 * @param <OT> Input/Output type of the BinaryOperator being applied
 */
public class AdaptedBinaryOperator<T, OT> extends Adapted<T, OT, OT, T, T> implements BinaryOperator<T> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    protected BinaryOperator<OT> binaryOperator;

    /**
     * Default - for serialisation
     */
    public AdaptedBinaryOperator() {
    }

    public AdaptedBinaryOperator(final BinaryOperator<OT> binaryOperator,
                                 final Function<T, OT> inputAdapter,
                                 final BiFunction<T, OT, T> outputAdapter) {
        setBinaryOperator(binaryOperator);
        setInputAdapter(inputAdapter);
        setOutputAdapter(outputAdapter);
    }

    public AdaptedBinaryOperator(final BinaryOperator<OT> binaryOperator,
                                 final Function<T, OT> inputAdapter,
                                 final Function<OT, T> outputAdapter) {
        setBinaryOperator(binaryOperator);
        setInputAdapter(inputAdapter);
        setOutputAdapter(outputAdapter);
    }

    /**
     * Apply the BinaryOperator by adapting the input and outputs.
     *
     * @param state Value to fold into
     * @param input New input to fold in
     * @return New state
     */
    @Override
    public T apply(final T state, final T input) {
        return adaptOutput(binaryOperator.apply(adaptInput(state), adaptInput(input)), state);
    }

    public BinaryOperator<OT> getBinaryOperator() {
        return binaryOperator;
    }

    public void setBinaryOperator(final BinaryOperator<OT> binaryOperator) {
        this.binaryOperator = binaryOperator;
    }
}
