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
package uk.gov.gchq.koryphe.serialisation.json;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;

import uk.gov.gchq.koryphe.util.ReflectionUtil;

import java.util.Map;

/**
 * A {@code SimpleClassNameIdResolver} is a {@link TypeIdResolver} that allows simple class names to be
 * used as type ids, rather than needing to provide the entire class name.
 * This class will scan the class path to resolve the simple class names, if
 * a simple class name is provided. Once the class path has been scanned the
 * results should be cached in a static variable.
 */
public abstract class SimpleClassNameIdResolver implements TypeIdResolver {
    /**
     * If true then simple class names are used for serialisation. This is
     * useful for testing and generating json examples.
     * However, it is not efficient as reflection is used to scan the class path
     * to find sub classes.
     */
    private static boolean enableForSerialisation = false;

    private final Class parentClass;
    private Map<String, Class> classes;
    private ClassNameIdResolver defaultResolver;
    private JavaType baseType;

    public SimpleClassNameIdResolver(final Class parentClass) {
        this.parentClass = parentClass;
    }

    public static void setEnableForSerialisation(final boolean enableForSerialisation) {
        SimpleClassNameIdResolver.enableForSerialisation = enableForSerialisation;
    }

    @Override
    public void init(final JavaType baseType) {
        this.baseType = baseType;
        if (null == baseType.getContentType()) {
            this.baseType = baseType;
        } else {
            this.baseType = baseType.getContentType();
        }
        defaultResolver = new ClassNameIdResolver(this.baseType, null);
        defaultResolver.init(this.baseType);
    }

    @Override
    public String idFromValue(final Object value) {
        String id;
        if (enableForSerialisation) {
            id = value.getClass().getSimpleName();
            if (!getClasses().containsKey(id)) {
                id = defaultResolver.idFromValue(value);
            }
        } else {
            id = defaultResolver.idFromValue(value);
        }
        return id;
    }

    @Override
    public String idFromValueAndType(final Object value, final Class<?> suggestedType) {
        String id;
        if (enableForSerialisation) {
            id = value.getClass().getSimpleName();
            if (!getClasses().containsKey(id)) {
                id = defaultResolver.idFromValueAndType(value, suggestedType);
            }
        } else {
            id = defaultResolver.idFromValue(value);
        }

        return id;
    }

    @Override
    public String idFromBaseType() {
        String id;
        if (enableForSerialisation) {
            id = baseType.getRawClass().getSimpleName();
            if (!getClasses().containsKey(id)) {
                id = defaultResolver.idFromBaseType();
            }
        } else {
            id = defaultResolver.idFromBaseType();
        }
        return id;
    }

    @Override
    public JavaType typeFromId(final String id) {
        return defaultResolver.typeFromId(expandId(id));
    }

    @Override
    public JavaType typeFromId(final DatabindContext context, final String id) {
        return defaultResolver.typeFromId(context, expandId(id));
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CLASS;
    }

    private String expandId(final String id) {
        String expandedId = null;
        if (null != id && !id.contains(".")) {
            final Class clazz = getClasses().get(id);
            if (null != clazz) {
                expandedId = clazz.getName();
            }
        }
        if (null == expandedId) {
            expandedId = id;
        }
        return expandedId;
    }

    private Map<String, Class> getClasses() {
        if (null == classes) {
            classes = ReflectionUtil.getSimpleClassNames(parentClass);
        }
        return classes;
    }
}
