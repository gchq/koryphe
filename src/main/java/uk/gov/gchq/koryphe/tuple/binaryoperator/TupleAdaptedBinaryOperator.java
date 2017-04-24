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

package uk.gov.gchq.koryphe.tuple.binaryoperator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.gov.gchq.koryphe.binaryoperator.AdaptedBinaryOperator;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.TupleInputAdapter;
import uk.gov.gchq.koryphe.tuple.TupleOutputAdapter;
import java.util.function.BinaryOperator;

/**
 * A <code>TupleAdaptedBinaryOperator</code> adapts a {@link BinaryOperator} so it can be applied to selected
 * fields from a {@link Tuple}.
 *
 * @see TupleInputAdapter
 * @see TupleOutputAdapter
 *
 * @param <R> Reference type used by tuples.
 * @param <OT> Input/Output type of the BinaryOperator
 */
public final class TupleAdaptedBinaryOperator<R, OT> extends AdaptedBinaryOperator<Tuple<R>, OT> {
    /**
     * Default - for serialisation.
     */
    public TupleAdaptedBinaryOperator() {
        setInputAdapter(new TupleInputAdapter<>());
        setOutputAdapter(new TupleOutputAdapter<>());
    }

    @SafeVarargs
    public TupleAdaptedBinaryOperator(final BinaryOperator<OT> binaryOperator, final R... selection) {
        this();
        setBinaryOperator(binaryOperator);
        setSelection(selection);
    }

    public R[] getSelection() {
        return getInputAdapter().getSelection();
    }

    @SafeVarargs
    public final void setSelection(final R... selection) {
        getInputAdapter().setSelection(selection);
        getOutputAdapter().setProjection(selection);
    }

    @JsonIgnore
    @Override
    public TupleInputAdapter<R, OT> getInputAdapter() {
        return (TupleInputAdapter<R, OT>) super.getInputAdapter();
    }

    @JsonIgnore
    @Override
    public TupleOutputAdapter<R, OT> getOutputAdapter() {
        return (TupleOutputAdapter<R, OT>) super.getOutputAdapter();
    }
}
