/*
 * Copyright 2017-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.util;

import uk.gov.gchq.koryphe.iterable.ChainedIterable;
import uk.gov.gchq.koryphe.iterable.FilteredIterable;
import uk.gov.gchq.koryphe.iterable.LimitedIterable;
import uk.gov.gchq.koryphe.iterable.MappedIterable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An {@code IterableUtil} is a utility class providing capabilities for:
 * <ul>
 * <li>Lazily applying a {@link Function}, or a {@link List} of {@link Function}s,
 * to each element of an {@link Iterable}</li>
 * <li>Flatmapping of nested {@link Iterable}s via concatenation, to allow correct closing of the iterables</li>
 * </ul>
 */
public final class IterableUtil {
    private IterableUtil() {
        // Empty
    }

    /**
     * Filters items out of an iterable.
     * If the predicate returns false then an item is not valid and is removed.
     *
     * @param iterable  the items to filter
     * @param predicate the predicate to apply
     * @param <T>       the type of the items in the iterable
     * @return the lazily filtered iterable
     */
    public static <T> Iterable<T> filter(final Iterable<T> iterable, final Predicate predicate) {
        if (null == predicate) {
            throw new IllegalArgumentException("Predicate cannot be null");
        }
        return filter(iterable, Collections.singletonList(predicate));
    }

    /**
     * Filters items out of an iterable.
     * If any predicate returns false then an item is not valid and is removed.
     *
     * @param iterable   the items to filter
     * @param predicates the predicates to apply
     * @param <T>        the type of the items in the iterable
     * @return the lazily filtered iterable
     */
    public static <T> Iterable<T> filter(final Iterable<T> iterable, final List<Predicate> predicates) {
        if (null == iterable) {
            return null;
        }

        if (null == predicates) {
            throw new IllegalArgumentException("List of predicates cannot be null");
        }

        for (final Predicate predicate : predicates) {
            if (null == predicate) {
                throw new IllegalArgumentException("Predicates list cannot contain a null predicate");
            }
        }
        return new FilteredIterable<>(iterable, predicates);
    }

    public static <I_ITEM, O_ITEM> Iterable<O_ITEM> map(final Iterable<I_ITEM> iterable, final Function function) {
        if (null == function) {
            throw new IllegalArgumentException("Function cannot be null");
        }
        return map(iterable, Collections.singletonList(function));
    }

    public static <I_ITEM, O_ITEM> Iterable<O_ITEM> map(final Iterable<I_ITEM> iterable, final List<Function> functions) {
        if (null == iterable) {
            return null;
        }

        if (null == functions) {
            throw new IllegalArgumentException("List of functions cannot be null");
        }

        for (final Function func : functions) {
            if (null == func) {
                throw new IllegalArgumentException("Functions list cannot contain a null function");
            }
        }
        return new MappedIterable<>(iterable, functions);
    }

    public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> iterables) {
        return new ChainedIterable<>(iterables);
    }

    public static <T> Iterable<T> limit(final Iterable<T> iterable, final int start, final Integer end, final boolean truncate) {
        return new LimitedIterable<>(iterable, start, end, truncate);
    }

}
