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
 * @param <O_ITEM> the type of items in the iterator
 */
public class MappedIterator<I_ITEM, O_ITEM> implements Closeable, Iterator<O_ITEM> {
    private final Iterator<? extends I_ITEM> iterator;
    private final List<Function> functions;

    public MappedIterator(final Iterator<I_ITEM> iterator, final List<Function> functions) {
        if (null == iterator) {
            throw new IllegalArgumentException("iterator is required");
        }

        this.iterator = iterator;
        this.functions = functions;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public O_ITEM next() {
        Object item = iterator.next();
        try {
            for (final Function function : functions) {
                item = function.apply(item);
            }
            return (O_ITEM) item;
        } catch (final ClassCastException c) {
            throw new IllegalArgumentException("The input/output types of the functions were incompatible", c);
        }
    }

    @Override
    public void close() {
        CloseableUtil.close(iterator);
    }
}
