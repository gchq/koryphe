/*
 * Copyright 2017-2020 Crown Copyright
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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.util.IterableUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * An {@code IterableFunction} is a {@link KorypheFunction} which lazily applies
 * a supplied {@link Function}, or {@link List} of {@link Function}s
 * to each object in the input {@link Iterable}, returning
 * an output {@link Iterable}.
 *
 * @param <I_ITEM> the type of objects in the input iterable
 * @param <O_ITEM> the type of objects in the output iterable
 */
@Since("1.1.0")
@Summary("Lazily applies functions to each item in an iterable")
public class IterableFunction<I_ITEM, O_ITEM> extends KorypheFunction<Iterable<I_ITEM>, Iterable<O_ITEM>> {
    private List<Function> functions = new ArrayList<>();

    public IterableFunction() {
        // empty
    }

    public IterableFunction(final Function function) {
        functions.add(function);
    }

    public IterableFunction(final List<Function> functions) {
        this.functions = functions;
    }

    @Override
    public Iterable<O_ITEM> apply(final Iterable<I_ITEM> items) {
        return IterableUtil.map(items, functions);
    }


    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(final List<Function> functions) {
        this.functions = functions;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || getClass() != obj.getClass()) {
            return false;
        }

        final IterableFunction func = (IterableFunction) obj;

        return new EqualsBuilder()
                .append(functions, func.functions)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 71)
                .append(functions)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("functions", functions)
                .toString();
    }

    public static final class Builder<I_ITEM> {
        private final List<Function> functions = new ArrayList<>();

        public <O_ITEM> OutputBuilder<I_ITEM, O_ITEM> first(final Function<I_ITEM, O_ITEM> function) {
            return new OutputBuilder<>(function, functions);
        }
    }

    public static final class OutputBuilder<I_ITEM, O_ITEM> {
        private final List<Function> functions;

        private OutputBuilder(final Function<I_ITEM, O_ITEM> function, final List<Function> functions) {
            this.functions = functions;
            functions.add(function);
        }

        public <NEXT> OutputBuilder<I_ITEM, NEXT> then(final Function<? super O_ITEM, NEXT> function) {
            return new OutputBuilder(function, functions);
        }

        public IterableFunction<I_ITEM, O_ITEM> build() {
            return new IterableFunction<>(functions);
        }
    }
}
