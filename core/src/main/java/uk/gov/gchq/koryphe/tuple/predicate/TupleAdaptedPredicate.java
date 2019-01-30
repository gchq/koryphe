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

package uk.gov.gchq.koryphe.tuple.predicate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.predicate.AdaptedPredicate;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.TupleInputAdapter;

import java.util.function.Predicate;

/**
 * A <code>TupleAdaptedPredicate</code> adapts a {@link Predicate} so it can be applied to selected
 * fields from a {@link Tuple}.
 *
 * @param <R>  Reference type used by tuples
 * @param <PI> Input type of the Predicate
 * @see TupleInputAdapter
 */
@JsonPropertyOrder(value = {"class", "selection", "predicate"}, alphabetic = true)
@Since("1.0.0")
@Summary("Applies a predicate and adapts the input")
public class TupleAdaptedPredicate<R, PI> extends AdaptedPredicate<Tuple<R>, PI> {
    /**
     * Default constructor - for serialisation.
     */
    public TupleAdaptedPredicate() {
        setInputAdapter(new TupleInputAdapter<>());
    }

    public TupleAdaptedPredicate(final Predicate<PI> predicate, final R[] selection) {
        this();
        setPredicate(predicate);
        setSelection(selection);
    }

    public R[] getSelection() {
        return getInputAdapter().getSelection();
    }

    public void setSelection(final R[] selection) {
        getInputAdapter().setSelection(selection);
    }

    @JsonIgnore
    @Override
    public TupleInputAdapter<R, PI> getInputAdapter() {
        return (TupleInputAdapter<R, PI>) super.getInputAdapter();
    }
}
