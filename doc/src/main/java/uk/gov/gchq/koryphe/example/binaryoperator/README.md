Copyright 2017 Crown Copyright

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

# Koryphe Binary Operator Examples
 
## Adapted Binary Operator applied to a complex object

This example shows how you can apply a simple binary operator (in this case Product) to aggregate a field in a complex object. We will configure the Product function to apply to field 'A' in a Tuple. The result of the product function will be projected back into field 'A'.

<img src="complexBinaryOperator.png" width="600">

[TupleAggregationProductExample.java](TupleAggregationProductExample.java) shows how to create this operator using the Java API. Execute it to produce the following output:

```
Binary Operator json: 
{
  "binaryOperator" : {
    "class" : "uk.gov.gchq.koryphe.impl.binaryoperator.Product"
  },
  "selection" : [ "A" ]
}

Binary Operator inputs: 
{1,2,3}
{4,5,6}
{7,8,9}

Binary Operator output: 
{28,2,3}
```

Field 'A' has been selected from each of the input tuples and passed to the Product operator. The result has been projected back into the first input tuple.

## Composite adapted Binary Operator applied to a complex object

This example shows the composition of a complex Binary Operator by applying simple operators to different fields in a complex object. We will configure the Product, Sum and Min functions to apply to fields A, B and C respectively.

<img src="complexCompositeBinaryOperator.png" width="600">

[TupleCompositeAggregationExample.java](TupleCompositeAggregationExample.java) shows how to create this operator using the Java API. Execute it to produce the following output:

```
Binary Operator json: 
{
  "operators" : [ {
    "binaryOperator" : {
      "class" : "uk.gov.gchq.koryphe.impl.binaryoperator.Product"
    },
    "selection" : [ "A" ]
  }, {
    "binaryOperator" : {
      "class" : "uk.gov.gchq.koryphe.impl.binaryoperator.Sum"
    },
    "selection" : [ "B" ]
  }, {
    "binaryOperator" : {
      "class" : "uk.gov.gchq.koryphe.impl.binaryoperator.Min"
    },
    "selection" : [ "C" ]
  } ]
}

Binary Operator inputs: 
{1,2,3}
{4,5,6}
{7,8,9}

Binary Operator output: 
{28,15,3}
```

Field 'A' has been aggregated by the Product operator, field 'B' by the Sum operator and field 'C' by the Min operator.