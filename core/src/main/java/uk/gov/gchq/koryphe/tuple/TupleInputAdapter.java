/*
 * Copyright 2017-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.tuple;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.util.Arrays;

import static uk.gov.gchq.koryphe.util.JavaUtils.requireNonNullElse;

/**
 * @param <R>  The type of reference used by tuples.
 * @param <FI> The adapted input type.
 */
@Since("1.0.0")
@Summary("Extracts items from a tuple")
public class TupleInputAdapter<R, FI> extends KorypheFunction<Tuple<R>, FI> {
    private R[] selection;

    /**
     * Create a new <code>TupleMask</code>.
     */
    public TupleInputAdapter() {
        selection = (R[]) new Object[0];
    }

    /**
     * Create a new <code>TupleMask</code> with the given field references.
     *
     * @param selection Field references.
     */
    public TupleInputAdapter(final R[] selection) {
        setSelection(selection);
    }

    @Override
    public FI apply(final Tuple<R> input) {
        if (null == selection) {
            throw new IllegalArgumentException("Selection is required");
        }

        if (null != input) {
            if (1 == selection.length) {
                return (FI) input.get(selection[0]);
            }
        }

        return (FI) new ReferenceArrayTuple<>(input, selection);
    }

    /**
     * @return Field references.
     */
    public R[] getSelection() {
        return Arrays.copyOf(selection, selection.length);
    }

    /**
     * Set this <code>TupleMask</code> to refer to a tuple of field references.
     *
     * @param selection Field references.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Cloning the array would be expensive - we will have to reply on users not modifying the array")
    public void setSelection(final R[] selection) {
        this.selection = requireNonNullElse(selection, (R[]) new Object[0]);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.equals(o)) {
            return false; // Does class checking
        }

        final TupleInputAdapter that = (TupleInputAdapter) o;
        return new EqualsBuilder()
                .append(selection, that.selection)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(43, 67)
                .appendSuper(super.hashCode())
                .append(selection)
                .toHashCode();
    }
}
