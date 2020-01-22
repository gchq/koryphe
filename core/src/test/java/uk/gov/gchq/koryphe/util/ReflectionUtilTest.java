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

package uk.gov.gchq.koryphe.util;

import com.google.common.collect.Sets;
import com.google.common.primitives.UnsignedLong;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.gov.gchq.koryphe.serialisation.json.obj.first.TestCustomNumber;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ReflectionUtilTest {
    @Before
    @After
    public void cleanUp() {
        ReflectionUtil.resetReflectionPackages();
        ReflectionUtil.resetReflectionCache();
    }

    @Test
    public void shouldReturnUnmodifiableClasses() throws IOException {
        // Given
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // When / Then
        try {
            subclasses.add(String.class);
            fail("Exception expected");
        } catch (final UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void shouldReturnUnmodifiableSimpleClassNames() throws IOException {
        // Given
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // When / Then
        try {
            simpleClassNames.put("test", new HashSet<>());
            fail("Exception expected");
        } catch (final UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }


    @Test
    public void shouldCacheSubclasses() throws IOException {
        // Given
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // When
        final Set<Class> subclasses2 = ReflectionUtil.getSubTypes(Number.class);

        // Then
        assertSame(subclasses, subclasses2);
    }

    @Test
    public void shouldCacheSimpleNames() throws IOException {
        // Given
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // When
        final Map<String, Set<Class>> simpleClassNames2 = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        assertSame(simpleClassNames, simpleClassNames2);
    }

    @Test
    public void shouldReturnSubclasses() throws IOException {
        // When
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // Then
        assertEquals(
                Sets.newHashSet(
                        TestCustomNumber.class,
                        uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
                ),
                subclasses
        );
        assertFalse(subclasses.contains(UnsignedLong.class));
    }

    @Test
    public void shouldReturnSimpleClassNames() throws IOException {
        // When
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        assertEquals(
                Sets.newHashSet(
                        TestCustomNumber.class,
                        uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
                ),
                simpleClassNames.get(TestCustomNumber.class.getSimpleName())
        );
        assertFalse(simpleClassNames.containsKey(UnsignedLong.class.getSimpleName()));
    }

    @Test
    public void shouldReturnSubclassesWithExtraClassesInPath() throws IOException {
        // When
        ReflectionUtil.addReflectionPackages(UnsignedLong.class.getPackage().getName());
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // Then
        assertThat(subclasses,
                IsCollectionContaining.hasItems(
                        TestCustomNumber.class,
                        uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class,
                        UnsignedLong.class
                )
        );
    }

    @Test
    public void shouldReturnSimpleClassNamesWithExtraClassesInPath() throws IOException {
        // When
        ReflectionUtil.addReflectionPackages(UnsignedLong.class.getPackage().getName());
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        assertEquals(
                Sets.newHashSet(
                        TestCustomNumber.class,
                        uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
                ),
                simpleClassNames.get(TestCustomNumber.class.getSimpleName())
        );
        assertEquals(Collections.singleton(UnsignedLong.class), simpleClassNames.get(UnsignedLong.class.getSimpleName()));

        final Set<String> expected = Sets.newHashSet(ReflectionUtil.DEFAULT_PACKAGES);
        expected.add(UnsignedLong.class.getPackage().getName());
        assertEquals(expected, ReflectionUtil.getReflectionPackages());
    }

    @Test
    public void shouldReturnSimpleClassNamesWithExtraClassesInPathWithTrailingDot() throws IOException {
        // When
        ReflectionUtil.addReflectionPackages(UnsignedLong.class.getPackage().getName() + ".");
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        assertEquals(
                Sets.newHashSet(
                        TestCustomNumber.class,
                        uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
                ),
                simpleClassNames.get(TestCustomNumber.class.getSimpleName())
        );
        assertEquals(Collections.singleton(UnsignedLong.class), simpleClassNames.get(UnsignedLong.class.getSimpleName()));
        final Set<String> expected = Sets.newHashSet(ReflectionUtil.DEFAULT_PACKAGES);
        expected.add(UnsignedLong.class.getPackage().getName());
        assertEquals(expected, ReflectionUtil.getReflectionPackages());
    }
}
