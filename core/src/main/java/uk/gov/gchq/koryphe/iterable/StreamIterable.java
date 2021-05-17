/*
 * Copyright 2019-2020 Crown Copyright
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

import com.fasterxml.jackson.annotation.JsonIgnore;

import uk.gov.gchq.koryphe.util.CloseableUtil;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @param <T> the type of items in the iterator.
 * @deprecated Closable will be removed, it is used with scaling Big Data and does not belong in Koryphe.
 */
@Deprecated
public class StreamIterable<T> implements CloseableIterable<T> {
    private final Supplier<Stream<T>> streamSupplier;

    public StreamIterable(final Supplier<Stream<T>> streamSupplier) {
        this.streamSupplier = streamSupplier;
    }

    @Override
    public void close() {
        CloseableUtil.close(streamSupplier);
    }

    @Override
    public CloseableIterator<T> iterator() {
        return new StreamIterator<>(streamSupplier.get());
    }

    @JsonIgnore
    public Stream<T> getStream() {
        return streamSupplier.get();
    }
}
