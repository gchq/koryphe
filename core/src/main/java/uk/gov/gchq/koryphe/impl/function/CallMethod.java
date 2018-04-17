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
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A <code>CallMethod</code> is a {@link java.util.function.Function} that takes
 * a method name and then invokes it.
 * <p>
 * The resulting object is what is returned from the method.
 */
@Since("1.3.1")
public class CallMethod extends KorypheFunction<Object, Object> {
    private String method;

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

        try {
            final Method objMethod = obj.getClass().getMethod(method);
            return objMethod.invoke(obj);
        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Unable to invoke " + method + " on object class " + obj.getClass(), e);
        }
    }
}
