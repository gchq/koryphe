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
import java.util.Arrays;

/**
 * A <code>SingletonSignature</code> is the type metadata for a single instance of a specific type.
 */
public class SingletonSignature extends Signature {
    private final Object input;
    private final boolean isInput;
    private Class<?> type;

    SingletonSignature(final Object input, final Class type, final boolean isInput) {
        this.input = input;
        this.type = type;
        this.isInput = isInput;
    }

    @Override
    public ValidationResult assignable(final Class<?>... arguments) {
        if (isInput) {
            if (input instanceof InputValidator) {
                return ((InputValidator) input).isInputValid(arguments);
            }
        } else if (input instanceof OutputValidator) {
            return ((OutputValidator) input).isOutputValid(arguments);
        }

        final ValidationResult result = new ValidationResult();
        if (type == null) {
            result.addError("Type could not be extracted from function " + input.getClass().getName());
            return result;
        }

        if (UnknownGenericType.class.equals(type)) {
            // unknown type so anything is assignable
            return result;
        }

        if (arguments.length != 1 || null == arguments[0]) {
            result.addError("Incompatible number of types. " + input.getClass().getName() + ": [" + type
                    + "], arguments: " + Arrays.toString(arguments));
            return result;
        }

        final boolean isAssignable = type.isAssignableFrom(arguments[0]);
        if (!isAssignable) {
            result.addError("Incompatible types. " + input.getClass().getName() + ": [" + type
                    + "], arguments: " + Arrays.toString(arguments));
        }
        return result;
    }

    @Override
    public Class[] getClasses() {
        return new Class[]{type};
    }

    @Override
    public Integer getNumClasses() {
        return UnknownGenericType.class.equals(type) ? null : 1;
    }
}
