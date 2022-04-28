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

import com.fasterxml.jackson.databind.DeserializationContext;
import com.google.common.primitives.UnsignedLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import uk.gov.gchq.koryphe.util.ReflectionUtil;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SimpleClassKeyDeserializerTest {

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
    public void shouldDeserialiseFromSimpleClassName() throws IOException, ClassNotFoundException {
        // Given
        final SimpleClassKeyDeserializer deserialiser = new SimpleClassKeyDeserializer();
        final DeserializationContext context = mock(DeserializationContext.class);
        final Class expectedClass = IsA.class;
        final String id = expectedClass.getSimpleName();
        given(context.findClass(expectedClass.getName())).willReturn(expectedClass);

        // When
        final Class<?> clazz = (Class<?>) deserialiser.deserializeKey(id, context);

        // Then
        assertThat(clazz).isEqualTo(expectedClass);
        verify(context).findClass(expectedClass.getName());
    }

    @Test
    public void shouldDeserialiseFromFullClassName() throws IOException, ClassNotFoundException {
        // Given
        final SimpleClassKeyDeserializer deserialiser = new SimpleClassKeyDeserializer();
        final DeserializationContext context = mock(DeserializationContext.class);
        final Class expectedClass = IsA.class;
        final String id = expectedClass.getName();
        given(context.findClass(expectedClass.getName())).willReturn(expectedClass);

        // When
        final Class<?> clazz = (Class<?>) deserialiser.deserializeKey(id, context);

        // Then
        assertThat(clazz).isEqualTo(expectedClass);
        verify(context).findClass(expectedClass.getName());
    }

    @Test
    public void shouldDeserialiseFromFullClassNameForUnknownClass() throws IOException, ClassNotFoundException {
        // Given
        final SimpleClassKeyDeserializer deserialiser = new SimpleClassKeyDeserializer();
        final DeserializationContext context = mock(DeserializationContext.class);
        final Class expectedClass = UnsignedLong.class;
        final String id = expectedClass.getName();
        given(context.findClass(expectedClass.getName())).willReturn(expectedClass);

        // When
        final Class<?> clazz = (Class<?>) deserialiser.deserializeKey(id, context);

        // Then
        assertThat(clazz).isEqualTo(expectedClass);
        verify(context).findClass(expectedClass.getName());
    }
}
