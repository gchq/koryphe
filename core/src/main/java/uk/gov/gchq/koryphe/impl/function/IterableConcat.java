/*
 * Copyright 2017-2024 Crown Copyright
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

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.util.IterableUtil;

/**
 * An {@code IterableConcat} is a {@link KorypheFunction} which flattens an
 * {@link Iterable} of {@link Iterable}s by concatenating them.
 *
 * @param <I_ITEM> the type of objects in the innermost iterable
 */
@Since("1.1.0")
@Summary("Concatenates 2 iterables")
public class IterableConcat<I_ITEM> extends KorypheFunction<Iterable<? extends Iterable<I_ITEM>>, Iterable<I_ITEM>> {
    @Override
    public Iterable<I_ITEM> apply(final Iterable<? extends Iterable<I_ITEM>> items) {
        return IterableUtil.concat(items);
    }
}
