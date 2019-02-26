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
import org.apache.commons.io.IOUtils;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import static java.util.Objects.isNull;

/**
 * A <code>Gunzip</code> is a {@link java.util.function.Function} that takes
 * a byte[] of gzipped data and decompresses it.
 */
@Since("1.7.0")
@Summary("Decompresses gzipped data")
public class Gunzip extends KorypheFunction<byte[], byte[]> {
    @SuppressFBWarnings(value = "PZLA_PREFER_ZERO_LENGTH_ARRAYS", justification = "Returning null means the input was null")
    @Override
    public byte[] apply(final byte[] compressed) {
        if (isNull(compressed)) {
            return null;
        }

        if (compressed.length == 0) {
            return new byte[0];
        }

        try (final GZIPInputStream gzipStream = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
            return IOUtils.toByteArray(gzipStream);
        } catch (final IOException e) {
            throw new RuntimeException("Failed to decompress provided gzipped string", e);
        }
    }
}
