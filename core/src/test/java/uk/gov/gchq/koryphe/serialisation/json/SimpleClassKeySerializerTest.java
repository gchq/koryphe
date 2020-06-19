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

package uk.gov.gchq.koryphe.serialisation.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.primitives.UnsignedLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import uk.gov.gchq.koryphe.util.ReflectionUtil;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SimpleClassKeySerializerTest {

    @BeforeEach
    @AfterEach
    public void beforeAndAfter() {
        SimpleClassNameCache.setUseFullNameForSerialisation(true);
        SimpleClassNameCache.reset();
        JsonSerialiser.resetMapper();
        ReflectionUtil.resetReflectionPackages();
        ReflectionUtil.resetReflectionCache();
    }

    @Test
    public void shouldSerialiseToSimpleClassName() throws IOException {
        // Given
        SimpleClassNameCache.setUseFullNameForSerialisation(false);
        final SimpleClassKeySerializer serialiser = new SimpleClassKeySerializer();
        final JsonGenerator jgen = mock(JsonGenerator.class);
        final Class clazz = IsA.class;

        // When
        serialiser.serialize(clazz, jgen, null);

        // Then
        verify(jgen).writeFieldName(clazz.getSimpleName());
    }

    @Test
    public void shouldSerialiseToFullClassName() throws IOException {
        // Given
        SimpleClassNameCache.setUseFullNameForSerialisation(true);
        final SimpleClassKeySerializer serialiser = new SimpleClassKeySerializer();
        final JsonGenerator jgen = mock(JsonGenerator.class);
        final Class clazz = IsA.class;

        // When
        serialiser.serialize(clazz, jgen, null);

        // Then
        verify(jgen).writeFieldName(clazz.getName());
    }

    @Test
    public void shouldSerialiseToFullClassNameWhenUnknown() throws IOException {
        // Given
        SimpleClassNameCache.setUseFullNameForSerialisation(false);
        final SimpleClassKeySerializer serialiser = new SimpleClassKeySerializer();
        final JsonGenerator jgen = mock(JsonGenerator.class);
        final Class clazz = UnsignedLong.class;

        // When
        serialiser.serialize(clazz, jgen, null);

        // Then
        verify(jgen).writeFieldName(clazz.getName());
    }
}
