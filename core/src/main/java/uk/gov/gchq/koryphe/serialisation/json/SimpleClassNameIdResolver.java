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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;

/**
 * <p>A {@code SimpleClassNameIdResolver} is a {@link TypeIdResolver} that allows
 * simple class names to be used as type ids, rather than needing to provide the
 * entire class name.
 * </p>
 * <p>
 * If 2 classes are discovered to have the same simple class name then
 * a useful exception is thrown telling the user to choose which class they want
 * and use the full class name.
 * </p>
 *
 * @see SimpleClassNameCache
 */
public class SimpleClassNameIdResolver implements TypeIdResolver {
    private ClassNameIdResolver defaultResolver;

    private JavaType baseType;

    /**
     * Gets a simple class name for the given class.
     *
     * @param clazz the class to get the simple class name for.
     * @return the simple class name or the full class name if the clazz is
     * unknown or a conflict is found.
     */
    public static String getSimpleClassName(final Class<?> clazz) {
        return SimpleClassNameCache.getSimpleClassName(clazz);
    }

    /**
     * Gets a simple class name for the given class. If a conflict is found
     * or the simple class name is unknown then null is returned.
     *
     * @param clazz the class to get the simple class name for.
     * @return the simple class name, or null a simple class name can't be used.
     */
    public static String getSimpleClassNameOrNull(final Class<?> clazz) {
        return SimpleClassNameCache.getSimpleClassNameOrNull(clazz);
    }

    /**
     * Gets the class name for a given id.
     *
     * @param id the simple class name or full class name.
     * @return the full class name or the original id if one could not be found.
     * @throws IllegalArgumentException if there are multiple classes with the same id.
     */
    public static String getClassName(final String id) {
        return SimpleClassNameCache.getClassName(id);
    }

    /**
     * Gets the class name for a given id.
     *
     * @param id       the simple class name or full class name.
     * @param baseType the base type of the ID - used to help resolve conflicting IDs
     * @return the full class name or the original id if one could not be found.
     * @throws IllegalArgumentException if there are multiple classes with the same id.
     */
    public static String getClassName(final String id, final JavaType baseType) {
        return SimpleClassNameCache.getClassName(id, baseType);
    }

    /**
     * Call this method with an {@link ObjectMapper} to configure the object
     * mapper to use this {@link SimpleClassNameIdResolver}.
     *
     * @param mapper the object mapper to configure.
     */
    public static void configureObjectMapper(final ObjectMapper mapper) {
        mapper.setAnnotationIntrospector(new SimpleClassNamedIdAnnotationIntrospector());
        mapper.registerModule(SimpleClassSerializer.getModule());
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
        String id = getSimpleClassNameOrNull(value.getClass());
        if (null == id) {
            id = defaultResolver.idFromValue(value);
        }
        return id;
    }

    @Override
    public String idFromValueAndType(final Object value, final Class<?> suggestedType) {
        String id = getSimpleClassNameOrNull(value.getClass());
        if (null == id) {
            id = defaultResolver.idFromValueAndType(value, suggestedType);
        }
        return id;
    }

    @Override
    public String idFromBaseType() {
        String id = getSimpleClassNameOrNull(baseType.getRawClass());
        if (null == id) {
            id = defaultResolver.idFromBaseType();
        }
        return id;
    }

    @Override
    public JavaType typeFromId(final String id) {
        return defaultResolver.typeFromId(getClassName(id, baseType));
    }

    @Override
    public JavaType typeFromId(final DatabindContext context, final String id) {
        return defaultResolver.typeFromId(context, getClassName(id, baseType));
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CLASS;
    }
}
