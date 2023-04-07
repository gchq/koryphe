/*
 * Copyright 2022-2023 Crown Copyright
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

import org.assertj.core.api.AbstractObjectAssert;

import java.util.Arrays;
import java.util.HashSet;

public class SignatureAssert extends AbstractObjectAssert<SignatureAssert, Signature> {

    public SignatureAssert(Signature actual) {
        super(actual, SignatureAssert.class);
    }

    public static SignatureAssert assertThat(Signature actual) {
        return new SignatureAssert(actual);
    }

    public SignatureAssert isAssignableFrom(Class<?>... arguments) {
        isNotNull();
        if (!actual.assignable(arguments).isValid()) {
            failWithMessage("Expected Signature %s to be assignable from %s", actual, new HashSet<>(Arrays.asList(arguments)));
        }
        return this;
    }

    public SignatureAssert isNotAssignableFrom(Class<?>... arguments) {
        isNotNull();
        if (actual.assignable(arguments).isValid()) {
            failWithMessage("Expected Signature %s not to be assignable from %s", actual, new HashSet<>(Arrays.asList(arguments)));
        }
        return this;
    }
}
