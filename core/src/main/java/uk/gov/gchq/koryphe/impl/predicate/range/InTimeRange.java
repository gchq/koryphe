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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * A <code>InTimeRange</code> is a {@link InRange}
 * {@link java.util.function.Predicate} that is applied to Long values.
 * The range can be configured using time offsets from the current system time.
 * </p>
 * <p>
 * So you can set the start range bound using
 * the start value as normal, or using startOffsetInMillis, startOffsetInHours
 * or startOffsetInDays. At the point of this class being instantiated the
 * current system time is used to calculate the start value based on:
 * System.currentTimeMillis() - offset.
 * </p>
 * <p>
 * Similarly with the end range bound, this can be set using end, endOffsetInMillis,
 * endOffsetInHours or endOffsetInDays.
 * </p>
 *
 * @see InRange
 * @see Builder
 */
public class InTimeRange extends InRangeWithTimeOffsets<Long> {
    @JsonCreator
    public InTimeRange(@JsonProperty("start") final Long start,
                       @JsonProperty("startOffsetInMillis") final Long startOffsetInMillis,
                       @JsonProperty("startOffsetInHours") final Long startOffsetInHours,
                       @JsonProperty("startOffsetInDays") final Integer startOffsetInDays,
                       @JsonProperty("end") final Long end,
                       @JsonProperty("endOffsetInMillis") final Long endOffsetInMillis,
                       @JsonProperty("endOffsetInHours") final Long endOffsetInHours,
                       @JsonProperty("endOffsetInDays") final Integer endOffsetInDays,
                       @JsonProperty("startInclusive") final Boolean startInclusive,
                       @JsonProperty("endInclusive") final Boolean endInclusive) {
        super(
                start, startOffsetInMillis, startOffsetInHours, startOffsetInDays,
                end,
                endOffsetInMillis, endOffsetInHours, endOffsetInDays,
                startInclusive,
                endInclusive
        );
    }

    public static class Builder extends BaseBuilder<Builder, InTimeRange, Long> {
        public InTimeRange build() {
            return new InTimeRange(start, startOffsetInMillis, startOffsetInHours, startOffsetInDays, end, endOffsetInMillis, endOffsetInHours, endOffsetInDays, startInclusive, endInclusive);
        }
    }
}
