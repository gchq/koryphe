/*
 * Copyright 2017-2018 Crown Copyright
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

package uk.gov.gchq.koryphe.tuple.predicate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.signature.InputValidator;
import uk.gov.gchq.koryphe.signature.Signature;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Predicate;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
@JsonPropertyOrder(value = {"class", "selection", "predicate"}, alphabetic = true)
@Since("1.0.0")
@Summary("Applies a predicate and adapts an input integer tuple")
public class IntegerTupleAdaptedPredicate extends TupleAdaptedPredicate<Integer, Object> implements InputValidator {
    public IntegerTupleAdaptedPredicate() {
    }

    public IntegerTupleAdaptedPredicate(final Predicate predicate, final Integer... selection) {
        super(predicate, selection);
    }

    @Override
    public ValidationResult isInputValid(final Class<?>... arguments) {
        if (null == getPredicate()) {
            return new ValidationResult();
        }

        final Integer[] selection = getSelection();
        if (null == selection) {
            return (Signature.getInputSignature(getPredicate()).assignable());
        }

        final Class[] selectedArgs = new Class[selection.length];
        int i = 0;
        for (final Integer selectionIndex : selection) {
            if (selectionIndex >= arguments.length) {
                final ValidationResult result = new ValidationResult();
                result.addError("Incorrect selection of arguments for nested predicate " + getPredicate().getClass().getName() + ". Selection requires " + (Collections.max(Arrays.asList(selection)) + 1) + " arguments, however only " + arguments.length + " provided.");
                return result;
            }
            selectedArgs[i] = arguments[selectionIndex];
            i++;
        }

        return (Signature.getInputSignature(getPredicate()).assignable(selectedArgs));
    }
}
