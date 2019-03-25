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
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

/**
 * A {@code SimpleClassDeserializer} is a {@link com.fasterxml.jackson.databind.JsonSerializer}
 * for {@link Class}es, which utilises the {@link SimpleClassNameIdResolver} to
 * allow simple class names to be used.
 */
public class SimpleClassKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(final String key, final DeserializationContext ctxt) throws IOException {
        try {
            return ctxt.findClass(SimpleClassNameIdResolver.getClassName(key));
        } catch (final Exception e) {
            throw ctxt.mappingException("Cannot find class %s", key);
        }
    }
}
