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

package uk.gov.gchq.koryphe.serialisation.json;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.google.common.primitives.UnsignedLong;
import org.junit.Before;
import org.junit.Test;

import uk.gov.gchq.koryphe.serialisation.json.first.TestCustomNumber;
import uk.gov.gchq.koryphe.util.ReflectionUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SimpleClassNameIdResolverTest {

    private SimpleClassNameIdResolver resolver;
    private ClassNameIdResolver defaultResolver;

    @Before
    public void before() {
        ReflectionUtil.resetPaths();
        resolver = new SimpleClassNameIdResolver(Number.class);
        defaultResolver = mock(ClassNameIdResolver.class);
        resolver.init(defaultResolver);
    }

    @Test
    public void shouldUseClassMechanism() {
        // When
        final JsonTypeInfo.Id mechanism = resolver.getMechanism();

        // Then
        assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }

    @Test
    public void shouldGetSimpleClassNameFromValue() {
        // Given
        ReflectionUtil.addPaths(UnsignedLong.class.getPackage().getName());
        final UnsignedLong value = UnsignedLong.asUnsigned(1L);

        // When
        final String id = resolver.idFromValue(value);

        // Then
        assertEquals(UnsignedLong.class.getSimpleName(), id);
    }

    @Test
    public void shouldGetFullClassNameFromValueWithConflicts() {
        // Given
        final TestCustomNumber value = new TestCustomNumber();
        final String expectedId = TestCustomNumber.class.getSimpleName();
        given(defaultResolver.idFromValue(value)).willReturn(expectedId);

        // When
        final String id = resolver.idFromValue(value);

        // Then
        assertEquals(expectedId, id);
    }

    @Test
    public void shouldGetFullClassNameFromUnknownValue() {
        // Given
        final UnsignedLong value = UnsignedLong.asUnsigned(1L);
        final String expectedId = UnsignedLong.class.getSimpleName();
        given(defaultResolver.idFromValue(value)).willReturn(expectedId);

        // When
        final String id = resolver.idFromValue(value);

        // Then
        assertEquals(expectedId, id);
    }

    @Test
    public void shouldNotExpandClassesFromDefaultJavaPackages() {
        // Given
        final DatabindContext context = mock(DatabindContext.class);

        final String id = Long.class.getSimpleName();

        // When
        resolver.typeFromId(context, id);

        // Then
        verify(defaultResolver).typeFromId(context, id);
    }

    @Test
    public void shouldNotExpandClassesFromOtherPackages() {
        // Given
        final DatabindContext context = mock(DatabindContext.class);
        final String id = UnsignedLong.class.getSimpleName();

        // When
        resolver.typeFromId(context, id);

        // Then
        verify(defaultResolver).typeFromId(context, id);
    }

    @Test
    public void shouldExpandClassesFromOtherPathsWhenAddedToReflectionClassPaths() {
        // Given
        ReflectionUtil.addPaths(UnsignedLong.class.getPackage().getName());
        final DatabindContext context = mock(DatabindContext.class);
        final String id = UnsignedLong.class.getSimpleName();

        // When
        resolver.typeFromId(context, id);

        // Then
        verify(defaultResolver).typeFromId(context, UnsignedLong.class.getName());
    }

    @Test
    public void shouldThrowExceptionIfDuplicateClassesFound() {
        // Given
        final DatabindContext context = mock(DatabindContext.class);
        final String id = TestCustomNumber.class.getSimpleName();

        // When / Then
        try {
            resolver.typeFromId(context, id);
            fail("Exception expected");
        } catch (final IllegalArgumentException e) {
            final String msg = e.getMessage();
            assertTrue(msg,
                    msg.contains("Please choose one of the following")
                            && msg.contains(TestCustomNumber.class.getName())
                            && msg.contains(uk.gov.gchq.koryphe.serialisation.json.second.TestCustomNumber.class.getName()));
        }
    }
}
