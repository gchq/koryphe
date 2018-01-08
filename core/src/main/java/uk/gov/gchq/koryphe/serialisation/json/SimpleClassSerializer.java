/*
 * Copyright 2016 Crown Copyright
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ClassSerializer;

import java.io.IOException;

public class SimpleClassSerializer extends ClassSerializer {
    private static final long serialVersionUID = -2838787535145218110L;

    public static SimpleModule getModule() {
        final SimpleModule module = new SimpleModule();
        module.addSerializer(Class.class, (JsonSerializer) new SimpleClassSerializer());
        module.addDeserializer(Class.class, new SimpleClassDeserializer());
        module.addKeySerializer(Class.class, (JsonSerializer) new SimpleClassKeySerializer());
        module.addKeyDeserializer(Class.class, new SimpleClassKeyDeserializer());
        return module;
    }

    @Override
    public void serialize(final Class<?> value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeString(SimpleClassNameCache.getSimpleClassName(value));
    }
}
