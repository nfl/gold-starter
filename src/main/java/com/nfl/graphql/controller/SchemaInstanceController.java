package com.nfl.graphql.controller;

import com.nfl.dm.shield.dynamic.security.SchemaWriteAccess;
import com.nfl.dm.shield.dynamic.service.GraphQLInstanceService;
import com.nfl.dm.shield.dynamic.service.GraphQLResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
@Controller
public class SchemaInstanceController extends BaseController {

    private final GraphQLInstanceService graphQLInstanceService;

    public SchemaInstanceController(GraphQLInstanceService graphQLInstanceService) {
        this.graphQLInstanceService = graphQLInstanceService;
    }

    @RequestMapping(value = "/gold/instances/{schemaName}",
            method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> definitionsController(@RequestParam("query") String query,
                                                        @PathVariable("schemaName") String schemaName) {

        SchemaWriteAccess mutablePerms = new SchemaWriteAccess();
        mutablePerms.addPermission(INSTANCE_NAMESPACE, SchemaWriteAccess.INSTANCE_MODIFY);
        mutablePerms.addPermission(INSTANCE_NAMESPACE, SchemaWriteAccess.INSTANCE_DELETE);
        mutablePerms.addPermission(INSTANCE_NAMESPACE, SchemaWriteAccess.INSTANCE_TRUNCATE);

        GraphQLResult result = graphQLInstanceService.executeQuery(query, buildVariablesMap(schemaName), mutablePerms, 8);

        // add a data container for the graphiql introspection query
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("data", result.getData());
        return result.isSuccessful() ? buildSuccessResponse(response) :
                new ResponseEntity<>(result.getErrors().get(0), HttpStatus.BAD_REQUEST);    }

}
