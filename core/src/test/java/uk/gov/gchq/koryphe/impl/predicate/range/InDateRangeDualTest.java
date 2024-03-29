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

package uk.gov.gchq.koryphe.impl.predicate.range;


import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.tuple.n.Tuple2;
import uk.gov.gchq.koryphe.util.DateUtil;

import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class InDateRangeDualTest extends AbstractInTimeRangeDualTest<Date> {

    @Test
    public void shouldTestValuesInRangeWithTimeZone() {
        // Given
        final String timeZone = "Etc/GMT+6";
        final InDateRangeDual filter = new InDateRangeDual.Builder()
                .start("2018/01/01 12:00")
                .end("2018/01/01 12:03")
                .startFullyContained(true)
                .endFullyContained(true)
                .timeZone(timeZone)
                .build();

        // When / Then
        assertThat(filter)
                // out of time range
                .rejects(new Tuple2<>(DateUtil.parse("2018/01/01 12:01", TimeZone.getTimeZone("Etc/GMT+0")),
                            DateUtil.parse("2018/01/01 12:02", TimeZone.getTimeZone("Etc/GMT+0"))))
                // inside time range
                .accepts(new Tuple2<>(DateUtil.parse("2018/01/01 12:01", TimeZone.getTimeZone("Etc/GMT+6")),
                            DateUtil.parse("2018/01/01 12:02", TimeZone.getTimeZone("Etc/GMT+6"))));
    }

    @Override
    protected Date convert(final Long value) {
        return null != value ? new Date(value) : null;
    }

    @Override
    protected InDateRangeDual.Builder createBuilderWithTimeOffsets() {
        return new InDateRangeDual.Builder();
    }
}
