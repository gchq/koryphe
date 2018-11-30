#!/usr/bin/env bash

set -e

if [ "$TRAVIS_PULL_REQUEST" != 'false' ]; then
    bash <(curl -s https://codecov.io/bash)
fi
