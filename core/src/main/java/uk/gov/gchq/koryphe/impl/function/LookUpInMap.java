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

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.util.Map;

/**
 * A {@link LookUpInMap} is a {@link KorypheFunction} that takes a key and using a parameterised map returns a value.
 */
@Since("1.6.1")
@Summary("Looks up a value in a parameterised map")
public class LookUpInMap<K, V> extends KorypheFunction<K, V> {
    Map<K, V> lookUpMap;

    public LookUpInMap() {
        // Required for serialisation
    }

    public LookUpInMap(Map<K, V> lookUpMap) {
        this.lookUpMap = lookUpMap;
    }

    @Override
    public V apply(K key) {
        return lookUpMap.get(key);
    }

    public Map<K, V> getLookUpMap() {
        return lookUpMap;
    }

    public void setLookUpMap(Map<K, V> lookUpMap) {
        this.lookUpMap = lookUpMap;
    }
}
