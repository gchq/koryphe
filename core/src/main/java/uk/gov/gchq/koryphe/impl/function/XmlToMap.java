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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Lists;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Since("1.6.0")
@Summary("Parses an XML document into multiple Maps")
@JsonPropertyOrder(alphabetic = true)
public class XmlToMap extends KorypheFunction<String, Map<String, Object>> implements Serializable {
    private static final long serialVersionUID = -6302491770456683336L;
    private static final XmlMapper MAPPER = new XmlMapper();
    private static final TypeReference<LinkedHashMap<String, Object>> TYPE_REFERENCE = new TypeReference<LinkedHashMap<String, Object>>() {
    };
    @Override
    public Map<String, Object> apply(final String xml) {
        if (isNull(xml)) {
            return null;
        }

        try {
            return MAPPER.readValue(xml, TYPE_REFERENCE);
        } catch (final IOException e) {
            throw new RuntimeException("Failed to parse xml string into maps", e);
        }
    }
}
