/*
 * Copyright 2017-2020 Crown Copyright
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
import uk.gov.gchq.koryphe.tuple.function.KorypheFunction2;

/**
 * A <code>Multiply</code> is a {@link java.util.function.Function} that takes in
 * two {@link Integer}s and returns the result of multiplying the first by the second.
 */
@Since("1.0.0")
@Summary("Multiplies 2 integers")
public class Multiply extends KorypheFunction2<Integer, Integer, Integer> {
    @Override
    public Integer apply(final Integer input1, final Integer input2) {
        if (input2 == null) {
            return input1;
        } else if (input1 == null) {
            return null;
        } else {
            return input1 * input2;
        }
    }
}
