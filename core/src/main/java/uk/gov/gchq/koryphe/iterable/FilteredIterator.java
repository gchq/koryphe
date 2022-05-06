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

import uk.gov.gchq.koryphe.impl.predicate.And;
import uk.gov.gchq.koryphe.util.CloseableUtil;

import java.io.Closeable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @param <T> the type of items in the iterator
 */
public class FilteredIterator<T> implements Closeable, Iterator<T> {
    private final Iterator<? extends T> iterator;
    private final And<T> andPredicate;

    public FilteredIterator(final Iterator<T> iterator, final List<Predicate> predicates) {
        if (null == iterator) {
            throw new IllegalArgumentException("iterator is required");
        }
        if (null == predicates) {
            throw new IllegalArgumentException("List of predicates cannot be null");
        }
        if (predicates.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Predicates list cannot contain a null predicate");
        }

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
