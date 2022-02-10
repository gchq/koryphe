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

package uk.gov.gchq.koryphe.impl.function;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.util.Map;

/**
 * A {@link DictionaryLookup} is a {@link KorypheFunction} that takes a key and using a dictionary returns a value.
 */
@Since("1.7.0")
@Summary("Looks up a value in a map")
public class DictionaryLookup<K, V> extends KorypheFunction<K, V> {
    Map<K, V> dictionary;

    public DictionaryLookup() {
        // Required for serialisation
    }

    public DictionaryLookup(final Map<K, V> dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public V apply(final K key) {
        if (dictionary == null) {
            throw new IllegalArgumentException("The " + DictionaryLookup.class.getName() + " KorypheFunction has not been provided with a dictionary");
        }
        return dictionary.get(key);
    }

    public Map<K, V> getDictionary() {
        return dictionary;
    }

    public void setDictionary(final Map<K, V> dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.equals(o)) {
            return false; // Does class checking
        }

        DictionaryLookup that = (DictionaryLookup) o;
        return new EqualsBuilder()
                .append(dictionary, that.dictionary)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 53)
                .appendSuper(super.hashCode())
                .append(dictionary)
                .toHashCode();
    }
}
