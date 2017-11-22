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

package uk.gov.gchq.koryphe.impl.predicate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.tuple.predicate.KoryphePredicate2;

/**
 * <p>
 * An <code>TimeWindow</code> is a {@link java.util.function.Predicate}
 * validates a start and end timestamp are within a time window.
 * </p>
 * The time window should be defined by providing a start time and a size for the
 * window.
 * <p>
 * The start time in milliseconds can either be set explicitly or you can set
 * an start time offset from the current time. The offset can be set in milliseconds,
 * in hours or in days. So if you want the start to be 7 days ago set startOffsetInDays to 7.
 * </p>
 * <p>
 * The size of the time window can be set in milliseconds, in hours or in days.
 * So if you want the time window to be to be 1 day then set sizeInDays to 1.
 * </p>
 * <p>
 * Predicate accepts 2 timestamps to cater for users that have start and end
 * timestamp values. However, you can just supply a single timestamp twice.
 * </p>
 *
 * @see Builder
 */
public class TimeWindow extends KoryphePredicate2<Long, Long> {
    public static final long HOURS_TO_MILLISECONDS = 60L * 60L * 1000L;
    public static final long DAYS_TO_MILLISECONDS = 24L * HOURS_TO_MILLISECONDS;
    private final Long start;
    private final Long startOffset;
    private final Long size;

    @JsonCreator
    public TimeWindow(@JsonProperty("start") Long start,
                      @JsonProperty("startOffset") Long startOffset,
                      @JsonProperty("startOffsetInHours") Long startOffsetInHours,
                      @JsonProperty("startOffsetInDays") Integer startOffsetInDays,
                      @JsonProperty("size") Long size,
                      @JsonProperty("sizeInHours") Long sizeInHours,
                      @JsonProperty("sizeInDays") Integer sizeInDays) {
        final boolean isStartInvalid;
        if (null != start) {
            isStartInvalid = null != startOffset || null != startOffsetInHours || null != startOffsetInDays;
        } else if (null != startOffset) {
            isStartInvalid = null != startOffsetInHours || null != startOffsetInDays;
        } else {
            isStartInvalid = null != startOffsetInHours && null != startOffsetInDays;
        }
        if (isStartInvalid) {
            throw new IllegalArgumentException("Only set one of the following: start, startOffset, startOffsetInHours or startOffsetInDays");
        }

        final boolean isSizeInvalid;
        if (null != size) {
            isSizeInvalid = null != sizeInHours || null != sizeInDays;
        } else {
            isSizeInvalid = null != sizeInHours && null != sizeInDays;
        }
        if (isSizeInvalid) {
            throw new IllegalArgumentException("Only set one of the following: size, sizeInHours or sizeInDays");
        }

        this.start = start;

        if (null != startOffsetInHours) {
            this.startOffset = HOURS_TO_MILLISECONDS * startOffsetInHours;
        } else if (null != startOffsetInDays) {
            this.startOffset = DAYS_TO_MILLISECONDS * startOffsetInDays;
        } else {
            this.startOffset = startOffset;
        }


        if (null != sizeInHours) {
            this.size = HOURS_TO_MILLISECONDS * sizeInHours;
        } else if (null != sizeInDays) {
            this.size = DAYS_TO_MILLISECONDS * sizeInDays;
        } else {
            this.size = size;
        }
    }

    @Override
    public boolean test(final Long inputStart, final Long inputEnd) {
        if (null == inputStart || null == inputEnd) {
            return false;
        }

        if (null == startOffset) {
            return inputStart > start && inputEnd < (start + size);
        }

        final long startTemp = System.currentTimeMillis() - startOffset;
        return inputStart > startTemp && inputEnd < (startTemp + size);
    }

    public Long getStart() {
        return start;
    }

    public Long getStartOffset() {
        return startOffset;
    }

    public Long getSize() {
        return size;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final TimeWindow otherPredicate = (TimeWindow) obj;
        return new EqualsBuilder()
                .append(start, otherPredicate.start)
                .append(startOffset, otherPredicate.startOffset)
                .append(size, otherPredicate.size)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 31)
                .appendSuper(super.hashCode())
                .append(start)
                .append(startOffset)
                .append(size)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("start", start)
                .append("startOffset", startOffset)
                .append("size", size)
                .toString();
    }

    public static class Builder {
        private Long start;
        private Long startOffset;
        private Long startOffsetInHours;
        private Integer startOffsetInDays;
        private Long size;
        private Long sizeInHours;
        private Integer sizeInDays;

        public Builder start(final Long start) {
            this.start = start;
            return this;
        }

        public Builder startOffset(final Long startOffset) {
            this.startOffset = startOffset;
            return this;
        }

        public Builder startOffsetInHours(final Long startOffsetInHours) {
            this.startOffsetInHours = startOffsetInHours;
            return this;
        }

        public Builder startOffsetDays(final Integer startOffsetInDays) {
            this.startOffsetInDays = startOffsetInDays;
            return this;
        }

        public Builder size(final Long size) {
            this.size = size;
            return this;
        }

        public Builder sizeInHours(final Long sizeInHours) {
            this.sizeInHours = sizeInHours;
            return this;
        }

        public Builder sizeInDays(final Integer sizeInDays) {
            this.sizeInDays = sizeInDays;
            return this;
        }

        public TimeWindow build() {
            return new TimeWindow(start, startOffset, startOffsetInHours, startOffsetInDays, size, sizeInHours, sizeInDays);
        }
    }
}
