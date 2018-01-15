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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

import java.io.IOException;
import java.util.Date;

/**
 * A {@code SimpleClassKeySerializer} is a {@link com.fasterxml.jackson.databind.JsonSerializer}
 * for {@link Class}es as Map keys, which utilises the {@link SimpleClassNameIdResolver} to
 * allow simple class names to be used.
 */
public class SimpleClassKeySerializer extends StdKeySerializer {
    private static final long serialVersionUID = -1765103660342721103L;

    // Largely copied from StdKeySerializer.serialise, with a slight change to use the simple class name.
    @Override
    public void serialize(final Object value, final JsonGenerator g, final SerializerProvider provider) throws IOException {
        String str;
        Class<?> cls = value.getClass();

        if (cls == String.class) {
            str = (String) value;
        } else if (cls.isEnum()) {
            // 24-Sep-2015, tatu: Minor improvement over older (2.6.2 and before) code: at least
            //     use name/toString() variation for as per configuration
            Enum<?> en = (Enum<?>) value;

            if (provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
                str = en.toString();
            } else {
                str = en.name();
            }
        } else if (value instanceof Date) {
            provider.defaultSerializeDateKey((Date) value, g);
            return;
        } else if (cls == Class.class) {
            str = SimpleClassNameCache.getSimpleClassName(((Class<?>) value));
        } else {
            str = value.toString();
        }
        g.writeFieldName(str);
    }
}
