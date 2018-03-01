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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;

/**
 * <p>
 * An <code>InRange</code> is a {@link java.util.function.Predicate}
 * that tests if a {@link Comparable} is within a provided range [start, end].
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
 * This range predicate takes a single value to test, if you want to test
 * a startValue and endValue lies within a range then you can use the
 * {@link InRangeDual} predicate.
 * </p>
 *
 * @see Builder
 */
@JsonPropertyOrder(value = {"start", "end", "startInclusive", "endInclusive"}, alphabetic = true)
@JsonDeserialize(builder = InRange.Builder.class)
@Since("1.1.0")
public class InRange<T extends Comparable<T>> extends KoryphePredicate<T> {
    private final InRangeDual<T> predicate;

    public InRange() {
        predicate = new InRangeDual<>();
    }

    @Override
    public boolean test(final T value) {
        return predicate.test(value, value);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public T getStart() {
        return predicate.getStart();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public T getEnd() {
        return predicate.getEnd();
    }

    public Boolean isStartInclusive() {
        return predicate.isStartInclusive();
    }

    public Boolean isEndInclusive() {
        return predicate.isEndInclusive();
    }

    protected InRangeDual<T> getPredicate() {
        return predicate;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final InRange otherPredicate = (InRange) obj;
        return new EqualsBuilder()
                .append(predicate, otherPredicate.predicate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 31)
                .appendSuper(predicate.hashCode())
                .append(predicate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(predicate.toString())
                .toString();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class BaseBuilder<B extends BaseBuilder<B, R, T>, R extends InRange<T>, T extends Comparable<T>> {
        protected final InRange<T> predicate;

        public BaseBuilder(final R predicate) {
            this.predicate = predicate;
        }

        public B start(final T start) {
            predicate.getPredicate().setStart(start);
            return getSelf();
        }

        public B end(final T end) {
            predicate.getPredicate().setEnd(end);
            return getSelf();
        }

        public B startInclusive(final boolean startInclusive) {
            predicate.getPredicate().setStartInclusive(startInclusive);
            return getSelf();
        }

        public B endInclusive(final boolean endInclusive) {
            predicate.getPredicate().setEndInclusive(endInclusive);
            return getSelf();
        }

        public R build() {
            if (null != predicate.getStart() && null != predicate.getEnd()
                    && !predicate.getStart().getClass().equals(predicate.getEnd().getClass())) {
                throw new IllegalArgumentException("start and end should be instances of the same class");
            }
            return (R) predicate;
        }

        protected B getSelf() {
            return (B) this;
        }

        protected R getPredicate() {
            return (R) predicate;
        }
    }

    public static class Builder<T extends Comparable<T>> extends BaseBuilder<Builder<T>, InRange<T>, T> {
        public Builder() {
            super(new InRange<>());
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
