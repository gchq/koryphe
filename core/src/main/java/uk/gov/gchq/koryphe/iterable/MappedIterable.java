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
import java.util.List;
import java.util.function.Function;

/**
 * A {@code MappedIterable} is a {@link java.io.Closeable}
 * {@link java.lang.Iterable} which applied a {@link java.util.List}
 * of {@link java.util.function.Function}s to an {@link java.lang.Iterable}.
 *
 * @param <I_ITEM> input type of items in the input iterator
 * @param <O_ITEM> output type of items in the output iterator
 */
public class MappedIterable<I_ITEM, O_ITEM> implements Closeable, Iterable<O_ITEM> {
    private final Iterable<I_ITEM> iterable;
    private final List<Function> functions;

    public MappedIterable(final Iterable<I_ITEM> iterable, final List<Function> functions) {
        if (null == iterable) {
            throw new IllegalArgumentException("iterable is required");
        }

        this.iterable = iterable;
        this.functions = functions;
    }

    @Override
    public Iterator<O_ITEM> iterator() {
        return new MappedIterator<>(iterable.iterator(), functions);
    }

    @Override
    public void close() {
        CloseableUtil.close(iterable);
    }
}
