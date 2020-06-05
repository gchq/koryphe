/*
 * Copyright 2020 Crown Copyright
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
import uk.gov.gchq.koryphe.tuple.function.KorypheFunction2;

/**
 * A {@code Longest} is a {@link java.util.function.Function} which takes two inputs and returns the longest of the two.
 * @param <T> the type of input objects
 */
@Since("1.9.0")
@Summary("Determines which of two input objects is the longest.")
public class Longest<T> extends KorypheFunction2<T, T, T> {

    private final Length delegate = new Length();

    public Longest() {
    }

    @Override
    public T apply(final T first, final T second) {
        final int firstLength = delegate.apply(first);
        final int secondLength = delegate.apply(second);

        return (firstLength - secondLength) > 0 ? first : second;
    }
}
