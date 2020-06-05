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

package uk.gov.gchq.koryphe.impl.function;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.util.CloseableUtil;

import java.util.function.BinaryOperator;
import java.util.stream.StreamSupport;

import static java.util.Objects.nonNull;

/**
 * A {@code IterableFlatten} is a {@link java.util.function.Function} that takes items from an {@link Iterable} and
 * combines them into a single result based on a provided {@link BinaryOperator}
 *
 * @param <I_ITEM> the type of object in the iterable
 */
@Since("1.9.0")
@Summary("Combines the items in an iterable into a single item based on the supplied operator.")
public class IterableFlatten<I_ITEM> extends KorypheFunction<Iterable<I_ITEM>, I_ITEM> {

    private BinaryOperator<I_ITEM> operator;

    public IterableFlatten() {
    }

    public IterableFlatten(final BinaryOperator<I_ITEM> operator) {
        this.operator = operator;
    }

    @Override
    public I_ITEM apply(final Iterable<I_ITEM> items) {
        if (nonNull(items) && nonNull(operator)) {
            return StreamSupport.stream(items.spliterator(), false)
                    .onClose(() -> CloseableUtil.close(items))
                    .reduce(operator)
                    .orElse(null);
        }

        return null;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public BinaryOperator<I_ITEM> getOperator() {
        return operator;
    }

    public void setOperator(final BinaryOperator<I_ITEM> operator) {
        this.operator = operator;
    }
}
