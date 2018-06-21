/*
 * Copyright 2018 Crown Copyright
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

package uk.gov.gchq.koryphe.tuple;

import java.lang.reflect.AccessibleObject;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class Cache {
    private final Map<Class, Map<String, AccessibleObject>> cache = new HashMap<>();

    public AccessibleObject get(final Class<? extends Object> itemClass, final String reference) {
        requireNonNull(itemClass);
        requireNonNull(reference);

        AccessibleObject rtn = null;
        final Map<String, AccessibleObject> map = cache.get(itemClass);
        if (nonNull(map)) {
            rtn = map.get(reference);
        }
        return rtn;
    }

    public AccessibleObject put(final Class<? extends Object> itemClass, final String reference, final AccessibleObject accessibleObject) {
        Map<String, AccessibleObject> accessibleObjectMap = cache.get(itemClass);
        if (isNull(accessibleObjectMap)) {
            accessibleObjectMap = new HashMap<>();
            cache.put(itemClass, accessibleObjectMap);
        }
        return accessibleObjectMap.put(reference, accessibleObject);
    }
}
