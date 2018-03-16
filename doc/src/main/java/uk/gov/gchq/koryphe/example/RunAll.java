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

package uk.gov.gchq.koryphe.example;

import uk.gov.gchq.koryphe.example.binaryoperator.TupleAggregationProductExample;
import uk.gov.gchq.koryphe.example.binaryoperator.TupleCompositeAggregationExample;
import uk.gov.gchq.koryphe.example.function.DivideByExample;
import uk.gov.gchq.koryphe.example.function.DivideExample;
import uk.gov.gchq.koryphe.example.function.ExtractKeysExample;
import uk.gov.gchq.koryphe.example.function.ExtractValueExample;
import uk.gov.gchq.koryphe.example.function.ExtractValuesExample;
import uk.gov.gchq.koryphe.example.function.FirstItemExample;
import uk.gov.gchq.koryphe.example.function.LastItemExample;
import uk.gov.gchq.koryphe.example.function.MultiplyByExample;
import uk.gov.gchq.koryphe.example.function.MultiplyExample;
import uk.gov.gchq.koryphe.example.function.NthItemExample;
import uk.gov.gchq.koryphe.example.function.TupleCompositeMultiplyByExample;
import uk.gov.gchq.koryphe.example.function.TupleDivideByExample;
import uk.gov.gchq.koryphe.example.function.TupleDivideExample;
import uk.gov.gchq.koryphe.example.function.TupleMultiplyByExample;
import uk.gov.gchq.koryphe.example.function.TupleMultiplyExample;
import uk.gov.gchq.koryphe.example.function.iterable.IterableConcatExample;
import uk.gov.gchq.koryphe.example.function.iterable.IterableFunctionExample;
import uk.gov.gchq.koryphe.example.predicate.IsMoreThanExample;
import uk.gov.gchq.koryphe.example.predicate.TupleAndExample;
import uk.gov.gchq.koryphe.example.predicate.TupleCompositeAndExample;

public final class RunAll {
    private RunAll() { }

    public static void main(final String[] args) throws Exception {
        (new MultiplyByExample()).execute();
        (new MultiplyExample()).execute();
        (new DivideByExample()).execute();
        (new DivideExample()).execute();
        (new TupleMultiplyByExample()).execute();
        (new TupleCompositeMultiplyByExample()).execute();
        (new TupleMultiplyExample()).execute();
        (new TupleDivideByExample()).execute();
        (new TupleDivideExample()).execute();

        (new TupleAggregationProductExample()).execute();
        (new TupleCompositeAggregationExample()).execute();

        (new IsMoreThanExample()).execute();
        (new TupleCompositeAndExample()).execute();
        (new TupleAndExample()).execute();

        (new FirstItemExample()).execute();
        (new NthItemExample()).execute();
        (new LastItemExample()).execute();

        (new ExtractKeysExample()).execute();
        (new ExtractValueExample()).execute();
        (new ExtractValuesExample()).execute();

        (new IterableConcatExample()).execute();
        (new IterableFunctionExample()).execute();
    }
}
