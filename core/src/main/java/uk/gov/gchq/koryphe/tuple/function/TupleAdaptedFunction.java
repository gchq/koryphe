/*
 * Copyright 2017-2019 Crown Copyright
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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.AdaptedFunction;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.TupleInputAdapter;
import uk.gov.gchq.koryphe.tuple.TupleOutputAdapter;

import java.util.function.Function;

/**
 * A <code>TupleAdaptedFunction</code> adapts a {@link Function} so it can be applied to selected
 * fields from a {@link Tuple}, projecting it's output back into the tuple.
 *
 * @param <R>  Reference type used by tuples
 * @param <FI> Input type of the Function
 * @param <FO> Output type of the Function
 * @see TupleInputAdapter
 * @see TupleOutputAdapter
 */
@JsonPropertyOrder(value = {"class", "selection", "function", "projection"}, alphabetic = true)
@Since("1.0.0")
@Summary("Applies a function and adapts the input/output")
public class TupleAdaptedFunction<R, FI, FO> extends AdaptedFunction<Tuple<R>, FI, FO, Tuple<R>> {
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

    public TupleAdaptedFunction(final R[] selection, final Function<FI, FO> function, final R[] projection) {
        this(function);
        setSelection(selection);
        setProjection(projection);
    }

    public R[] getSelection() {
        return getInputAdapter().getSelection();
    }

    public void setSelection(final R[] selection) {
        getInputAdapter().setSelection(selection);
    }

    public R[] getProjection() {
        return getOutputAdapter().getProjection();
    }

    public void setProjection(final R[] fields) {
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
