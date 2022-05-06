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
package uk.gov.gchq.koryphe.commonutil.iterable;

import uk.gov.gchq.koryphe.util.CloseableUtil;

import java.io.Closeable;
import java.util.Collections;
import java.util.Iterator;

/**
 * @param <T> the type of items in the iterator
 */
public class ChainedIterator<T> implements Closeable, Iterator<T> {
    private final Iterator<? extends Iterable<? extends T>> iterablesIterator;
    private Iterator<? extends T> currentIterator = Collections.emptyIterator();

    public ChainedIterator(final Iterator<? extends Iterable<? extends T>> iterablesIterator) {
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