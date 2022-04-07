/*
 * Copyright 2022 Crown Copyright
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

import com.google.common.collect.Sets;

import org.assertj.core.api.AbstractAssert;

public class InputValidatorAssert extends AbstractAssert<InputValidatorAssert, InputValidator> {

    public InputValidatorAssert(InputValidator actual) {
        super(actual, InputValidatorAssert.class);
    }

    public static InputValidatorAssert assertThat(InputValidator actual) {
        return new InputValidatorAssert(actual);
    }

    public InputValidatorAssert acceptsInput(Class<?>... arguments) {
        isNotNull();
        if (!actual.isInputValid(arguments).isValid()) {
            failWithMessage("Expected Class %s to accept inputs %s", actual, Sets.newHashSet(arguments));
        }
        return this;
    }

    public InputValidatorAssert rejectsInput(Class<?>... arguments) {
        isNotNull();
        if (actual.isInputValid(arguments).isValid()) {
            failWithMessage("Expected Class %s to reject inputs %s", actual, Sets.newHashSet(arguments));
        }
        return this;
    }
}
