/*
 * Copyright 2018 Crown Copyright
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

        if (!cache.containsKey(clazz)) {
            Map<Class, Method> newCache = new HashMap<>(cache);
            newCache.put(clazz, getMethodFromClass(clazz));
            cache = newCache;
        }

        try {
            return cache.get(clazz).invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Unable to invoke " + method + " on object class " + clazz, e);
        }
    }

    private Method getMethodFromClass(Class clazz) {
        try {
            return clazz.getMethod(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to invoke " + method + " on object class " + clazz, e);
        }
    }
}
