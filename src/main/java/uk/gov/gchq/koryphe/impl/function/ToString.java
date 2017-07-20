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
package uk.gov.gchq.koryphe.impl.function;

import uk.gov.gchq.koryphe.function.KorypheFunction;

/**
 * A <code>ToString</code> is a {@link java.util.function.Function} that takes in
 * an object (null or otherwise), and calls toString on it.
 */
public class ToString extends KorypheFunction {

    @Override
    public String apply(final Object o) {
        return null == o ? "null" : String.valueOf(o);
    }
}
