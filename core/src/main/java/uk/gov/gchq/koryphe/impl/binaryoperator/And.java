/*
 * Copyright 2017-2020 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.binaryoperator;

import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.binaryoperator.KorypheBinaryOperator;

/**
 * An <code>And</code> is a {@link KorypheBinaryOperator}
 * which takes two {@link Boolean}s and returns the result of applying
 * the logical AND operation on the inputs.
 */
@Since("1.0.0")
@Summary("Applies the logical AND operation to 2 booleans")
public class And extends KorypheBinaryOperator<Boolean> {

    @Override
    protected Boolean _apply(final Boolean a, final Boolean b) {
        return a && b;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
