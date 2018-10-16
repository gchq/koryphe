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

package uk.gov.gchq.koryphe.impl.predicate;

import uk.gov.gchq.koryphe.impl.predicate.range.InDateRange;
import uk.gov.gchq.koryphe.impl.predicate.range.InDateRangeDual;
import uk.gov.gchq.koryphe.impl.predicate.range.InRange;
import uk.gov.gchq.koryphe.impl.predicate.range.InRangeDual;
import uk.gov.gchq.koryphe.impl.predicate.range.InTimeRange;
import uk.gov.gchq.koryphe.impl.predicate.range.InTimeRangeDual;
import uk.gov.gchq.koryphe.predicate.PredicateMap;
import uk.gov.gchq.koryphe.util.TimeUnit;

public class PredicateLibrary {

    public static InDateRange inDateRange(final String start, final String end) {
        return new InDateRange.Builder().start(start).end(end).build();
    }

    public static InDateRangeDual inDateRangeDual(final String start, final String end) {
        return new InDateRangeDual.Builder().start(start).end(end).build();
    }

    public static InTimeRange inTimeRange(final String start, final String end) {
        return new InTimeRange.Builder().start(start).end(end).build();
    }

    public static InTimeRange inTimeRange(final String start, final String end, final TimeUnit timeUnit) {
        return new InTimeRange.Builder().start(start).end(end).timeUnit(timeUnit).build();
    }

    public static InRange inRange(final Comparable start, final Comparable end) {
        return new InRange.Builder().start(start).end(end).build();
    }

    public static InRangeDual inRangeDual(final Comparable start, final Comparable end) {
        return new InRangeDual.Builder().start(start).end(end).build();
    }

    public static InTimeRangeDual inTimeRangeDual(final String start, final String end) {
        return new InTimeRangeDual.Builder().start(start).end(end).build();
    }

    public static InTimeRangeDual inTimeRangeDual(final String start, final String end, final TimeUnit timeUnit) {
        return new InTimeRangeDual.Builder().start(start).end(end).timeUnit(timeUnit).build();
    }

    public static AgeOff ageOff() {
        return new AgeOff();
    }

    public static AgeOffFromDays ageOffFromDays() {
        return new AgeOffFromDays();
    }

    public static And and() {
        return new And();
    }

    public static AreEqual areEqual() {
        return new AreEqual();
    }

    public static AreIn areIn() {
        return new AreIn();
    }

    public static CollectionContains collectionContains() {
        return new CollectionContains();
    }

    public static Exists exists() {
        return new Exists();
    }

    public static If iff() {
        return new If();
    }

    public static IsA isA() {
        return new IsA();
    }

    public static IsEqual isEqual() {
        return new IsEqual();
    }

    public static IsFalse isFalse() {
        return new IsFalse();
    }

    public static IsIn isIn() {
        return new IsIn();
    }

    public static IsLessThan isLessThan() {
        return new IsLessThan();
    }

    public static IsLongerThan isLongerThan() {
        return new IsLongerThan();
    }

    public static IsMoreThan isMoreThan() {
        return new IsMoreThan();
    }

    public static IsShorterThan isShorterThan() {
        return new IsShorterThan();
    }

    public static IsTrue isTrue() {
        return new IsTrue();
    }

    public static IsXLessThanY isXLessThanY() {
        return new IsXLessThanY();
    }

    public static IsXMoreThanY isXMoreThanY() {
        return new IsXMoreThanY();
    }

    public static MapContains mapContains() {
        return new MapContains();
    }

    public static MapContainsPredicate mapContainsPredicate() {
        return new MapContainsPredicate();
    }

    public static MultiRegex multiRegex() {
        return new MultiRegex();
    }

    public static Not not() {
        return new Not();
    }

    public static Or or() {
        return new Or();
    }

    public static Regex regex() {
        return new Regex();
    }

    public static StringContains stringContains() {
        return new StringContains();
    }

    public static PredicateMap predicateMap() {
        return new PredicateMap();
    }
}
