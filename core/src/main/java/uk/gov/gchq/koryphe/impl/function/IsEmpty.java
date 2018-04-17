/*
 * Copyright 2017-2018 Crown Copyright
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

import com.google.common.collect.Iterables;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.util.CloseableUtil;

/**
 * A {@code IsEmpty} is a {@link KorypheFunction} which returns a boolean based
 * on whether or not the input {@link Iterable} is empty.
 */
@Since("1.1.0")
@Summary("Returns true if an iterable is empty")
public class IsEmpty extends KorypheFunction<Iterable, Boolean> {
    @Override
    @SuppressFBWarnings(value = "DE_MIGHT_IGNORE", justification = "Any exceptions are to be ignored")
    public Boolean apply(final Iterable input) {
        if (null == input) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        try {
            return Iterables.isEmpty(input);
        } finally {
            CloseableUtil.close(input);
        }
    }
}
