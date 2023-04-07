/*
 * Copyright 2018-2020 Crown Copyright
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

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.util.IterableUtil;

/**
 * A {@code ToArray} is a {@link java.util.function.Function} that takes
 * an Object and converts it to a array. If the object is already an array then it is just returned.
 * If it is an iterable then the items will be added to a new array. Otherwise a new array is created with
 * the single value as an item.
 */
@Since("1.6.0")
@Summary("Converts an object into a Array")
public class ToArray extends KorypheFunction<Object, Object[]> {

    @Override
    public Object[] apply(final Object value) {
        if (null == value) {
            return new Object[]{null};
        }

        if (value instanceof Object[]) {
            return ((Object[]) value);
        }

        if (value instanceof Iterable) {
            return IterableUtil.toList(((Iterable) value)).toArray();
        }

        return new Object[]{value};
    }
}
