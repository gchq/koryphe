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

package uk.gov.gchq.koryphe.impl.binaryoperator;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.binaryoperator.KorypheBinaryOperator;

import java.util.Collection;

/**
 * A <code>CollectionIntersect</code> is a {@link KorypheBinaryOperator} that
 * intersects 2 collections. It calls {@link Collection#retainAll(Collection)}.
 */
@Since("1.6.0")
@Summary("Concatenates collections together.")
public class CollectionIntersect<T> extends KorypheBinaryOperator<Collection<T>> {
    @Override
    protected Collection<T> _apply(final Collection<T> a, final Collection<T> b) {
        a.retainAll(b);
        return a;
    }
}
