package uk.gov.gchq.koryphe;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.util.EqualityTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationResultTest extends EqualityTest<ValidationResult> {
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
        assertTrue(valid);
    }

    @Test
    public void shouldReturnEmptySetIfValid() {
        // Given
        ValidationResult validationResult = new ValidationResult();

        // When
        Set<String> errors = validationResult.getErrors();

        // Then
        assertEquals(new HashSet<>(), errors);
    }

    @Test
    public void shouldReturnStringForErrorStringIfNoErrorsArePresent() {
        // Given
        ValidationResult validationResult = new ValidationResult();

        // When
        String errorString = validationResult.getErrorString();

        // Then
        assertEquals("Validation errors: " + System.lineSeparator(), errorString);
    }

    @Test
    public void shouldDeduplicateErrors() {
        // Given
        ValidationResult validationResult = new ValidationResult();
        validationResult.addError("err");
        validationResult.addError("err");

        // When
        Set<String> errors = validationResult.getErrors();

        assertEquals(1, errors.size());
    }

    @Test
    public void shouldListAllUniqueErrors() {
        // Given
        ValidationResult validationResult = new ValidationResult();
        validationResult.addError("err");
        validationResult.addError("different error");

        // When
        Set<String> errors = validationResult.getErrors();

        // Then
        assertEquals(2, errors.size());
        HashSet<String> expected = Sets.newHashSet("err", "different error");

        assertEquals(expected, errors);
    }

    @Test
    public void shouldSeparateAllTheErrorsUsingTheSystemLineSeparator() {
        // Given
        String lineSeparator = System.lineSeparator();
        ValidationResult validationResult = new ValidationResult();
        validationResult.addError("err");
        validationResult.addError("different error");

        // When
        String errorString = validationResult.getErrorString();

        // Then
        assertEquals("Validation errors: " + lineSeparator + "err" + lineSeparator + "different error", errorString);
    }
}
