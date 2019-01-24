/*
 * Copyright 2018 Crown Copyright
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

import com.google.common.collect.Sets;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.util.Set;

/**
 * A {@code ToSet} is a {@link java.util.function.Function} that takes
 * an Object and converts it to a set. If the object is an array or iterable
 * the items will be added to a new set. Otherwise a new set is created with
 * the single value as an item.
 */
@Since("1.6.0")
@Summary("Converts an object into a Set")
public class ToSet extends KorypheFunction<Object, Set<?>> {

    @Override
    public Set<?> apply(final Object value) {
        if (null == value) {
            return Sets.newHashSet((Object) null);
        }

        if (value instanceof Object[]) {
            return Sets.newHashSet((Object[]) value);
        }

        if (value instanceof Iterable) {
            if (value instanceof Set) {
                return (Set<?>) value;
            }
            return Sets.newHashSet((Iterable) value);
        }

        return Sets.newHashSet(value);
    }
}
