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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A {@code ToList} is a {@link java.util.function.Function} that takes
 * an Object and converts it to a list. If the object is an array or iterable
 * the items will be added to a new list. Otherwise a new list is created with
 * the single value as an item.
 */
@Since("1.6.0")
@Summary("Converts an object into a List")
public class ToList extends KorypheFunction<Object, List<?>> {

    @Override
    public List<?> apply(final Object value) {
        if (null == value) {
            return Arrays.asList((Object) null);
        }

        if (value instanceof Object[]) {
            return new ArrayList<>(Arrays.asList((Object[]) value));
        }

        if (value instanceof Iterable) {
            return IterableUtil.toList((Iterable) value);
        }

        return new ArrayList<>(Arrays.asList(value));
    }
}
