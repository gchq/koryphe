/*
 * Copyright 2018-2019 Crown Copyright
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

package uk.gov.gchq.koryphe.serialisation.json;

import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.annotation.Annotation;

/**
 * A {@link SimpleClassNameIdResolverAnnotation} is lightweight implementation of
 * {@link JsonTypeIdResolver} that sets the value to SimpleClassNameIdResolver.class.
 * Should be used with the {@link DelegatingAnnotationIntrospector}.
 */
@SuppressWarnings("ClassExplicitlyAnnotation")
public class SimpleClassNameIdResolverAnnotation implements JsonTypeIdResolver {
    @Override
    public Class<? extends Annotation> annotationType() {
        return JsonTypeIdResolver.class;
    }

    @Override
    public Class<? extends TypeIdResolver> value() {
        return SimpleClassNameIdResolver.class;
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || (null != obj && getClass().equals(obj.getClass()));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 17)
                .build();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }
}
