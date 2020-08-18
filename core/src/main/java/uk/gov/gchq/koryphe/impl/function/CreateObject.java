/*
 * Copyright 2019-2020 Crown Copyright
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static java.util.Objects.isNull;

/**
 * A {@code CreateObject} is a {@link java.util.function.Function} that takes
 * an optional Object and creates an new instance of a class. It uses reflection
 * to instantiate an new object given a class name. If an object is provided
 * and it isn't null then 'new TheProvidedClassName(object)' is called. Otherwise
 * the no-arg constructor is used.
 */
@Since("1.7.0")
@Summary("Creates a new object of the given type")
public class CreateObject extends KorypheFunction<Object, Object> {

    private Class<?> objectClass;

    public CreateObject() {
    }

    public CreateObject(final Class<?> objectClass) {
        this.objectClass = objectClass;
    }

    @Override
    public Object apply(final Object value) {
        if (isNull(objectClass)) {
            return null;
        }

        if (isNull(value)) {
            try {
                return objectClass.newInstance();
            } catch (final InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Unable to create a new instance of " + objectClass.getName() + " using the no-arg constructor", e);
            }
        }

        for (final Constructor<?> constructor : objectClass.getConstructors()) {
            if (constructor.getParameterTypes().length == 1
                    && constructor.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
                try {
                    return constructor.newInstance(value);
                } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Unable to create a new instance of " + objectClass.getName() + " using the constructor with single argument type " + value.getClass().getName(), e);
                }
            }
        }

        throw new RuntimeException("Unable to create a new instance of " + objectClass.getName() + ". No constructors were found that accept a " + value.getClass().getName());
    }

    public Class<?> getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(final Class<?> objectClass) {
        this.objectClass = objectClass;
    }

    @Override
    public boolean equals(final Object o) {
        if (!super.equals(o)) {
            return false; // Does exact equals and Class checking
        }

        CreateObject that = (CreateObject) o;
        return new EqualsBuilder()
                .append(objectClass, that.objectClass)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 29)
                .append(super.hashCode())
                .append(objectClass)
                .toHashCode();
    }
}
