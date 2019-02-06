/*
 * Copyright 2016-2019 Crown Copyright
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

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;

/**
 * A {@code SimpleClassDeserializer} is a {@link com.fasterxml.jackson.databind.JsonDeserializer}
 * for {@link Class}es, which utilises the {@link SimpleClassNameIdResolver} to
 * allow simple class names to be used.
 */
public class SimpleClassDeserializer extends FromStringDeserializer<Class> {
    private static final long serialVersionUID = 1714355399436671951L;

    public SimpleClassDeserializer() {
        super(Class.class);
    }

    @Override
    protected Class<?> _deserialize(final String value, final DeserializationContext ctxt) throws IOException {
        try {
            return ctxt.findClass(SimpleClassNameIdResolver.getClassName(value));
        } catch (final Exception e) {
            throw ctxt.instantiationException(_valueClass, ClassUtil.getRootCause(e));
        }
    }
}
