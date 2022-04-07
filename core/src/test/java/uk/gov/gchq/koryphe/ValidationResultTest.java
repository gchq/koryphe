/*
 * Copyright 2020-2022 Crown Copyright
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

package uk.gov.gchq.koryphe;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.util.EqualityTest;

import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationResultTest extends EqualityTest<ValidationResult> {
    private static final String EXAMPLE_ERROR = "err";
    private static final String DIFFERENT_ERROR = "different error";

    @Override
    protected ValidationResult getInstance() {
        return new ValidationResult();
    }

    @Override
    protected Iterable<ValidationResult> getDifferentInstancesOrNull() {
        ValidationResult twoErrors = new ValidationResult("Something went wrong");

        twoErrors.addError("another thing went wrong");

        return Arrays.asList(
                new ValidationResult("Something broke"),
                twoErrors
        );
    }

    @Test
    public void shouldInitiallyBeValid() {
        // Given
        ValidationResult validationResult = new ValidationResult();

        // When
        boolean valid = validationResult.isValid();

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void shouldReturnEmptySetIfValid() {
        // Given
        ValidationResult validationResult = new ValidationResult();

        // When
        Set<String> errors = validationResult.getErrors();

        // Then
        assertThat(errors).isEmpty();
    }

    @Test
    public void shouldReturnStringForErrorStringIfNoErrorsArePresent() {
        // Given
        ValidationResult validationResult = new ValidationResult();

        // When
        String errorString = validationResult.getErrorString();

        // Then
        assertThat(errorString).isEqualTo("Validation errors: %s", System.lineSeparator());
    }

    @Test
    public void shouldDeduplicateErrors() {
        // Given
        ValidationResult validationResult = new ValidationResult();
        validationResult.addError(EXAMPLE_ERROR);
        validationResult.addError(EXAMPLE_ERROR);

        // When
        Set<String> errors = validationResult.getErrors();

        // Then
        assertThat(errors).hasSize(1);
    }

    @Test
    public void shouldListAllUniqueErrors() {
        // Given
        ValidationResult validationResult = new ValidationResult();
        validationResult.addError(EXAMPLE_ERROR);
        validationResult.addError(DIFFERENT_ERROR);

        // When
        Set<String> errors = validationResult.getErrors();

        // Then
        assertThat(errors)
                .hasSize(2)
                .containsExactly(EXAMPLE_ERROR, DIFFERENT_ERROR);
    }

    @Test
    public void shouldSeparateAllTheErrorsUsingTheSystemLineSeparator() {
        // Given
        String lineSeparator = System.lineSeparator();
        ValidationResult validationResult = new ValidationResult();
        validationResult.addError(EXAMPLE_ERROR);
        validationResult.addError(DIFFERENT_ERROR);

        // When
        String errorString = validationResult.getErrorString();

        // Then
        assertThat(errorString)
                .isEqualTo("Validation errors: %s%s%s%s", lineSeparator, EXAMPLE_ERROR, lineSeparator, DIFFERENT_ERROR);
    }
}
