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
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static uk.gov.gchq.koryphe.util.JavaUtils.requireNonNullElse;

/**
 * An {@code LimitedIterator} is a {@link java.io.Closeable}
 * {@link java.util.Iterator} which is limited to a maximum size. This is
 * achieved by iterating through the objects contained in the iterator
 * until the preconfigured starting point is reached
 * (and discarding these), then by retrieving objects until either:
 * <ul>
 *     <li>the end of the iterator is reached, or</li>
 *     <li>the iterator pointer exceeds the specified limit</li>
 * </ul>
 *
 * @param <T> the type of items in the iterator.
 */
public final class LimitedIterator<T> implements Closeable, Iterator<T> {
    private final Iterator<T> iterator;
    private final Integer end;
    private int index = 0;
    private Boolean truncate = true;

    public LimitedIterator(final Iterator<T> iterator, final int start, final Integer end) {
        this(iterator, start, end, true);
    }

    public LimitedIterator(final Iterator<T> iterator, final int start, final Integer end, final boolean truncate) {
        if (null != end && start > end) {
            throw new IllegalArgumentException("start should be less than end");
        }

        this.iterator = requireNonNullElse(iterator, Collections.emptyIterator());
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
            throw new RuntimeException("Limit of " + end + " exceeded.");
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

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
