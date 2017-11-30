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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>
 * An <code>InRangeWithTimeOffsets</code> is a {@link InRange}
 * {@link java.util.function.Predicate} that can be configured using time offsets
 * from the current system time.
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
 */
public abstract class InRangeWithTimeOffsets<T extends Comparable<T>> extends InRange<T> {
    protected InRangeWithTimeOffsets(final InRangeDualWithTimeOffsets<T> predicate) {
        super(predicate);
    }

    public Long getStartOffsetInMillis() {
        return getPredicate().getStartOffsetInMillis();
    }

    public Long getStartOffsetInHours() {
        return getPredicate().getStartOffsetInHours();
    }

    public Integer getStartOffsetInDays() {
        return getPredicate().getStartOffsetInDays();
    }

    public Long getEndOffsetInMillis() {
        return getPredicate().getEndOffsetInMillis();
    }

    public Long getEndOffsetInHours() {
        return getPredicate().getEndOffsetInHours();
    }

    public Integer getEndOffsetInDays() {
        return getPredicate().getEndOffsetInDays();
    }

    protected Long getStartOffset() {
        return getPredicate().getStartOffset();
    }

    protected Long getEndOffset() {
        return getPredicate().getEndOffset();
    }

    @SuppressFBWarnings(value = "BC_UNCONFIRMED_CAST_OF_RETURN_VALUE", justification = "The predicate will always be of this type.")
    @Override
    protected InRangeDualWithTimeOffsets<T> getPredicate() {
        return (InRangeDualWithTimeOffsets<T>) super.getPredicate();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .toString();
    }

    public abstract static class BaseBuilder<
            B extends BaseBuilder<B, R, T>,
            R extends InRangeWithTimeOffsets<T>,
            T extends Comparable<T>>
            extends InRangeDualWithTimeOffsets.BaseBuilder<B, R, T> {
    }
}
