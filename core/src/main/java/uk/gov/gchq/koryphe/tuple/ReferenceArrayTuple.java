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

package uk.gov.gchq.koryphe.tuple;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.tuple.n.Tuple5;

import java.util.Iterator;

public class ReferenceArrayTuple<R> extends Tuple5 {
    private final R[] fields;
    private final Tuple<R> tuple;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Cloning the array would be expensive - we will have to reply on users not modifying the array")
    public ReferenceArrayTuple(final Tuple<R> tuple, final R[] fields) {
        this.tuple = tuple;
        this.fields = fields;
    }

    @Override
    public Object get(final Integer index) {
        if (null != tuple && index < fields.length) {
            return tuple.get(fields[index]);
        }

        return null;
    }

    @Override
    public void put(final Integer index, final Object value) {
        if (null != tuple && index < fields.length) {
            final R field = fields[index];
            tuple.put(field, value);
        }
    }

    @Override
    public Iterable<Object> values() {
        final ArrayTuple selected = new ArrayTuple(fields.length);
        for (int i = 0; i < fields.length; i++) {
            selected.put(i, get(i));
        }
        return selected;
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

        final ReferenceArrayTuple concat = (ReferenceArrayTuple) obj;

        return new EqualsBuilder()
                .append(fields, concat.fields)
                .append(tuple, concat.tuple)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 31)
                .append(fields)
                .append(tuple)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fields", fields)
                .append("tuple", tuple)
                .build();
    }
}
