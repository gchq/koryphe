/*
 * Copyright 2019-2020 Crown Copyright
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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.json.XML;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.io.Serializable;
import java.util.Map;

import static java.util.Objects.isNull;

@Since("1.8.0")
@Summary("Parses an XML document into multiple Maps")
@JsonPropertyOrder(alphabetic = true)
public class DeserialiseXml extends KorypheFunction<String, Map<String, Object>> implements Serializable {
    private static final long serialVersionUID = -6302491770456683336L;

    @Override
    public Map<String, Object> apply(final String xml) {
        if (isNull(xml)) {
            return null;
        }

        return XML.toJSONObject(xml).toMap();
    }
}
