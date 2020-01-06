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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Iterables;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.util.CloseableUtil;

import java.util.function.Predicate;

import static java.util.Objects.nonNull;
import static uk.gov.gchq.koryphe.util.IterableUtil.filter;

/**
 * A {@code FirstItem} is a {@link java.util.function.Function} that returns the first item from a provided input
 * Iterable which matches the provided {@link Predicate}
 *
 * @param <I_ITEM> the type of objects in the iterable
 */
@Since("1.8.3")
@Summary("Provides the first valid item from an iterable based on a predicate.")
public class FirstValid<I_ITEM> extends KorypheFunction<Iterable<I_ITEM>, I_ITEM> {
    private Predicate predicate = null;

    public FirstValid() {
    }

    public FirstValid(final Predicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public I_ITEM apply(final Iterable<I_ITEM> iterable) {
        if (nonNull(iterable) && nonNull(predicate)) {
            try {
                return Iterables.getFirst(filter(iterable, predicate), null);
            } finally {
                CloseableUtil.close(iterable);
            }
        }
        return null;
    }

    public FirstValid<I_ITEM> predicate(final Predicate predicate) {
        return setPredicate(predicate);
    }

    public FirstValid<I_ITEM> setPredicate(final Predicate predicate) {
        this.predicate = predicate;
        return this;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public Predicate getPredicate() {
        return predicate;
    }
}
