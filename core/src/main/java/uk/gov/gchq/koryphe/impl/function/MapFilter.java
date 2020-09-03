/*
 * Copyright 2018-2020 Crown Copyright
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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.tuple.predicate.KoryphePredicate2;

import java.util.Map;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * A {@code MapFilter} is a {@link java.util.function.Function} that filters
 * items from a {@link Map} based on a {@link java.util.function.Predicate}.
 * You can provide a key predicate, value predicate and keyValuePredicate.
 * All predicates must return true for the entry to be valid and kept.
 * If a predicate is not provided then it will default to returning true.
 */
@Since("1.6.0")
@Summary("Filters map entries based on a predicate")
public class MapFilter<K, V> extends KorypheFunction<Map<K, V>, Map<K, V>> {
    private Predicate<K> keyPredicate = null;
    private Predicate<V> valuePredicate = null;
    private KoryphePredicate2<K, V> keyValuePredicate = null;

    private Predicate<Map.Entry<K, V>> removeIfPredicate = null;

    @Override
    public Map<K, V> apply(final Map<K, V> map) {
        if (nonNull(map) && nonNull(removeIfPredicate)) {
            map.entrySet().removeIf(removeIfPredicate);
        }
        return map;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public Predicate<K> getKeyPredicate() {
        return keyPredicate;
    }

    public MapFilter<K, V> keyPredicate(final Predicate keyPredicate) {
        return setKeyPredicate(keyPredicate);
    }

    public MapFilter<K, V> setKeyPredicate(final Predicate<K> keyPredicate) {
        this.keyPredicate = keyPredicate;
        updateRemoveIfPredicate();
        return this;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public Predicate<V> getValuePredicate() {
        return valuePredicate;
    }

    public MapFilter<K, V> valuePredicate(final Predicate valuePredicate) {
        return setValuePredicate(valuePredicate);
    }

    public MapFilter<K, V> setValuePredicate(final Predicate<V> valuePredicate) {
        this.valuePredicate = valuePredicate;
        updateRemoveIfPredicate();
        return this;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public KoryphePredicate2<K, V> getKeyValuePredicate() {
        return keyValuePredicate;
    }

    public MapFilter<K, V> keyValuePredicate(final KoryphePredicate2 keyValuePredicate) {
        return setKeyValuePredicate(keyValuePredicate);
    }

    public MapFilter<K, V> setKeyValuePredicate(final KoryphePredicate2<K, V> keyValuePredicate) {
        this.keyValuePredicate = keyValuePredicate;
        updateRemoveIfPredicate();
        return this;
    }

    private void updateRemoveIfPredicate() {
        Predicate<Map.Entry<K, V>> filter = (e) -> true;
        if (nonNull(keyValuePredicate)) {
            filter = filter.and((e) -> keyValuePredicate.test(e.getKey(), e.getValue()));
        }
        if (nonNull(valuePredicate)) {
            filter = filter.and((e) -> valuePredicate.test(e.getValue()));
        }
        if (nonNull(keyPredicate)) {
            filter = filter.and((e) -> keyPredicate.test(e.getKey()));
        }

        removeIfPredicate = filter.negate();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.classEquals(o)) {
            return false; // Does exact equals and class checking
        }

        MapFilter that = (MapFilter) o;
        return new EqualsBuilder()
                .append(keyPredicate, that.keyPredicate)
                .append(valuePredicate, that.valuePredicate)
                .append(keyValuePredicate, that.keyValuePredicate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 79)
                .appendSuper(super.hashCode())
                .append(keyPredicate)
                .append(valuePredicate)
                .append(keyValuePredicate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("keyPredicate", keyPredicate)
                .append("valuePredicate", valuePredicate)
                .append("keyValuePredicate", keyValuePredicate)
                .toString();
    }
}
