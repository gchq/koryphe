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

import com.google.common.primitives.UnsignedLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.serialisation.json.obj.first.TestCustomNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> subclasses.add(String.class));
    }

    @Test
    public void shouldReturnUnmodifiableSimpleClassNames() {
        // Given
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // When / Then
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> simpleClassNames.put("test", new HashSet<>()));
    }

    @Test
    public void shouldCacheSubclasses() {
        // Given
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // When
        final Set<Class> subclasses2 = ReflectionUtil.getSubTypes(Number.class);

        // Then
        assertThat(subclasses2).isSameAs(subclasses);
    }

    @Test
    public void shouldCacheSimpleNames() {
        // Given
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // When
        final Map<String, Set<Class>> simpleClassNames2 = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        assertThat(simpleClassNames2).isSameAs(simpleClassNames);
    }

    @Test
    public void shouldReturnSubclasses() {
        // When
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // Then
        assertThat(subclasses)
                .containsExactlyInAnyOrder(TestCustomNumber.class, uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class)
                .doesNotContain(UnsignedLong.class);
    }

    @Test
    public void shouldReturnSimpleClassNames() {
        // When
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        assertThat(simpleClassNames)
                .containsEntry(
                    TestCustomNumber.class.getSimpleName(),
                    new HashSet<>(Arrays.asList(
                        TestCustomNumber.class,
                        uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
                    )
                ))
                .doesNotContainKey(UnsignedLong.class.getSimpleName());
    }

    @Test
    public void shouldReturnSubclassesWithExtraClassesInPath() {
        // When
        ReflectionUtil.addReflectionPackages(UnsignedLong.class.getPackage().getName());
        final Set<Class> subclasses = ReflectionUtil.getSubTypes(Number.class);

        // Then
        assertThat(subclasses).contains(
            TestCustomNumber.class,
            uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class,
            UnsignedLong.class
        );
    }

    @Test
    public void shouldReturnSimpleClassNamesWithExtraClassesInPath() {
        // When
        ReflectionUtil.addReflectionPackages(UnsignedLong.class.getPackage().getName());
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        assertThat(simpleClassNames)
                .containsEntry(
                    TestCustomNumber.class.getSimpleName(),
                    new HashSet<>(Arrays.asList(
                        TestCustomNumber.class,
                        uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
                    )
                ))
                .containsEntry(
                    UnsignedLong.class.getSimpleName(),
                    Collections.singleton(UnsignedLong.class)
                );

        assertThat(ReflectionUtil.getReflectionPackages())
                .containsAnyElementsOf(ReflectionUtil.DEFAULT_PACKAGES)
                .contains(UnsignedLong.class.getPackage().getName());
    }

    @Test
    public void shouldReturnSimpleClassNamesWithExtraClassesInPathWithTrailingDot() {
        // When
        ReflectionUtil.addReflectionPackages(UnsignedLong.class.getPackage().getName() + ".");
        final Map<String, Set<Class>> simpleClassNames = ReflectionUtil.getSimpleClassNames(Number.class);

        // Then
        assertThat(simpleClassNames)
                .containsEntry(
                    TestCustomNumber.class.getSimpleName(),
                    new HashSet<>(Arrays.asList(
                        TestCustomNumber.class,
                        uk.gov.gchq.koryphe.serialisation.json.obj.second.TestCustomNumber.class
                    )
                ))
                .containsEntry(
                    UnsignedLong.class.getSimpleName(),
                    Collections.singleton(UnsignedLong.class)
                );

        assertThat(ReflectionUtil.getReflectionPackages())
                .containsAnyElementsOf(ReflectionUtil.DEFAULT_PACKAGES)
                .contains(UnsignedLong.class.getPackage().getName());
    }
}
