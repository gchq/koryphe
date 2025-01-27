/*
 * Copyright 2025 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.binaryoperator;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.binaryoperator.KorypheBinaryOperator;
import uk.gov.gchq.koryphe.util.IterableUtil;

import java.util.Arrays;

/**
 * An <code>IterableMerge</code> is a {@link KorypheBinaryOperator} that takes two
 * {@link java.lang.Iterable}s and merges them together.
 */
@Since("2.6.0")
@Summary("Merges two iterables together.")
public class IterableMerge<T> extends KorypheBinaryOperator<Iterable<T>> {

    @Override
    protected Iterable<T> _apply(final Iterable<T> a, final Iterable<T> b) {
        return IterableUtil.concat(Arrays.asList(a, b));
    }
}
