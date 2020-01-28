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

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.util.IterableUtil;

import java.util.function.Predicate;

import static java.util.Objects.nonNull;

/**
 * A {@code IterableFilter} is a {@link java.util.function.Function} that filters
 * items from an {@link Iterable} based on a {@link Predicate}.
 * If the predicate returns false then an item is not valid and is removed.
 */
@Since("1.6.0")
@Summary("Removes items from an iterable based on a predicate")
public class IterableFilter<I_ITEM> extends KorypheFunction<Iterable<I_ITEM>, Iterable<I_ITEM>> {
    private Predicate<I_ITEM> predicate = null;

    public IterableFilter() {
    }

    public IterableFilter(final Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public Iterable<I_ITEM> apply(final Iterable<I_ITEM> items) {
        if (nonNull(items) && nonNull(predicate)) {
            return IterableUtil.filter(items, predicate);
        }
        return items;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public Predicate<I_ITEM> getPredicate() {
        return predicate;
    }

    public IterableFilter<I_ITEM> predicate(final Predicate predicate) {
        return setPredicate(predicate);
    }

    public IterableFilter<I_ITEM> setPredicate(final Predicate<I_ITEM> predicate) {
        this.predicate = predicate;
        return this;
    }
}
