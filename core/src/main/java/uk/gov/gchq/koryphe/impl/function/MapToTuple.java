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

import com.fasterxml.jackson.annotation.JsonInclude;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.io.Serializable;
import java.util.Map;

@Since("1.8.0")
@Summary("Converts a Map to a Tuple")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class MapToTuple<K> extends KorypheFunction<Map<K, Object>, Tuple<K>> implements Serializable {
    private static final long serialVersionUID = -2964196592651621579L;

    @Override
    public Tuple<K> apply(final Map<K, Object> map) {
        return new MapTuple<>(map);
    }
}
