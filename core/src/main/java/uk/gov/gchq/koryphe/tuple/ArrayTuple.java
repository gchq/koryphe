/*
 * Copyright 2017-2023 Crown Copyright
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
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.util.IterableUtil;

import java.util.Arrays;
import java.util.Iterator;

/**
 * An <code>ArrayTuple</code> is a simple implementation of the {@link Tuple} interface, backed by an
 * array of {@link Object}s, referenced by their index.
 */
public class ArrayTuple implements Tuple<Integer> {
    private final Object[] values;

    /**
     * Create an <code>ArrayTuple</code> backed by the given array.
     *
     * @param values Array backing this <code>ArrayTuple</code>.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This class is designed to simply wrap an object array.")
    public ArrayTuple(final Object... values) {
        this.values = values;
    }

    public ArrayTuple(final Iterable<?> values) {
        this(IterableUtil.toList(values).toArray());
    }

    /**
     * Create an <code>ArrayTuple</code> backed by a new array of the given size.
     *
     * @param size Size of array backing this <code>ArrayTuple</code>.
     */
    public ArrayTuple(final int size) {
        this.values = new Object[size];
    }

    /**
     * Get a value from this <code>ArrayTuple</code> at the given index.
     *
     * @param index Value index.
     * @return Value.
     */
    public Object get(final Integer index) {
        if (index < values.length) {
            return values[index];
        }

        return null;
    }

    /**
     * Put a value into this <code>ArrayTuple</code> at the given index.
     *
     * @param index Value index.
     * @param value Value to put.
     */
    public void put(final Integer index, final Object value) {
        if (index < values.length) {
            values[index] = value;
        }
    }

    @Override
    public Iterable<Object> values() {
        return Arrays.asList(values);
    }

    @Override
    public Iterator<Object> iterator() {
        return values().iterator();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final ArrayTuple concat = (ArrayTuple) obj;

        return new EqualsBuilder()
                .append(values, concat.values)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 31)
                .append(values)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("values", values)
                .build();
    }
}
