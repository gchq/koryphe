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

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

/**
 * A <code>ToInteger</code> is a {@link java.util.function.Function} that takes
 * an Object and casts it to an Integer.
 * <p>
 * The resulting object is what is returned from the method.
 */
@Since("1.5.0")
@Summary("Casts input Object to an Integer")
public class ToInteger extends KorypheFunction<Object, Integer> {

    @Override
    public Integer apply(final Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        if (value instanceof String) {
            return Integer.valueOf(((String) value));
        }

        throw new IllegalArgumentException("Could not convert value to Integer: " + value);
    }
}
