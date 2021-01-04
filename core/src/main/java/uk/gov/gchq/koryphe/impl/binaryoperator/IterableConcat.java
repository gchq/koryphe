/*
 * Copyright 2021 Crown Copyright
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


/**
 * A <code>IterableConcat</code> is a {@link KorypheBinaryOperator} that concatenates
 * {@link Iterable}s together.
 */
@Since("1.12.1")
@Summary("Concatenates Iterables together.")
public class IterableConcat<I_ITEM> extends KorypheBinaryOperator<Iterable<I_ITEM>> {
    @Override
    protected Iterable<I_ITEM> _apply(final Iterable<I_ITEM> a, final Iterable<I_ITEM> b) {
        return IterableUtil.concat(a, b);
    }

}
