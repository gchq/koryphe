/*
 * Copyright 2022 Crown Copyright
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

package uk.gov.gchq.koryphe.iterable;

import uk.gov.gchq.koryphe.util.CloseableUtil;

import java.io.Closeable;
import java.util.Iterator;

/**
 * @param <T> the type of items in the iterator
 */
public class ChainedIterable<T> implements Closeable, Iterable<T> {
    private final Iterable<? extends Iterable<? extends T>> iterables;

    public ChainedIterable(final Iterable<? extends Iterable<? extends T>> iterables) {
        if (null == iterables) {
            throw new IllegalArgumentException("iterables are required");
        }
        this.iterables = iterables;
    }

    @Override
    public Iterator<T> iterator() {
        return new ChainedIterator<>(iterables.iterator());
    }

    @Override
    public void close() {
        for (final Iterable<? extends T> iterable : iterables) {
            CloseableUtil.close(iterable);
        }
    }
}
