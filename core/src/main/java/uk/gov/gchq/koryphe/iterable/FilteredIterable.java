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
import java.util.function.Predicate;

/**
 * A {@code FilteredIterable} is a {@link java.io.Closeable}
 * {@link java.lang.Iterable} which can filter out elements
 * based on a {@link java.util.List} of {@link java.util.function.Predicate}s.
 *
 * @param <T> the type of items in the iterable.
 */
public class FilteredIterable<T> implements Closeable, Iterable<T> {
    private final Iterable<T> iterable;
    private final List<Predicate> predicates;

    public FilteredIterable(final Iterable<T> iterable, final List<Predicate> predicates) {
        if (null == iterable) {
            throw new IllegalArgumentException("iterable is required");
        }

        this.iterable = iterable;
        this.predicates = predicates;
    }

    @Override
    public Iterator<T> iterator() {
        return new FilteredIterator<>(iterable.iterator(), predicates);
    }

    @Override
    public void close() {
        CloseableUtil.close(iterable);
    }
}
