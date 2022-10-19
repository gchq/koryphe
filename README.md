<img align="right" width="200" height="auto" src="logos/koryphe_logo_text.png">

# Koryphe

![ci](https://github.com/gchq/koryphe/actions/workflows/continuous-integration.yaml/badge.svg?branch=develop)
[![codecov](https://codecov.io/gh/gchq/koryphe/branch/master/graph/badge.svg?token=vdFm5s0HCR)](https://codecov.io/gh/gchq/koryphe)
[<img src="https://img.shields.io/badge/docs-passing-success.svg?logo=readthedocs">](https://gchq.github.io/gaffer-doc/latest/)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/uk.gov.gchq.koryphe/koryphe/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.gov.gchq.koryphe/koryphe)

Koryphe is an extensible functions library for filtering, aggregating and transforming data  
based on the Java Function API. It provides:

 - Context adapted functions.
 - Function composition.
 - Functions with multiple inputs/outputs.
 - JSON serialisation.
 - Library of re-usable functions.
 
## Context adapted functions

Koryphe allows any Java 11 Function, BinaryOperator or Predicate to be adapted and applied to the values contained within a complex object such as a Tuple.

## Function composition

Functions (or BinaryOperators or Predicates) can be combined and applied together in a single composite function. This is particularly powerful when combined with context adapted functions, allowing composite functions to be built from a library of simple functions and applied to complex objects.

## Functions with multiple inputs/outputs

Koryphe can combine inputs and outputs into tuples, providing type-safe interfaces for functions that apply to and/or return more than one value.

## JSON serialisation

Functions, including composites, can be JSON serialised, allowing them to be used across distributed applications.

## Library of re-usable functions

Koryphe provides a number of useful Functions, BinaryOperators and Predicates for building simple applications.

## License

Koryphe is licensed under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0) and is covered by [Crown Copyright](https://www.nationalarchives.gov.uk/information-management/re-using-public-sector-information/uk-government-licensing-framework/crown-copyright/).

## Contributing
We welcome contributions to the project. Detailed information on our ways of working can be found [here](https://gchq.github.io/gaffer-doc/latest/ways-of-working/). In brief:

- Sign the [GCHQ Contributor Licence Agreement](https://cla-assistant.io/gchq/koryphe);
- Push your changes to a fork;
- Submit a pull request.
