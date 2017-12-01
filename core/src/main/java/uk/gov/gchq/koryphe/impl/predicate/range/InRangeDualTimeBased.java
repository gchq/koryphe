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

import com.fasterxml.jackson.annotation.JsonGetter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.util.DateUtil;

import java.util.function.Function;

/**
 * <p>
 * An <code>InRangeDualTimeBased</code> is a {@link InRangeDual}
 * {@link java.util.function.Predicate} that can be configured using time offsets
 * from the current system time or a provided start/end time.
 * </p>
 * <p>
 * So you can set the start range bound using
 * the start value as normal, or using startOffsetInMillis, startOffsetInHours
 * or startOffsetInDays. At the point when test is called on the class the
 * current system time is used to calculate the start value based on:
 * System.currentTimeMillis() + offset.
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
 * @see InRangeDual
 */
public abstract class InRangeDualTimeBased<T extends Comparable<T>> extends InRangeDual<T> {
    public static final long HOURS_TO_MILLISECONDS = 60L * 60L * 1000L;
    public static final long DAYS_TO_MILLISECONDS = 24L * HOURS_TO_MILLISECONDS;

    private Long startOffsetInMillis;
    private Long startOffsetInHours;
    private Integer startOffsetInDays;
    private Long endOffsetInMillis;
    private Long endOffsetInHours;
    private Integer endOffsetInDays;

    private final Function<Long, T> toT;
    private final Function<T, Long> toLong;
    private Long startOffset;
    private Long endOffset;
    private String startString;
    private String endString;

    protected InRangeDualTimeBased() {
        this(t -> (T) t, l -> (Long) l);
    }

    protected InRangeDualTimeBased(final Function<Long, T> toT,
                                   final Function<T, Long> toLong) {
        this.toT = toT;
        this.toLong = toLong;
        calculateOffsets();
    }

    public static Long calculateOffset(final Long valueOffsetInMillis, final Long valueOffsetInHours, final Integer valueOffsetInDays, final String variableName) {
        final boolean isValueInvalid;
        if (null != valueOffsetInMillis) {
            isValueInvalid = null != valueOffsetInHours || null != valueOffsetInDays;
        } else {
            isValueInvalid = null != valueOffsetInHours && null != valueOffsetInDays;
        }
        if (isValueInvalid) {
            throw new IllegalArgumentException(String.format("Only set one of the following: %sOffsetInMillis, %sOffsetInHours or %sOffsetInDays", variableName, variableName, variableName));
        }

        final Long offset;
        if (null != valueOffsetInHours) {
            offset = HOURS_TO_MILLISECONDS * valueOffsetInHours;
        } else if (null != valueOffsetInDays) {
            offset = DAYS_TO_MILLISECONDS * valueOffsetInDays;
        } else {
            offset = valueOffsetInMillis;
        }

        return offset;
    }

    protected void calculateOffsets() {
        this.startOffset = calculateOffset(startOffsetInMillis, startOffsetInHours, startOffsetInDays, "start");
        this.endOffset = calculateOffset(endOffsetInMillis, endOffsetInHours, endOffsetInDays, "end");
    }

    protected T getValueFromOffset(final T value, final Long offset) {
        if (null == offset) {
            return value;
        }

        final long base = null != value ? toLong.apply(value) : System.currentTimeMillis();
        return toT.apply(base + offset);
    }

    @Override
    protected boolean testAgainstRange(final Comparable<T> startValue, final Comparable<T> endValue,
                                       final T rangeStart, final T rangeEnd) {
        return super.testAgainstRange(
                startValue,
                endValue,
                getValueFromOffset(rangeStart, startOffset),
                getValueFromOffset(rangeEnd, endOffset)
        );
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final InRangeDualTimeBased otherPredicate = (InRangeDualTimeBased) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(otherPredicate))
                .append(startOffsetInMillis, otherPredicate.startOffsetInMillis)
                .append(startOffsetInHours, otherPredicate.startOffsetInHours)
                .append(startOffsetInDays, otherPredicate.startOffsetInDays)
                .append(endOffsetInMillis, otherPredicate.endOffsetInMillis)
                .append(endOffsetInHours, otherPredicate.endOffsetInHours)
                .append(endOffsetInDays, otherPredicate.endOffsetInDays)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31)
                .appendSuper(super.hashCode())
                .append(startOffsetInMillis)
                .append(startOffsetInHours)
                .append(startOffsetInDays)
                .append(endOffsetInMillis)
                .append(endOffsetInHours)
                .append(endOffsetInDays)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("startOffsetInMillis", startOffsetInMillis)
                .append("startOffsetInHours", startOffsetInHours)
                .append("startOffsetInDays", startOffsetInDays)
                .append("endOffsetInMillis", endOffsetInMillis)
                .append("endOffsetInHours", endOffsetInHours)
                .append("endOffsetInDays", endOffsetInDays)
                .toString();
    }


    public Long getStartOffsetInMillis() {
        return startOffsetInMillis;
    }

    public Long getStartOffsetInHours() {
        return startOffsetInHours;
    }

    public Integer getStartOffsetInDays() {
        return startOffsetInDays;
    }

    public Long getEndOffsetInMillis() {
        return endOffsetInMillis;
    }

    public Long getEndOffsetInHours() {
        return endOffsetInHours;
    }

    public Integer getEndOffsetInDays() {
        return endOffsetInDays;
    }

    public String getStartString() {
        return startString;
    }

    public String getEndString() {
        return endString;
    }

    @JsonGetter("start")
    T getStartForJson() {
        if (null == startString) {
            return super.getStart();
        }

        return null;
    }

    @JsonGetter("end")
    T getEndForJson() {
        if (null == endString) {
            return super.getEnd();
        }

        return null;
    }

    protected Long getStartOffset() {
        return startOffset;
    }

    protected Long getEndOffset() {
        return endOffset;
    }

    protected void setStartString(final String startString) {
        this.startString = startString;
        setStart(toT.apply(DateUtil.parse(startString).getTime()));
    }

    protected void setStartOffsetInMillis(final Long startOffsetInMillis) {
        this.startOffsetInMillis = startOffsetInMillis;
    }

    protected void setStartOffsetInHours(final Long startOffsetInHours) {
        this.startOffsetInHours = startOffsetInHours;
    }

    protected void setStartOffsetInDays(final Integer startOffsetInDays) {
        this.startOffsetInDays = startOffsetInDays;
    }

    protected void setEndString(final String endString) {
        this.endString = endString;
        setEnd(toT.apply(DateUtil.parse(endString).getTime()));
    }

    protected void setEndOffsetInMillis(final Long endOffsetInMillis) {
        this.endOffsetInMillis = endOffsetInMillis;
    }

    protected void setEndOffsetInHours(final Long endOffsetInHours) {
        this.endOffsetInHours = endOffsetInHours;
    }

    protected void setEndOffsetInDays(final Integer endOffsetInDays) {
        this.endOffsetInDays = endOffsetInDays;
    }

    public abstract static class BaseBuilder<
            B extends BaseBuilder<B, R, T>,
            R extends InRangeDualTimeBased<T>,
            T extends Comparable<T>>
            extends InRangeDual.BaseBuilder<B, R, T> {

        public BaseBuilder(final R predicate) {
            super(predicate);
        }

        public B startString(final String startString) {
            getPredicate().setStartString(startString);
            return getSelf();
        }

        public B startOffsetInMillis(final Long startOffsetInMillis) {
            getPredicate().setStartOffsetInMillis(startOffsetInMillis);
            return getSelf();
        }

        public B startOffsetInHours(final Long startOffsetInHours) {
            getPredicate().setStartOffsetInHours(startOffsetInHours);
            return getSelf();
        }

        public B startOffsetInDays(final Integer startOffsetInDays) {
            getPredicate().setStartOffsetInDays(startOffsetInDays);
            return getSelf();
        }

        public B endString(final String endString) {
            getPredicate().setEndString(endString);
            return getSelf();
        }

        public B endOffsetInMillis(final Long endOffsetInMillis) {
            getPredicate().setEndOffsetInMillis(endOffsetInMillis);
            return getSelf();
        }

        public B endOffsetInHours(final Long endOffsetInHours) {
            getPredicate().setEndOffsetInHours(endOffsetInHours);
            return getSelf();
        }

        public B endOffsetInDays(final Integer endOffsetInDays) {
            getPredicate().setEndOffsetInDays(endOffsetInDays);
            return getSelf();
        }

        @Override
        public R build() {
            getPredicate().calculateOffsets();
            return super.build();
        }
    }
}
