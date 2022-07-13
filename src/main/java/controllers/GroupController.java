package controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.interfaces.GroupServiceInterface;

import java.io.IOException;

public class GroupController implements HttpHandler {

    private GroupServiceInterface groupService;

    public GroupController(GroupServiceInterface groupService) {
        this.groupService = groupService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }

}
