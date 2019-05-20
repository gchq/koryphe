/*
 * Copyright 2019 Crown Copyright
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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.codec.binary.Base64;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import static java.util.Objects.isNull;

/**
 * A <code>Base64Decode</code> is a {@link java.util.function.Function} that takes
 * a Base 64 encoded byte[] and decodes it into a byte[].
 */
@Since("1.8.0")
@Summary("Decodes a base64 encoded byte array")
public class Base64Decode extends KorypheFunction<byte[], byte[]> {
    @SuppressFBWarnings(value = "PZLA_PREFER_ZERO_LENGTH_ARRAYS", justification = "Returning null means the input was null")
    @Override
    public byte[] apply(final byte[] base64Encoded) {
        if (isNull(base64Encoded)) {
            return null;
        }

        if (base64Encoded.length == 0) {
            return new byte[0];
        }

        return Base64.decodeBase64(base64Encoded);
    }
}
