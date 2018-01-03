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
package uk.gov.gchq.koryphe.serialisation.json;

import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.lang.annotation.Annotation;

/**
 * A {@code SimpleClassNamedIdAnnotationIntrospector} is a lightweight extension
 * to {@link JacksonAnnotationIntrospector} to trick Jackson into thinking all
 * classes are annotated with {@code @JsonTypeIdResolver(value = SimpleClassNameIdResolver.class) }
 * If you add a custom JsonTypeIdResolver annotation it will override the magic
 * annotation from this class.
 */
public class SimpleClassNamedIdAnnotationIntrospector extends JacksonAnnotationIntrospector {
    private static final long serialVersionUID = -8810132418628913715L;

    @SuppressFBWarnings(value = "SIC_INNER_SHOULD_BE_STATIC_ANON", justification = "This is required")
    @SuppressWarnings("unchecked")
    @Override
    protected <A extends Annotation> A _findAnnotation(
            final Annotated annotated, final Class<A> annoClass) {
        A annotation = super._findAnnotation(annotated, annoClass);
        if (null == annotation && JsonTypeIdResolver.class.equals(annoClass)) {
            annotation = (A) new SimpleClassNameIdResolverAnnotation();
        }
        return annotation;
    }

    @SuppressWarnings("ClassExplicitlyAnnotation")
    private static class SimpleClassNameIdResolverAnnotation implements JsonTypeIdResolver {
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
}
