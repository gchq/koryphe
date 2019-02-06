/*
 * Copyright 2017-2019 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.predicate;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.signature.InputValidator;
import uk.gov.gchq.koryphe.tuple.predicate.KoryphePredicate2;

/**
 * An <code>IsXMoreThanY</code> is a {@link java.util.function.BiPredicate} that checks that the first input
 * {@link Comparable} is more than the second input {@link Comparable}.
 */
@Since("1.0.0")
@Summary("Checks the first comparable is more than the second comparable")
public class IsXMoreThanY extends KoryphePredicate2<Comparable, Comparable> implements InputValidator {
    @Override
    public boolean test(final Comparable input1, final Comparable input2) {
        return null != input1 && null != input2
                && input1.getClass() == input2.getClass()
                && (input1.compareTo(input2) > 0);
    }

    @Override
    public ValidationResult isInputValid(final Class<?>... arguments) {
        final ValidationResult result = new ValidationResult();
        if (null == arguments || 2 != arguments.length || null == arguments[0] || null == arguments[1]) {
            result.addError("Incorrect number of arguments for " + getClass().getName() + ". 2 arguments are required.");
            return result;
        }

        if (!arguments[0].equals(arguments[1]) || !Comparable.class.isAssignableFrom(arguments[0])) {
            result.addError("Inputs must be the same class type and comparable: " + arguments[0] + "," + arguments[1]);
        }

        return result;
    }
}
