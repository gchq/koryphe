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

package uk.gov.gchq.koryphe.impl.predicate.range;

import org.junit.Test;

import uk.gov.gchq.koryphe.util.TimeUnit;

import java.util.Date;

public class InDateRangeTest extends AbstractInTimeRangeTest<Date> {
    @Override
    protected Date convert(final Long value) {
        return null != value ? new Date(value) : null;
    }

    @Override
    protected InDateRange.Builder createBuilder() {
        return new InDateRange.Builder();
    }

    @Test
    public void test() {
        InDateRange inDateRange = new InDateRange.Builder().timeUnit(TimeUnit.DAY).build();
    }
}
