# gold-starter
Wrapper for the [gold](https://github.com/nfl/gold) project to provide endpoints for experimentation.

## Architecture
* _REST Controllers_: The project employs Spring Boot and Spring Web to expose _gold_ services
* _Services_: _gold-starter_ simply autowires all Spring components declared in _gold_
  and uses _gold_ Facade: GraphQLSchemaService.executeQuery(...) and GraphQLInstanceService.executeQuery(...).
* _Persistence Layer_: In memory hash maps are used to get the project up and running in no time.

## Features
For the complete documentation of supported features, queries, mutations, etc please refer
to [gold documentation](https://github.com/nfl/gold).
This project simply exposes _gold_via REST.
So, this document only mentions some default and tweaks it applies before calling _gold_.

## Tweaks / defaults
To make experimentation simpler, _gold-starter_ applies a few defaults before calling _gold_ services:
* _Security_: REST controllers automatically grant all _gold_ permissions to all REST calls.
* _Namespaces_: All schemas are in _GOLD_STARTER_SCHEMA_NAMESPACE_.
  All instances are in _GOLD_STARTER_INSTANCE_NAMESPACE_.
  No need to worry about namespaces or to specify them in any REST calls.
* _Nesting depth_ of queries is limited to 5 levels.

## REST endpoint to manage schemas: `/gold/definitions?query={query_contents}`
Since this project simply delegates all processing to _gold_, it supports the same set of operations to
view or modify a schema definition declared in [gold documentation](https://github.com/nfl/gold).  
For a quick start:
* POST the [schema of a Pet](examples/pet/schema_with_lizard.txt) to the *schema definition endpoint*, abbreviated example:  
`POST http://localhost:8080/gold/definitions?query=mutation { upsertSchemaDefinition(schemaDef: { name: "Pet", ... } }`
* GET all schema definitions using [this query](examples/schema_list.txt) to the same endpoint.

## REST endpoint to manage instances: `/gold/instances/{schemaName}?query={query_contents}`
Assuming you have defined a Pet schema as suggested in the section above,
you can populate the in memory repository with little effort:
* POST some pets ([katie_dog.txt](examples/pet/katie_dog.txt),
[tweety_bird.txt](examples/pet/tweety_bird.txt), [larry_lizard.txt](examples/pet/larry_lizard.txt))
to this *instance management endpoint*.  
Example POST request:
```
http://localhost:8080/gold/instances/Pet?query=mutation {
  upsertSchemaInstance(schemaInstance: {
    id: "katie1234",
    name: "katie",
    weight: 75,
    type: Dog
  })
  {
    id,
    name,
    weight,
    type
  }
}
``` 
* GET all pets at the same endpoint using a query like in [instance_list.txt](examples/pet/instance_list.txt).

Just like _gold_, the schema instance interface supports mutation operations
upsertSchemaInstance, removeInstance and removeAllInstances fully
described [there](https://github.com/nfl/gold).
