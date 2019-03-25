/*
 * Copyright 2019 Crown Copyright
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
 * A {@link DictionaryLookUp} is a {@link KorypheFunction} that takes a key and using a dictionary returns a value.
 */
@Since("1.7.0")
@Summary("Looks up a value in a map")
public class DictionaryLookUp<K, V> extends KorypheFunction<K, V> {
    Map<K, V> dictionary;

    public DictionaryLookUp() {
        // Required for serialisation
    }

    public DictionaryLookUp(final Map<K, V> dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public V apply(final K key) {
        return dictionary.get(key);
    }

    public Map<K, V> getDictionary() {
        return dictionary;
    }

    public void setDictionary(final Map<K, V> dictionary) {
        this.dictionary = dictionary;
    }
}
