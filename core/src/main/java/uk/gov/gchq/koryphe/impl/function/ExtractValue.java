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
package uk.gov.gchq.koryphe.impl.function;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.util.Map;

/**
 * An <code>ExtractValue</code> is a {@link java.util.function.Function} that retrieves the value from a given {@link Map}, for the corresponding pre-configured key.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
@Since("1.2.0")
@Summary("Extracts a value from a Map")
public class ExtractValue<K, V> extends KorypheFunction<Map<K, V>, V> {
    private K key;

    public ExtractValue() {
    }

    public ExtractValue(final K key) {
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    public void setKey(final K key) {
        this.key = key;
    }

    @Override
    public V apply(final Map<K, V> map) {
        return null == map ? null : map.get(key);
    }
}
