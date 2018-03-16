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

import com.fasterxml.jackson.annotation.JsonProperty;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.composite.Composite;

import java.util.List;
import java.util.function.BinaryOperator;

/**
 * A {@link Composite} {@link BinaryOperator} that applies each operator in turn, supplying the result of each operator
 * as the state of the next, and returning the result of the last operator.
 *
 * @param <T> Input/output type
 * @param <C> Type of BinaryOperator components
 */
@Since("1.0.0")
public class BinaryOperatorComposite<T, C extends BinaryOperator<T>> extends Composite<C> implements BinaryOperator<T> {
    /**
     * Default - for serialisation.
     */
    public BinaryOperatorComposite() {
        super();
    }

    public BinaryOperatorComposite(final List<C> binaryOperators) {
        super(binaryOperators);
    }

    @JsonProperty("operators")
    public List<C> getComponents() {
        return super.getComponents();
    }

    /**
     * Apply the BinaryOperator components in turn, returning the output of the last.
     *
     * @param state Value to fold into
     * @param input New input to fold in
     * @return New state
     */
    @Override
    public T apply(final T state, final T input) {
        T result = state;
        for (final BinaryOperator<T> component : this.components) {
            result = component.apply(result, input);
        }
        return result;
    }
}
