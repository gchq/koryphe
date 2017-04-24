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

package uk.gov.gchq.koryphe.tuple.function;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.gov.gchq.koryphe.function.AdaptedFunction;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.TupleInputAdapter;
import uk.gov.gchq.koryphe.tuple.TupleOutputAdapter;
import java.util.function.Function;

/**
 * A <code>TupleAdaptedFunction</code> adapts a {@link Function} so it can be applied to selected
 * fields from a {@link Tuple}, projecting it's output back into the tuple.
 *
 * @see TupleInputAdapter
 * @see TupleOutputAdapter
 *
 * @param <R> Reference type used by tuples
 * @param <FI> Input type of the Function
 * @param <FO> Output type of the Function
 */
public final class TupleAdaptedFunction<R, FI, FO> extends AdaptedFunction<Tuple<R>, FI, FO, Tuple<R>> {
    /**
     * Default - for serialisation.
     */
    public TupleAdaptedFunction() {
        setInputAdapter(new TupleInputAdapter<>());
        setOutputAdapter(new TupleOutputAdapter<>());
    }

    public TupleAdaptedFunction(final Function<FI, FO> function) {
        this();
        setFunction(function);
    }

    public R[] getSelection() {
        return getInputAdapter().getSelection();
    }

    @SafeVarargs
    public final void setSelection(final R... selection) {
        getInputAdapter().setSelection(selection);
    }

    public R[] getProjection() {
        return getOutputAdapter().getProjection();
    }

    @SafeVarargs
    public final void setProjection(final R... fields) {
        getOutputAdapter().setProjection(fields);
    }

    @JsonIgnore
    @Override
    public TupleInputAdapter<R, FI> getInputAdapter() {
        return (TupleInputAdapter<R, FI>) super.getInputAdapter();
    }

    @JsonIgnore
    @Override
    public TupleOutputAdapter<R, FO> getOutputAdapter() {
        return (TupleOutputAdapter<R, FO>) super.getOutputAdapter();
    }
}
