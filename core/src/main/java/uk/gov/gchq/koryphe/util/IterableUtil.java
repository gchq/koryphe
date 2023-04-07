/*
 * Copyright 2017-2023 Crown Copyright
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

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
        return new FilteredIterable<>(iterable, predicates);
    }

    public static <I_ITEM, O_ITEM> Iterable<O_ITEM> map(final Iterable<I_ITEM> iterable, final Function function) {
        return map(iterable, Collections.singletonList(function));
    }

    public static <I_ITEM, O_ITEM> Iterable<O_ITEM> map(final Iterable<I_ITEM> iterable, final List<Function> functions) {
        if (null == iterable) {
            return null;
        }
        return new MappedIterable<>(iterable, functions);
    }

    public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> iterables) {
        return new ChainedIterable<>(iterables);
    }

    public static <T> Iterable<T> limit(final Iterable<T> iterable, final int start, final Integer end, final boolean truncate) {
        return new LimitedIterable<>(iterable, start, end, truncate);
    }

    public static int size(final Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection<?>) iterable).size();
        }
        return (int) StreamSupport.stream(iterable.spliterator(), false).count();
    }

    public static <T> List<T> toList(final Iterable<T> iterable) {
        if (iterable instanceof List) {
            return (List<T>) iterable;
        }
        if (iterable instanceof Set) {
            return new ArrayList<>((Set<T>) iterable);
        }
        List<T> asList = new ArrayList<>();
        iterable.forEach(asList::add);
        return asList;
    }

    public static boolean isEmpty(final Iterable<?> iterable) {
        return size(iterable) == 0;
    }

    public static <T> T getFirst(final Iterable<T> iterable) {
        return iterable.iterator().next();
    }

    public static <T> T getLast(final Iterable<T> iterable) {
        List<T> list = toList(iterable);
        return list.get(list.size() - 1);
    }
}
