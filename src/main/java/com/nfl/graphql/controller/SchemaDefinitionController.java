package com.nfl.graphql.controller;

import com.nfl.dm.shield.dynamic.security.SchemaWriteAccess;
import com.nfl.dm.shield.dynamic.service.GraphQLResult;
import com.nfl.dm.shield.dynamic.service.GraphQLSchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
@Controller
public class SchemaDefinitionController extends BaseController {

    private final GraphQLSchemaService graphQLSchemaService;

    @Autowired
    public SchemaDefinitionController(GraphQLSchemaService graphQLSchemaService) {
        this.graphQLSchemaService = graphQLSchemaService;
    }

    @RequestMapping(value = "/gold/definitions",
            method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> definitionsController(@RequestParam("query") String query) {

        SchemaWriteAccess mutablePerms = new SchemaWriteAccess();
        mutablePerms.addPermission(SCHEMA_NAMESPACE, SchemaWriteAccess.SCHEMA_MODIFY);

        GraphQLResult result = graphQLSchemaService.executeQuery(query, buildVariablesMap(""), mutablePerms);

        // add a data container for the graphiql introspection query
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", result.getData());
        return result.isSuccessful() ? buildSuccessResponse(response) :
                new ResponseEntity<>(result.getErrors().get(0), HttpStatus.BAD_REQUEST);
    }
}
