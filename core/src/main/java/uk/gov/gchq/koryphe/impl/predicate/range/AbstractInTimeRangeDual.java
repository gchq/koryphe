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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.tuple.predicate.KoryphePredicate2;
import uk.gov.gchq.koryphe.util.DateUtil;
import uk.gov.gchq.koryphe.util.RangeUtil;
import uk.gov.gchq.koryphe.util.TimeUnit;

import java.util.function.Function;

/**
 * <p>
 * An <code>AbstractInTimeRangeDual</code> is a {@link java.util.function.Predicate}
 * that tests if a start {@link Comparable} and end {@link Comparable} is
 * within a provided range [start, end]. Specifically the start {@link Comparable}
 * has to be greater than the start bound and the end {@link Comparable} has to
 * be less than the end bound.
 * By default the range is inclusive, you can toggle this using the startInclusive
 * and endInclusive booleans.
 * </p>
 * <p>
 * If the start is not set then this will be treated as unbounded.
 * Similarly with the end.
 * </p>
 * <p>
 * If the test value is null then the predicate will return false.
 * </p>
 * <p>
 * This range predicate takes 2 values to test, if you want to test
 * a single value lies within a range then you can use the
 * {@link AbstractInTimeRange} predicate.
 * </p>
 * <p>
 * The range can also be configured using time offsets
 * from the current system time or a provided start/end time.
 * You can set the start and end offsets using startOffset and endOffset.
 * By default the offset is measured in Days, this can be changed to
 * DAY, HOUR, MINUTE, SECOND, MILLISECOND and MICROSECOND using the offsetUnit field.
 * <p>
 * At the point when test is called on the class the
 * current system time is used to calculate the start and end values based on:
 * System.currentTimeMillis() + offset.
 * </p>
 * <p>
 * By default checks are carried out assuming the data will be in milliseconds.
 * If this is not the case you can change the time unit using the timeUnit property.
 * </p>
 * <p>
 * You can configure the start and end time strings using one of the following formats:
 * </p>
 * <ul>
 * <li>timestamp in milliseconds</li>
 * <li>yyyy/MM</li>
 * <li>yyyy/MM/dd</li>
 * <li>yyyy/MM/dd HH</li>
 * <li>yyyy/MM/dd HH:mm</li>
 * <li>yyyy/MM/dd HH:mm:ss</li>
 * </ul>
 * You can use a space, '-', '/', '_', ':', '|', or '.' to separate the parts.
 */
public abstract class AbstractInTimeRangeDual<T extends Comparable<T>> extends KoryphePredicate2<Comparable<T>, Comparable<T>> {
    private String start;
    private Long startOffset;
    private Boolean startInclusive;
    private Boolean startFullyContained;

    private String end;
    private Long endOffset;
    private Boolean endInclusive;
    private Boolean endFullyContained;

    private TimeUnit offsetUnit;
    private TimeUnit timeUnit = TimeUnit.MILLISECOND;


    private Long startTime;
    private Long startOffsetTime;
    private Long endTime;
    private Long endOffsetTime;

    private final Function<Long, T> toT;

    protected AbstractInTimeRangeDual() {
        this(t -> (T) t);
    }

    protected AbstractInTimeRangeDual(final Function<Long, T> toT) {
        this.toT = toT;
    }

    public void initialise() {
        this.startTime = timeUnit.fromMilliSeconds(DateUtil.parseTime(start));
        this.endTime = timeUnit.fromMilliSeconds(DateUtil.parseTime(end));
        this.startOffsetTime = timeUnit.fromMilliSeconds(TimeUnit.asMilliSeconds(offsetUnit, startOffset));
        this.endOffsetTime = timeUnit.fromMilliSeconds(TimeUnit.asMilliSeconds(offsetUnit, endOffset));
    }

    @Override
    public boolean test(final Comparable<T> startValue, final Comparable<T> endValue) {
        return RangeUtil.inRange(
                startValue,
                endValue,
                getValueFromOffset(startTime, startOffsetTime),
                getValueFromOffset(endTime, endOffsetTime),
                startInclusive,
                endInclusive,
                startFullyContained,
                endFullyContained
        );
    }

    private T getValueFromOffset(final Long value, final Long offset) {
        if (null == offset) {
            return null != value ? toT.apply(value) : null;
        }

        final long base = null != value ? value : timeUnit.fromMilliSeconds(System.currentTimeMillis());
        return toT.apply(base + offset);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final AbstractInTimeRangeDual otherPredicate = (AbstractInTimeRangeDual) obj;
        return new EqualsBuilder()
                .append(start, otherPredicate.start)
                .append(startOffset, otherPredicate.startOffset)
                .append(startInclusive, otherPredicate.startInclusive)
                .append(startFullyContained, otherPredicate.startFullyContained)
                .append(end, otherPredicate.end)
                .append(endOffset, otherPredicate.endOffset)
                .append(endInclusive, otherPredicate.endInclusive)
                .append(endFullyContained, otherPredicate.endFullyContained)
                .append(offsetUnit, otherPredicate.offsetUnit)
                .append(timeUnit, otherPredicate.timeUnit)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31)
                .appendSuper(super.hashCode())
                .append(start)
                .append(startOffset)
                .append(startInclusive)
                .append(startFullyContained)
                .append(end)
                .append(endOffset)
                .append(endInclusive)
                .append(endFullyContained)
                .append(offsetUnit)
                .append(timeUnit)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("start", start)
                .append("startOffset", startOffset)
                .append("startInclusive", startInclusive)
                .append("startFullyContained", startFullyContained)
                .append("end", end)
                .append("endOffset", endOffset)
                .append("endInclusive", endInclusive)
                .append("endFullyContained", endFullyContained)
                .append("offsetUnit", offsetUnit)
                .append("timeUnit", timeUnit)
                .toString();
    }

    public Long getStartOffset() {
        return startOffset;
    }

    public String getStart() {
        return start;
    }

    public Boolean isStartInclusive() {
        return startInclusive;
    }

    public Boolean isStartFullyContained() {
        return startFullyContained;
    }

    public String getEnd() {
        return end;
    }

    public Long getEndOffset() {
        return endOffset;
    }

    public Boolean isEndInclusive() {
        return endInclusive;
    }

    public Boolean isEndFullyContained() {
        return endFullyContained;
    }

    public TimeUnit getOffsetUnit() {
        return offsetUnit;
    }

    @JsonInclude(Include.NON_DEFAULT)
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    protected void setTimeUnit(final TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    protected void setStart(final String start) {
        this.start = start;
    }

    protected void setStartOffset(final Long startOffset) {
        this.startOffset = startOffset;
    }

    protected void setStartInclusive(final Boolean startInclusive) {
        this.startInclusive = startInclusive;
    }

    protected void setStartFullyContained(final Boolean startFullyContained) {
        this.startFullyContained = startFullyContained;
    }

    protected void setEnd(final String end) {
        this.end = end;
    }

    protected void setEndOffset(final Long endOffset) {
        this.endOffset = endOffset;
    }

    protected void setEndInclusive(final Boolean endInclusive) {
        this.endInclusive = endInclusive;
    }

    protected void setEndFullyContained(final Boolean endFullyContained) {
        this.endFullyContained = endFullyContained;
    }

    protected void setOffsetUnit(final TimeUnit offsetUnit) {
        this.offsetUnit = offsetUnit;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class BaseBuilder<
            B extends BaseBuilder<B, R, T>,
            R extends AbstractInTimeRangeDual<T>,
            T extends Comparable<T>> {
        protected final AbstractInTimeRangeDual<T> predicate;

        public BaseBuilder(final R predicate) {
            this.predicate = predicate;
        }

        public B start(final String start) {
            predicate.setStart(start);
            return getSelf();
        }

        public B startOffset(final Long startOffset) {
            getPredicate().setStartOffset(startOffset);
            return getSelf();
        }

        public B startInclusive(final boolean startInclusive) {
            predicate.setStartInclusive(startInclusive);
            return getSelf();
        }

        public B startFullyContained(final boolean startFullyContained) {
            predicate.setStartFullyContained(startFullyContained);
            return getSelf();
        }

        public B end(final String end) {
            predicate.setEnd(end);
            return getSelf();
        }

        public B endOffset(final Long endOffset) {
            getPredicate().setEndOffset(endOffset);
            return getSelf();
        }

        public B endInclusive(final boolean endInclusive) {
            predicate.setEndInclusive(endInclusive);
            return getSelf();
        }

        public B endFullyContained(final boolean endFullyContained) {
            predicate.setEndFullyContained(endFullyContained);
            return getSelf();
        }

        public B offsetUnit(final TimeUnit timeUnit) {
            getPredicate().setOffsetUnit(timeUnit);
            return getSelf();
        }

        public B timeUnit(final TimeUnit timeUnit) {
            getPredicate().setTimeUnit(timeUnit);
            return getSelf();
        }

        public R build() {
            predicate.initialise();
            return (R) predicate;
        }

        protected B getSelf() {
            return (B) this;
        }

        protected R getPredicate() {
            return (R) predicate;
        }
    }
}
