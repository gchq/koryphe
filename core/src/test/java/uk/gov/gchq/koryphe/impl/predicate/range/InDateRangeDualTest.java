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

package uk.gov.gchq.koryphe.impl.predicate.range;

import java.util.Date;

public class InDateRangeDualTest extends InRangeDualTimeBasedTest<Date> {
    @Override
    protected InDateRangeDual.Builder createBuilderWithTimeOffsets() {
        return new InDateRangeDual.Builder();
    }

    @Override
    protected Date convert(final Long value) {
        return null != value ? new Date(value) : null;
    }

    @Override
    protected Long unconvert(final Date value) {
        return null == value ? null : value.getTime();
    }

    @Override
    protected Class<Date> getTClass() {
        return Date.class;
    }
}
