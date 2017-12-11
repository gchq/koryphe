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
package uk.gov.gchq.koryphe.util;

import com.google.common.collect.Iterators;

import uk.gov.gchq.koryphe.iterable.CloseableIterable;
import uk.gov.gchq.koryphe.iterable.CloseableIterator;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * An {@code IterableUtil} is a utility class for lazily applying a {@link Function}
 * to each element of an {@link Iterable}
 */
public final class IterableUtil {
    private IterableUtil() {
        // Empty
    }

    public static <I_ITEM, O_ITEM> CloseableIterable<O_ITEM> map(final Iterable<I_ITEM> iterable, final Function function) {
        return map(iterable, Collections.singletonList(function));
    }

    public static <I_ITEM, O_ITEM> CloseableIterable<O_ITEM> map(final Iterable<I_ITEM> iterable, final List<Function> functions) {
        return new MappedIterable<>(iterable, functions);
    }

    public static <T> CloseableIterable<T> concat(final Iterable<? extends Iterable<? extends T>> iterables) {
        return new ChainedIterable<>(iterables);
    }

    private static class MappedIterable<I_ITEM, O_ITEM> implements CloseableIterable<O_ITEM> {
        private final Iterable<I_ITEM> iterable;
        private final List<Function> functions;

        MappedIterable(final Iterable<I_ITEM> iterable, final List<Function> functions) {
            this.iterable = iterable;
            this.functions = functions;
        }

        @Override
        public CloseableIterator<O_ITEM> iterator() {
            return new MappedIterator<>(iterable.iterator(), functions);
        }

        @Override
        public void close() {
            CloseableUtil.close(iterable);
        }
    }

    private static class MappedIterator<I_ITEM, O_ITEM> implements CloseableIterator<O_ITEM> {
        private final Iterator<? extends I_ITEM> iterator;
        private final List<Function> functions;

        MappedIterator(final Iterator<I_ITEM> iterator, final List<Function> functions) {
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
                    if (null == function) {
                        throw new IllegalArgumentException("Function cannot be null");
                    }
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

    private static class ChainedIterable<T> implements CloseableIterable<T> {
        private final Iterable<? extends Iterable<? extends T>> iterables;

        ChainedIterable(final Iterable<? extends Iterable<? extends T>> iterables) {
            if (null == iterables) {
                throw new IllegalArgumentException("iterables are required");
            }
            this.iterables = iterables;
        }

        @Override
        public CloseableIterator<T> iterator() {
            return new ChainedIterator<>(iterables.iterator());
        }

        @Override
        public void close() {
            for (final Iterable<? extends T> iterable : iterables) {
                CloseableUtil.close(iterable);
            }
        }
    }

    private static class ChainedIterator<T> implements CloseableIterator<T> {
        private final Iterator<? extends Iterable<? extends T>> iterablesIterator;
        private Iterator<? extends T> currentIterator = Iterators.emptyIterator();

        ChainedIterator(final Iterator<? extends Iterable<? extends T>> iterablesIterator) {
            this.iterablesIterator = iterablesIterator;
        }

        @Override
        public boolean hasNext() {
            return getIterator().hasNext();
        }

        @Override
        public T next() {
            return getIterator().next();
        }

        @Override
        public void remove() {
            currentIterator.remove();
        }

        @Override
        public void close() {
            CloseableUtil.close(currentIterator);
            while (iterablesIterator.hasNext()) {
                CloseableUtil.close(iterablesIterator.next());
            }
        }

        private Iterator<? extends T> getIterator() {
            while (!currentIterator.hasNext()) {
                CloseableUtil.close(currentIterator);
                if (iterablesIterator.hasNext()) {
                    currentIterator = iterablesIterator.next().iterator();
                } else {
                    break;
                }
            }

            return currentIterator;
        }
    }
}
