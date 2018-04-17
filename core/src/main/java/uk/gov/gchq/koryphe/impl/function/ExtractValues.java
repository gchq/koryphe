/*
 * Copyright 2017-2018 Crown Copyright
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
 * An {@link ExtractValues} is a {@link KorypheFunction} which extracts the {@link java.util.Set} of values
 * of a provided input {@link Map}
 *
 * @param <K> the key type
 * @param <V> the value type
 */
@Since("1.1.0")
@Summary("Extracts the values from a Map")
public class ExtractValues<K, V> extends KorypheFunction<Map<K, V>, Iterable<V>> {
    @Override
    public Iterable<V> apply(final Map<K, V> map) {
        return null == map ? null : map.values();
    }
}
