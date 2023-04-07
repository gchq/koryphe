/*
 * Copyright 2018-2020 Crown Copyright
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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.serialisation.json.SimpleClassNameIdResolver;
import uk.gov.gchq.koryphe.util.IterableUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Objects.nonNull;

/**
 * A {@code ToSet} is a {@link java.util.function.Function} that takes
 * an Object and converts it to a set. If the object is an array or iterable
 * the items will be added to a new set. Otherwise a new set is created with
 * the single value as an item.
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Since("1.6.0")
@Summary("Converts an object into a Set")
public class ToSet extends KorypheFunction<Object, Set<?>> {
    public static final Class DEFAULT_IMPLEMENTATION = HashSet.class;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Class implementation;

    public ToSet()  {
        setImplementation(DEFAULT_IMPLEMENTATION);
    }

    public ToSet(final String implementationString) throws ClassNotFoundException {
        setImplementation(implementationString);
    }

    public ToSet(final Class implementation) {
        setImplementation(implementation);
    }

    @Override
    public Set<?> apply(final Object value) {
        final Set<Object> setImpl;

        if (implementation.isAssignableFrom(HashSet.class)) {
            setImpl = new HashSet<>();
        } else if (implementation.isAssignableFrom(TreeSet.class)) {
            setImpl = new TreeSet<>();
        } else {
            throw new IllegalArgumentException("Unrecognised Set implementation");
        }

        if (null == value && implementation.isAssignableFrom(TreeSet.class)) {
            return setImpl;
        } else if (implementation.isInstance(value)) {
           return (Set) value;
        } else if (value instanceof Object[]) {
            setImpl.addAll(new HashSet<>(Arrays.asList((Object[]) value)));
        } else if (value instanceof Iterable) {
            setImpl.addAll(new HashSet<>(IterableUtil.toList((Iterable) value)));
        } else {
            setImpl.add(value);
        }

        return setImpl;
    }

    public Class getImplementation() {
        return implementation;
    }

    public void setImplementation(final Class implementation) {
        this.implementation = nonNull(implementation) ? implementation : DEFAULT_IMPLEMENTATION;
    }

    @JsonSetter("implementation")
    public void setImplementation(final String implementationString) throws ClassNotFoundException {
        setImplementation(nonNull(implementationString) ? Class.forName(SimpleClassNameIdResolver.getClassName(implementationString)) : DEFAULT_IMPLEMENTATION);
    }

    @JsonGetter("implementation")
    public String getImplementationAsString() {
        return implementation.getTypeName();
    }
}
