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
import java.util.Set;

/**
 * A {@code SimpleClassNameIdResolver} is a {@link TypeIdResolver} that allows simple class names to be
 * used as type ids, rather than needing to provide the entire class name.
 * This class will scan the class path to resolve the simple class names, if
 * a simple class name is provided. Once the class path has been scanned the
 * results should be cached in a static variable.
 */
public class SimpleClassNameIdResolver implements TypeIdResolver {
    /**
     * If true then simple class names are used for serialisation.
     * However, for large class paths this may be inefficient as reflection is
     * used to scan the class path to find sub classes.
     */
    private static boolean enableForSerialisation = true;

    private final Class parentClass;
    private Map<String, Set<String>> classes;
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
        final ClassNameIdResolver newDefaultResolver = new ClassNameIdResolver(this.baseType, null);
        newDefaultResolver.init(this.baseType);
        init(newDefaultResolver);
    }

    protected void init(final ClassNameIdResolver defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    @Override
    public String idFromValue(final Object value) {
        String id;
        if (enableForSerialisation) {
            id = value.getClass().getSimpleName();
            final Set<String> classesForId = getClasses().get(id);
            if (null == classesForId || 1 != classesForId.size()) {
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

    public Class getParentClass() {
        return parentClass;
    }

    private String expandId(final String id) {
        String expandedId = null;
        if (null != id && !id.contains(".")) {
            final Set<String> classes = getClasses().get(id);
            if (null != classes && !classes.isEmpty()) {
                if (1 == classes.size()) {
                    expandedId = classes.iterator().next();
                } else {
                    throw new IllegalArgumentException("Multiple " + id + " classes exist. Please choose one of the following and specify the full class name: " + classes);
                }
            }
        }
        if (null == expandedId) {
            expandedId = id;
        }
        return expandedId;
    }

    private Map<String, Set<String>> getClasses() {
        if (null == classes) {
            classes = ReflectionUtil.getSimpleClassNames(parentClass);
        }
        return classes;
    }
}
