/*
 * Copyright 2020 Crown Copyright
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

package uk.gov.gchq.koryphe.adapted;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A {@code StateAgnosticOutputAdapter} is a {@link BiFunction} which ignores the first argument (the state)
 * and applies a provided function to the output. By default the output is returned.
 * @param <T> The type of the unused state
 * @param <U> The type of the input
 * @param <R> The type of the output
 */
@Since("1.11.0")
@Summary("Adapts an output without considering the state")
public class StateAgnosticOutputAdapter<T, U, R> implements BiFunction<T, U, R> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    private Function<U, R> adapter;

    public StateAgnosticOutputAdapter() {
        // required for Json Serialisation
    }

    public StateAgnosticOutputAdapter(final Function<U, R> adapter) {
        this.adapter = adapter;
    }

    @Override
    public R apply(final T ignoredState, final U output) {
        if (adapter == null) {
            return (R) output;
        }
        return adapter.apply(output);
    }

    public Function<U, R> getAdapter() {
        return adapter;
    }

    public void setAdapter(final Function<U, R> adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final StateAgnosticOutputAdapter that = (StateAgnosticOutputAdapter) o;

        return new EqualsBuilder()
                .append(adapter, that.adapter)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getClass())
                .append(adapter)
                .toHashCode();
    }
}
