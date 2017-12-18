/*
 * Copyright 2017 Crown Copyright
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
package uk.gov.gchq.koryphe.signature;

import uk.gov.gchq.koryphe.ValidationResult;

/**
 * A {@code TypeResolver} should attempt to dynamically resolve the
 * type of a JSON field.
 * This removes the requirement for users to explicitly specify types
 * when providing {@link java.util.function.Predicate} or
 * {@link java.util.function.Function} control value(s).
 */
public interface TypeResolver {

    /**
     * This method should attempt to serialise and deserialise any control
     * values of the predicate/function into the
     * concrete type(s) contained within the arguments.
     *
     * @param arguments the concrete classes to which to attempt to resolve
     * @return {@link ValidationResult} containing any errors
     */
    ValidationResult resolveTypes(final Class<?>... arguments);
}
