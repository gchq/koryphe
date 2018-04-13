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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.tuple.predicate.KoryphePredicate2;
import uk.gov.gchq.koryphe.util.RangeUtil;

/**
 * <p>
 * A <code>InRangeDual</code> is a {@link java.util.function.Predicate}
 * that tests if a start {@link Comparable} and end {@link Comparable} is
 * within a provided range [start, end]. Specifically the start {@link Comparable}
 * has to be greater than the start bound and the end {@link Comparable} has to
 * be less than the end bound.
 * <p>
 * By default the range is inclusive, you can toggle this using the startInclusive
 * and endInclusive booleans.
 * </p>
 * <p>
 * If the start is not set then this will be treated as unbounded.
 * Similarly with the end.
 * </p>
 * <p>
 * If either of the test values are null then the predicate will return false.
 * </p>
 * <p>
 * This range predicate takes a 2 value to test - the start and end values.
 * If you want to test a single value lies within a range then you can use the
 * {@link InRange} predicate.
 * </p>
 *
 * @see Builder
 */
@JsonPropertyOrder(value = {"start", "end", "startInclusive", "endInclusive"}, alphabetic = true)
@JsonDeserialize(builder = InRangeDual.Builder.class)
@Since("1.1.0")
@Summary("Tests if a start and end comparable is within a provided range [start, end].")
public class InRangeDual<T extends Comparable<T>> extends KoryphePredicate2<Comparable<T>, Comparable<T>> {
    private T start;
    private T end;
    private Boolean startInclusive;
    private Boolean endInclusive;

    public void initialise() {
        if (null != getStart() && null != getEnd()
                && !getStart().getClass().equals(getEnd().getClass())) {
            throw new IllegalArgumentException("start and end should be instances of the same class");
        }
    }

    @Override
    public boolean test(final Comparable<T> startValue, final Comparable<T> endValue) {
        return RangeUtil.inRange(startValue, endValue, start, end, startInclusive, endInclusive);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public T getStart() {
        return start;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public T getEnd() {
        return end;
    }

    public Boolean isStartInclusive() {
        return startInclusive;
    }

    public Boolean isEndInclusive() {
        return endInclusive;
    }

    protected void setStart(final T start) {
        this.start = start;
    }

    protected void setEnd(final T end) {
        this.end = end;
    }

    protected void setStartInclusive(final Boolean startInclusive) {
        this.startInclusive = startInclusive;
    }

    protected void setEndInclusive(final Boolean endInclusive) {
        this.endInclusive = endInclusive;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final InRangeDual otherPredicate = (InRangeDual) obj;
        return new EqualsBuilder()
                .append(start, otherPredicate.start)
                .append(end, otherPredicate.end)
                .append(startInclusive, otherPredicate.startInclusive)
                .append(endInclusive, otherPredicate.endInclusive)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 31)
                .appendSuper(super.hashCode())
                .append(start)
                .append(end)
                .append(startInclusive)
                .append(endInclusive)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("start", start)
                .append("end", end)
                .append("startInclusive", startInclusive)
                .append("endInclusive", endInclusive)
                .toString();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class BaseBuilder<B extends BaseBuilder<B, R, T>, R extends InRangeDual<T>, T extends Comparable<T>> {
        protected final InRangeDual<T> predicate;

        public BaseBuilder(final R predicate) {
            this.predicate = predicate;
        }

        public B start(final T start) {
            predicate.setStart(start);
            return getSelf();
        }

        public B end(final T end) {
            predicate.setEnd(end);
            return getSelf();
        }

        public B startInclusive(final boolean startInclusive) {
            predicate.setStartInclusive(startInclusive);
            return getSelf();
        }

        public B endInclusive(final boolean endInclusive) {
            predicate.setEndInclusive(endInclusive);
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

    public static class Builder<T extends Comparable<T>> extends BaseBuilder<Builder<T>, InRangeDual<T>, T> {
        public Builder() {
            super(new InRangeDual<>());
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
        @Override
        public Builder<T> start(final T start) {
            return super.start(start);
        }

        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
        @Override
        public Builder<T> end(final T end) {
            return super.end(end);
        }
    }
}
