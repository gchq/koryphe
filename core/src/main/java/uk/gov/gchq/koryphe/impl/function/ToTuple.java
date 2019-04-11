/*
 * Copyright 2018-2019 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.function;

import org.apache.commons.lang3.ArrayUtils;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.ReflectiveTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.util.Map;

import static java.util.Objects.isNull;

/**
 * A <code>ToTuple</code> is a {@link java.util.function.Function} that takes
 * an object and converts it into a {@link Tuple} for follow on processing.
 */
@Since("1.7.0")
@Summary("Converts an Object into a Tuple")
public class ToTuple extends KorypheFunction<Object, Tuple<?>> {
    @Override
    public Tuple<?> apply(final Object value) {
        if (isNull(value)) {
            return null;
        }
        if (value instanceof Tuple) {
            return (Tuple<?>) value;
        }

        if (value instanceof Map) {
            return new MapTuple<>(((Map<?, Object>) value));
        }

        if (value instanceof Object[]) {
            return new ArrayTuple(((Object[]) value));
        }

        if (value.getClass().isArray()) {
            if (value instanceof int[]) {
                return new ArrayTuple(ArrayUtils.toObject((int[]) value));
            }
            if (value instanceof double[]) {
                return new ArrayTuple(ArrayUtils.toObject((double[]) value));
            }
            if (value instanceof long[]) {
                return new ArrayTuple(ArrayUtils.toObject((long[]) value));
            }
            if (value instanceof float[]) {
                return new ArrayTuple(ArrayUtils.toObject((float[]) value));
            }
            if (value instanceof short[]) {
                return new ArrayTuple(ArrayUtils.toObject((short[]) value));
            }
            if (value instanceof boolean[]) {
                return new ArrayTuple(ArrayUtils.toObject((boolean[]) value));
            }
            if (value instanceof byte[]) {
                return new ArrayTuple(ArrayUtils.toObject((byte[]) value));
            }
            if (value instanceof char[]) {
                return new ArrayTuple(ArrayUtils.toObject((char[]) value));
            }
        }

        if (value instanceof Iterable) {
            return new ArrayTuple((Iterable) value);
        }

        return new ReflectiveTuple(value);
    }
}
