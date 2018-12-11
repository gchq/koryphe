/*
 * Copyright 2018 Crown Copyright
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

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.tuple.predicate.KoryphePredicate2;

import java.util.Map;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * A {@code MapRemoveIf} is a {@link java.util.function.Function} that removes
 * items from a {@link Map} based on a {@link java.util.function.Predicate}.
 * You can provide a key predicate, value predicate and keyValuePredicate.
 * If one or more of the predicates return true then an entry will be removed.
 */
@Since("1.6.0")
@Summary("Removes map entries based on a predicate")
public class MapRemoveIf<K, V> extends KorypheFunction<Map<K, V>, Map<K, V>> {
    private Predicate<K> keyPredicate = null;
    private Predicate<V> valuePredicate = null;
    private KoryphePredicate2<K, V> keyValuePredicate = null;

    private Predicate<Map.Entry<K, V>> combinedPredicate;

    @Override
    public Map<K, V> apply(final Map<K, V> map) {
        if (nonNull(map)) {
            map.entrySet().removeIf(combinedPredicate);
        }
        return map;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public Predicate<K> getKeyPredicate() {
        return keyPredicate;
    }

    public MapRemoveIf<K, V> keyPredicate(final Predicate keyPredicate) {
        return setKeyPredicate(keyPredicate);
    }

    public MapRemoveIf<K, V> setKeyPredicate(final Predicate<K> keyPredicate) {
        this.keyPredicate = keyPredicate;
        updateCombinedPredicate();
        return this;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public Predicate<V> getValuePredicate() {
        return valuePredicate;
    }

    public MapRemoveIf<K, V> valuePredicate(final Predicate valuePredicate) {
        return setValuePredicate(valuePredicate);
    }

    public MapRemoveIf<K, V> setValuePredicate(final Predicate<V> valuePredicate) {
        this.valuePredicate = valuePredicate;
        updateCombinedPredicate();
        return this;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public KoryphePredicate2<K, V> getKeyValuePredicate() {
        return keyValuePredicate;
    }

    public MapRemoveIf<K, V> keyValuePredicate(final KoryphePredicate2 keyValuePredicate) {
        return setKeyValuePredicate(keyValuePredicate);
    }

    public MapRemoveIf<K, V> setKeyValuePredicate(final KoryphePredicate2<K, V> keyValuePredicate) {
        this.keyValuePredicate = keyValuePredicate;
        updateCombinedPredicate();
        return this;
    }

    private void updateCombinedPredicate() {
        combinedPredicate = (e) -> false;

        if (nonNull(keyValuePredicate)) {
            combinedPredicate = combinedPredicate.or((e) -> keyValuePredicate.test(e.getKey(), e.getValue()));
        }
        if (nonNull(valuePredicate)) {
            combinedPredicate = combinedPredicate.or((e) -> valuePredicate.test(e.getValue()));
        }
        if (nonNull(keyPredicate)) {
            combinedPredicate = combinedPredicate.or((e) -> keyPredicate.test(e.getKey()));
        }
    }
}
