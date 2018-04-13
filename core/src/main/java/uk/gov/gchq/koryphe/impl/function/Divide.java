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

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.tuple.function.KorypheFunction2;
import uk.gov.gchq.koryphe.tuple.n.Tuple2;

/**
 * A <code>Divide</code> is a {@link java.util.function.Function} that takes in
 * two {@link Integer}s and returns the result of dividing the first by the second.
 * <p>
 * The resulting object is a {@link Tuple2} containing the quotient and remainder.
 */
@Since("1.0.0")
@Summary("Takes in two integers and returns the result of dividing the first by the second.")
public class Divide extends KorypheFunction2<Integer, Integer, Tuple2<Integer, Integer>> {
    @Override
    public Tuple2<Integer, Integer> apply(final Integer input1, final Integer input2) {
        int in2 = input2 == null ? 1 : input2;
        if (input1 == null) {
            return null;
        } else {
            return new Tuple2<>(input1 / in2, input1 % in2);
        }
    }
}
