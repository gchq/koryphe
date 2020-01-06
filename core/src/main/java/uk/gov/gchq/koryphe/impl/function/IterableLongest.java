/*
 * Copyright 2019 Crown Copyright
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

import static java.util.Objects.nonNull;

/**
 * An {@code IterableLongest} is a {@link java.util.function.Function} which evaluates the length of each item in the
 * input Iterable and returns the longest item.
 * <p>
 * The {@link Length} function is used to determine the length of each item in the iterable.
 */
@Since("1.8.2")
@Summary("Returns the longest item in the provided iterable.")
public class IterableLongest extends KorypheFunction<Iterable<?>, Object> {

    private final Length delegate = new Length();

    public IterableLongest() {
        delegate.setMaxLength(Integer.MAX_VALUE);
    }

    @Override
    public Object apply(final Iterable<?> items) {
        if (nonNull(items)) {
            return getLongest(items);
        }

        return null;
    }

    private Object getLongest(final Iterable<?> items) {
        Object longest = null;
        int maxLength = 0;

        for (final Object obj : items) {
            final int length = getLength(obj);

            if (length > maxLength) {
                longest = obj;
                maxLength = length;
            }
        }

        return longest;
    }

    private int getLength(final Object value) {
        return delegate.apply(value);
    }
}
