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

package uk.gov.gchq.koryphe.signature;

import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.tuple.n.Tuple1;

import java.util.Arrays;

/**
 * An <code>TupleSignature</code> is the type metadata for a tuple of values.
 */
public class TupleSignature extends Signature {
    private final Object input;
    private final Integer numClasses;
    private final Class[] classes;
    private final SingletonSignature[] types;
    private final boolean isInput;

    TupleSignature(final Object input, final Class tupleClazz, final Class[] classes, final boolean isInput) {
        this.input = input;
        this.classes = classes;
        this.isInput = isInput;
        if (1 == classes.length && !(Tuple1.class.isAssignableFrom(tupleClazz))) {
            // This tuple will accept any number of arguments
            numClasses = null;
        } else {
            numClasses = classes.length;
        }

        types = new SingletonSignature[classes.length];
        int i = 0;
        for (final Class clazz : classes) {
            types[i++] = new SingletonSignature(input, clazz, isInput);
        }
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
        if (null != types) {
            if (null != numClasses && types.length != arguments.length) {
                result.addError("Incompatible number of types. " + input.getClass() + ": " + Arrays.toString(types)
                        + ", arguments: " + Arrays.toString(arguments));
                return result;
            }

            if (null == numClasses) {
                for (final Class type : arguments) {
                    result.add(types[0].assignable(type));
                }
            } else {
                int i = 0;
                for (final Class type : arguments) {
                    result.add(types[i].assignable(type));
                    i++;
                }
            }
        }
        return result;
    }

    @Override
    public Integer getNumClasses() {
        return numClasses;
    }

    @Override
    public Class[] getClasses() {
        return Arrays.copyOf(classes, classes.length);
    }
}
