/*
 * Copyright 2017 Crown Copyright
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
import uk.gov.gchq.koryphe.example.predicate.IsMoreThanExample;
import uk.gov.gchq.koryphe.example.predicate.TupleCompositeAndExample;
import uk.gov.gchq.koryphe.example.predicate.TupleAndExample;
import uk.gov.gchq.koryphe.example.function.*;

public class RunAll {
    public static void main(String[] args) throws Exception {
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
    }
}