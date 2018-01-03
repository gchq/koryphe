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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;
import uk.gov.gchq.koryphe.serialisation.json.first.TestCustomObjImpl;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import uk.gov.gchq.koryphe.util.ReflectionUtil;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SimpleClassNameIdResolverTest {

    @Before
    @After
    public void before() {
        SimpleClassNameIdResolver.setUseFullNameForSerialisation(true);
        SimpleClassNameIdResolver.reset();
        JsonSerialiser.resetMapper();
        ReflectionUtil.resetReflectionPackages();
        ReflectionUtil.resetReflectionCache();
    }

    @Test
    public void shouldDeserialiseFromSimpleClassName() throws IOException {
        // Given
        final String json = "{\"class\":\"IsA\",\"type\":\"Integer\"}";

        // When
        final IsA predicate = (IsA) JsonSerialiser.deserialise(json, KoryphePredicate.class);

        // Then
        assertEquals(Integer.class.getName(), predicate.getType());
    }

    @Test
    public void shouldDeserialiseFromFullClassName() throws IOException {
        // Given
        final String json = "{\"class\":\"uk.gov.gchq.koryphe.impl.predicate.IsA\",\"type\":\"java.lang.Integer\"}";

        // When
        final IsA predicate = (IsA) JsonSerialiser.deserialise(json, KoryphePredicate.class);

        // Then
        assertEquals(Integer.class.getName(), predicate.getType());
    }

    @Test
    public void shouldSerialiseUsingFullClassName() throws IOException {
        // Given
        final IsA predicate = new IsA(Integer.class);

        // When
        final String json = JsonSerialiser.serialise(predicate);

        // Then
        final String expectedJson = "{\"class\":\"uk.gov.gchq.koryphe.impl.predicate.IsA\",\"type\":\"java.lang.Integer\"}";
        JsonSerialiser.assertEquals(expectedJson, json);
    }

    @Test
    public void shouldSerialiseUsingSimpleClassName() throws IOException {
        // Given
        SimpleClassNameIdResolver.setUseFullNameForSerialisation(false);
        final IsA predicate = new IsA(Integer.class);

        // When
        final String json = JsonSerialiser.serialise(predicate);

        // Then
        final String expectedJson = "{\"class\":\"IsA\",\"type\":\"java.lang.Integer\"}";
        JsonSerialiser.assertEquals(expectedJson, json);
    }

    @Test
    public void shouldThrowExceptionIfMultipleClassesWithTheSameName() throws IOException {
        // Given
        SimpleClassNameIdResolver.addSimpleClassNames(true, TestCustomObj.class);

        // When / Then
        try {
            JsonSerialiser.deserialise("{\"class\":\"TestCustomObjImpl\"}", TestCustomObj.class);
            fail("Exception expected");
        } catch (final Exception e) {
            assertTrue(e.getMessage(),
                    e.getMessage().contains("Multiple TestCustomObjImpl classes exist")
                            && e.getMessage().contains(TestCustomObjImpl.class.getName())
                            && e.getMessage().contains(uk.gov.gchq.koryphe.serialisation.json.second.TestCustomObjImpl.class.getName()));
        }
    }

    @Test
    public void shouldDeserialiseClassWithUniqueSimpleName() throws IOException {
        // Given
        SimpleClassNameIdResolver.addSimpleClassNames(true, TestCustomObj.class);

        // When
        final TestCustomObj obj = JsonSerialiser.deserialise("{\"class\":\"TestCustomObjImplUnique\"}", TestCustomObj.class);

        // Then
        assertNotNull(obj);
    }
}
