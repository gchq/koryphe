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
import org.hamcrest.Matcher;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.serialisation.json.obj.first.TestCustomNumber;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReflectionUtilTest {

    @BeforeEach
    @AfterEach
    public void cleanUp() {
        ReflectionUtil.resetReflectionPackages();
        ReflectionUtil.resetReflectionCache();
    }

    @Test
    public void shouldReturnUnmodifiableClasses() {
        // Given
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // When / Then
        assertThrows(UnsupportedOperationException.class, () -> subclasses.add(String.class));
    }

    @Test
    public void shouldReturnUnmodifiableSimpleClassNames() {
        // Given
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // When / Then
        assertThrows(UnsupportedOperationException.class, () -> simpleClassNames.put("test", new HashSet<>()));
    }

    @Test
    public void shouldCacheSubclasses() {
        // Given
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // When
        final Set<Class> subclasses2 = ReflectionUtil.getSubTypes(Number.class);

        // Then
        assertSame(subclasses, subclasses2);
    }

    @Test
    public void shouldCacheSimpleNames() {
        // Given
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // When
        final Map<String, Set<Class>> simpleClassNames2 = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        assertSame(simpleClassNames, simpleClassNames2);
    }

    @Test
    public void shouldReturnSubclasses() {
        // When
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // Then
        final HashSet<Class<? extends Number>> expected = Sets.newHashSet(
                TestCustomNumber.class, uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
        );
        assertEquals(expected, subclasses);
        assertFalse(subclasses.contains(UnsignedLong.class));
    }

    @Test
    public void shouldReturnSimpleClassNames() {
        // When
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        final HashSet<Class<? extends Number>> expected = Sets.newHashSet(
                TestCustomNumber.class,
                uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
        );
        assertEquals(expected, simpleClassNames.get(TestCustomNumber.class.getSimpleName()));
        assertFalse(simpleClassNames.containsKey(UnsignedLong.class.getSimpleName()));
    }

    @Test
    public void shouldReturnSubclassesWithExtraClassesInPath() {
        // When
        ReflectionUtil.addReflectionPackages(UnsignedLong.class.getPackage().getName());
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // Then
        final Matcher<Iterable<Class>> matcher = IsCollectionContaining.hasItems(
                TestCustomNumber.class,
                uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class,
                UnsignedLong.class
        );
        assertThat(subclasses, matcher);
    }

    @Test
    public void shouldReturnSimpleClassNamesWithExtraClassesInPath() {
        // When
        ReflectionUtil.addReflectionPackages(UnsignedLong.class.getPackage().getName());
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        final HashSet<Class<? extends Number>> expected1 = Sets.newHashSet(
                TestCustomNumber.class,
                uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
        );
        assertEquals(expected1, simpleClassNames.get(TestCustomNumber.class.getSimpleName()));
        assertEquals(Collections.singleton(UnsignedLong.class), simpleClassNames.get(UnsignedLong.class.getSimpleName()));

        final Set<String> expected2 = Sets.newHashSet(ReflectionUtil.DEFAULT_PACKAGES);
        expected2.add(UnsignedLong.class.getPackage().getName());
        assertEquals(expected2, ReflectionUtil.getReflectionPackages());
    }

    @Test
    public void shouldReturnSimpleClassNamesWithExtraClassesInPathWithTrailingDot() {
        // When
        ReflectionUtil.addReflectionPackages(UnsignedLong.class.getPackage().getName() + ".");

        // Then
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        final HashSet<Class<? extends Number>> expected1 = Sets.newHashSet(
                TestCustomNumber.class,
                uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
        );
        assertEquals(expected1, simpleClassNames.get(TestCustomNumber.class.getSimpleName()));
        assertEquals(Collections.singleton(UnsignedLong.class), simpleClassNames.get(UnsignedLong.class.getSimpleName()));

        final Set<String> expected = Sets.newHashSet(ReflectionUtil.DEFAULT_PACKAGES);
        expected.add(UnsignedLong.class.getPackage().getName());
        assertEquals(expected, ReflectionUtil.getReflectionPackages());
    }
}
