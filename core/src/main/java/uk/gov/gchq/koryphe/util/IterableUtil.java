/*
 * Copyright 2017-2020 Crown Copyright
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import uk.gov.gchq.koryphe.impl.predicate.And;
import uk.gov.gchq.koryphe.iterable.CloseableIterable;
import uk.gov.gchq.koryphe.iterable.CloseableIterator;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An {@code IterableUtil} is a utility class providing capabilities for:
 * <ul>
 * <li>Lazily applying a {@link Function}, or a {@link List} of {@link Function}s,
 * to each element of an {@link Iterable}</li>
 * <li>Flatmapping of nested {@link Iterable}s via concatenation, to allow correct closing of the iterables</li>
 * </ul>
 */
public final class IterableUtil {
    private IterableUtil() {
        // Empty
    }

    /**
     * Filters items out of an iterable.
     * If the predicate returns false then an item is not valid and is removed.
     *
     * @param iterable  the items to filter
     * @param predicate the predicate to apply
     * @param <T>       the type of the items in the iterable
     * @return the lazily filtered iterable
     */
    public static <T> CloseableIterable<T> filter(final Iterable<T> iterable, final Predicate predicate) {
        if (null == predicate) {
            throw new IllegalArgumentException("Predicate cannot be null");
        }
        return filter(iterable, Collections.singletonList(predicate));
    }

    /**
     * Filters items out of an iterable.
     * If any predicate returns false then an item is not valid and is removed.
     *
     * @param iterable   the items to filter
     * @param predicates the predicates to apply
     * @param <T>        the type of the items in the iterable
     * @return the lazily filtered iterable
     */
    public static <T> CloseableIterable<T> filter(final Iterable<T> iterable, final List<Predicate> predicates) {
        if (null == iterable) {
            return null;
        }

        if (null == predicates) {
            throw new IllegalArgumentException("List of predicates cannot be null");
        }

        for (final Predicate predicate : predicates) {
            if (null == predicate) {
                throw new IllegalArgumentException("Predicates list cannot contain a null predicate");
            }
        }
        return new FilteredIterable<>(iterable, predicates);
    }

    public static <I_ITEM, O_ITEM> CloseableIterable<O_ITEM> map(final Iterable<I_ITEM> iterable, final Function function) {
        if (null == function) {
            throw new IllegalArgumentException("Function cannot be null");
        }
        return map(iterable, Collections.singletonList(function));
    }

    public static <I_ITEM, O_ITEM> CloseableIterable<O_ITEM> map(final Iterable<I_ITEM> iterable, final List<Function> functions) {
        if (null == iterable) {
            return null;
        }

        if (null == functions) {
            throw new IllegalArgumentException("List of functions cannot be null");
        }

        for (final Function func : functions) {
            if (null == func) {
                throw new IllegalArgumentException("Functions list cannot contain a null function");
            }
        }
        return new MappedIterable<>(iterable, functions);
    }

    public static <T> CloseableIterable<T> concat(final Iterable<? extends Iterable<? extends T>> iterables) {
        return new ChainedIterable<>(iterables);
    }

    public static <T> CloseableIterable<T> limit(final Iterable<T> iterable, final int start, final Integer end, final boolean truncate) {
        return new LimitedIterable<>(iterable, start, end, truncate);
    }

    /**
     * @param <I_ITEM> input type of items in the input iterator
     * @param <O_ITEM> output type of items in the output iterator
     * @deprecated Closable will be removed, it is used with scaling Big Data and does not belong in Koryphe.
     */
    @Deprecated
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

    /**
     * @param <O_ITEM> the type of items in the iterator
     * @deprecated Closable will be removed, it is used with scaling Big Data and does not belong in Koryphe.
     */
    @Deprecated
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

    /**
     * @param <T> the type of items in the iterator
     * @deprecated Closable will be removed, it is used with scaling Big Data and does not belong in Koryphe.
     */
    @Deprecated
    private static class FilteredIterable<T> implements CloseableIterable<T> {
        private final Iterable<T> iterable;
        private final List<Predicate> predicates;

        FilteredIterable(final Iterable<T> iterable, final List<Predicate> predicates) {
            this.iterable = iterable;
            this.predicates = predicates;
        }

        @Override
        public CloseableIterator<T> iterator() {
            return new FilteredIterator<>(iterable.iterator(), predicates);
        }

        @Override
        public void close() {
            CloseableUtil.close(iterable);
        }
    }

    /**
     * @param <T> the type of items in the iterator
     * @deprecated Closable will be removed, it is used with scaling Big Data and does not belong in Koryphe.
     */
    @Deprecated
    private static class FilteredIterator<T> implements CloseableIterator<T> {
        private final Iterator<? extends T> iterator;
        private final And<T> andPredicate;

        FilteredIterator(final Iterator<T> iterator, final List<Predicate> predicates) {
            this.iterator = iterator;
            this.andPredicate = new And<>(predicates);
        }

        private T nextElement;
        private Boolean hasNext;

        @Override
        public boolean hasNext() {
            if (null == hasNext) {
                while (iterator.hasNext()) {
                    final T possibleNext = iterator.next();
                    if (andPredicate.test(possibleNext)) {
                        nextElement = possibleNext;
                        hasNext = true;
                        return true;
                    }
                }
                hasNext = false;
                nextElement = null;
            }

            final boolean hasNextResult = Boolean.TRUE.equals(hasNext);
            if (!hasNextResult) {
                close();
            }

            return hasNextResult;
        }

        @Override
        public T next() {
            if ((null == hasNext) && (!hasNext())) {
                throw new NoSuchElementException("Reached the end of the iterator");
            }

            final T elementToReturn = nextElement;
            nextElement = null;
            hasNext = null;

            return elementToReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot call remove on a " + getClass().getSimpleName());
        }

        @Override
        public void close() {
            CloseableUtil.close(iterator);
        }
    }

    /**
     * @param <T> the type of items in the iterator
     * @deprecated Closable will be removed, it is used with scaling Big Data and does not belong in Koryphe.
     */
    @Deprecated
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

    /**
     * @param <T> the type of items in the iterator
     * @deprecated Closable will be removed, it is used with scaling Big Data and does not belong in Koryphe.
     */
    @Deprecated
    private static class ChainedIterator<T> implements CloseableIterator<T> {
        private final Iterator<? extends Iterable<? extends T>> iterablesIterator;
        private Iterator<? extends T> currentIterator = Collections.emptyIterator();

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

    /**
     * @param <T> the type of items in the iterator
     * @deprecated Closable will be removed, it is used with scaling Big Data and does not belong in Koryphe.
     */
    @Deprecated
    private static final class LimitedIterable<T> implements CloseableIterable<T> {
        private final Iterable<T> iterable;
        private final int start;
        private final Integer end;
        private final Boolean truncate;

        private LimitedIterable(final Iterable<T> iterable, final int start, final Integer end, final boolean truncate) {
            if (null != end && start > end) {
                throw new IllegalArgumentException("The start pointer must be less than the end pointer.");
            }

            if (null == iterable) {
                this.iterable = Collections.emptyList();
            } else {
                this.iterable = iterable;
            }

            this.start = start;
            this.end = end;
            this.truncate = truncate;
        }

        @JsonIgnore
        public int getStart() {
            return start;
        }

        @JsonIgnore
        public Integer getEnd() {
            return end;
        }

        @Override
        public void close() {
            CloseableUtil.close(iterable);
        }

        @Override
        public CloseableIterator<T> iterator() {
            return new LimitedIterator<>(iterable.iterator(), start, end, truncate);
        }
    }

    /**
     * @param <T> the type of items in the iterator
     * @deprecated Closable will be removed, it is used with scaling Big Data and does not belong in Koryphe.
     */
    @Deprecated
    private static final class LimitedIterator<T> implements CloseableIterator<T> {
        private final Iterator<T> iterator;
        private final Integer end;
        private int index = 0;
        private Boolean truncate = true;

        private LimitedIterator(final Iterator<T> iterator, final int start, final Integer end, final boolean truncate) {
            if (null != end && start > end) {
                throw new IllegalArgumentException("start should be less than end");
            }

            if (null == iterator) {
                this.iterator = Collections.emptyIterator();
            } else {
                this.iterator = iterator;
            }
            this.end = end;
            this.truncate = truncate;

            while (index < start && hasNext()) {
                next();
            }
        }

        @Override
        public void close() {
            CloseableUtil.close(iterator);
        }

        @Override
        public boolean hasNext() {
            final boolean withinLimit = (null == end || index < end);

            if (!withinLimit && !truncate && iterator.hasNext()) {
                // Throw an exception if we are - not within the limit, we don't want to truncate and there are items remaining.
                throw new NoSuchElementException("Limit of " + end + " exceeded.");
            }

            final boolean hasNext = withinLimit && iterator.hasNext();
            if (!hasNext) {
                close();
            }

            return hasNext;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            index++;
            return iterator.next();
        }
    }
}
