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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * A <code>CallMethod</code> is a {@link java.util.function.Function} that takes
 * a method name and then invokes it.
 * <p>
 * The resulting object is what is returned from the method.
 */
@Since("1.4.0")
@Summary("Calls a supplied method")
public class CallMethod extends KorypheFunction<Object, Object> {
    private String method;
    private Map<Class, Method> cache = new HashMap<>();

    public CallMethod() {
    }

    public CallMethod(final String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    @Override
    public Object apply(final Object obj) {
        if (null == obj) {
            return null;
        }

        Class clazz = obj.getClass();
        Method method = cache.get(clazz);

        if (null == method) {
            method = getMethodFromClass(clazz);
            Map<Class, Method> newCache = new HashMap<>(cache);
            newCache.put(clazz, method);
            cache = newCache;
        }

        try {
            return method.invoke(obj);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Unable to invoke " + getMethod() + " on object class " + clazz, e);
        }
    }

    protected Map<Class, Method> getCache() {
        return cache;
    }

    private Method getMethodFromClass(final Class clazz) {
        try {
            return clazz.getMethod(getMethod());
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException("Unable to invoke " + getMethod() + " on object class " + clazz, e);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!super.classEquals(o)) {
            return false; // Does class checking
        }

        CallMethod that = (CallMethod) o;
        return new EqualsBuilder()
                .append(method, that.method)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(23, 67)
                .appendSuper(super.hashCode())
                .append(method)
                .toHashCode();
    }
}
