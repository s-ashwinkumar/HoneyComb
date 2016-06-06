package fi.vertx;

import fi.core.User;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;

/**
 * Created by ashwin on 6/3/16.
 */
public final class RouterClass {
    /**
     * Constant for Success return.
     */
    private static final int SUCCESS = 200;
    /**
     * Constant for ERROR return.
     */
    private static final int ERROR = 401;

    /**
     * Instantiates a new RouterClass. Private to prevent instantiation.
     */
    private RouterClass() {

        // Throw an exception if this ever *is* called
        throw new AssertionError("Instantiating utility class.");
    }

    /**
     * Login method responding to the post action.
     *
     * @param routingContext receives routing context from vertx.
     */
    static void login(final RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();

        User user = new User(request.getParam("username"),
                request.getParam("password"));
        boolean validUser = user.isValidUser();
        HashMap<String, String> response = new HashMap<>();
        int responseCode;
        if (validUser) {
            response.put("Token", user.getApiToken());
            responseCode = SUCCESS;
        } else {
            response.put("error", "Invalid combination of username and "
                    + "password.");
            responseCode = ERROR;
        }

        routingContext.response()
                .setStatusCode(responseCode)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(response));
    }
}
