/*
 * Copyright 2018-2020 Crown Copyright
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

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@code DelegatingAnnotationIntrospector} is a lightweight extension
 * to {@link JacksonAnnotationIntrospector} to trick Jackson into thinking all
 * classes are annotated with {@code @JsonTypeIdResolver(value = SimpleClassNameIdResolver.class) }
 * If you add a custom JsonTypeIdResolver annotation it will override the magic
 * annotation from this class.
 */
public class DelegatingAnnotationIntrospector extends JacksonAnnotationIntrospector {
    private static final long serialVersionUID = -6308527388339985525L;
    private final Map<Class, Object> delegateAnnotations;

    DelegatingAnnotationIntrospector(final Map<Class, Object> delegateAnnotations) {
        this.delegateAnnotations = delegateAnnotations;
    }

    @SuppressFBWarnings(value = "SIC_INNER_SHOULD_BE_STATIC_ANON", justification = "This is required")
    @SuppressWarnings("unchecked")
    @Override
    protected <A extends Annotation> A _findAnnotation(
            final Annotated annotated, final Class<A> annoClass) {
        A annotation = super._findAnnotation(annotated, annoClass);
        if (null == annotation) {
            annotation = (A) delegateAnnotations.get(annoClass);
        }
        return annotation;
    }

    public static class Builder {
        private final Map<Class, Object> delegateAnnotations = new HashMap<>();

        public Builder add(final Annotation annotation) {
            delegateAnnotations.put(annotation.annotationType(), annotation);
            return this;
        }

        public DelegatingAnnotationIntrospector build() {
            return new DelegatingAnnotationIntrospector(delegateAnnotations);
        }
    }
}
