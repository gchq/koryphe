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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * <p>
 * A <code>InTimeRangeDual</code> is a {@link InRangeDual}
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
 * <p>
 * You can also configure the start and end times using startString and endString. These strings should be in one of the following formats:
 * </p>
 * <ul>
 * <li>yyyy</li>
 * <li>yyyyMM</li>
 * <li>yyyyMMdd</li>
 * <li>yyyyMMddHH</li>
 * <li>yyyyMMddHHmm</li>
 * <li>yyyyMMddHHmmss</li>
 * </ul>
 * You can also use a space, '-', '/', '_', ':', '|', or '.' to separate the parts.
 *
 * @see InRange
 * @see Builder
 */
@JsonDeserialize(builder = InTimeRangeDual.Builder.class)
public class InTimeRangeDual extends InRangeDualTimeBased<Long> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @Override
    public Long getStart() {
        return super.getStart();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @Override
    public Long getEnd() {
        return super.getEnd();
    }

    public static class Builder extends BaseBuilder<Builder, InTimeRangeDual, Long> {
        public Builder() {
            super(new InTimeRangeDual());
        }
    }
}
