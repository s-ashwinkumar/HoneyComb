package fi.vertx;

import fi.core.FaultInjector;
import fi.core.User;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ashwin on 6/3/16.
 */
public final class RouterClass {
    /**
     * Constant for Success return.
     */
    private static final int SUCCESS = 200;
    /**
     * Constant ERROR for authentication return.
     */
    private static final int ERROR = 401;

    /**
     * The request could not be understood by the server due to malformed syntax.
     */
    private static final int BADREQUEST = 400;

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

    /**
     * inject method respoding to post action
     *
     * @param routingContext receives routing context from vertx.
     */
    public static void inject(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();

        boolean validateToken = User.isValidUser(request.getParam("token"));

        FaultInjector object1;

        if(validateToken) {
            object1 = new FaultInjector(
                    request.getParam("faultId"), request.params());
            StringBuilder reason = new StringBuilder();
            if (!object1.validate(reason)) {
                /**
                 * Missing some parameter
                 */
                HashMap<String, String> response = new HashMap<>();
                int responseCode;

                response.put("error", "missing argument: " + reason.toString());
                responseCode = BADREQUEST;

                routingContext.response()
                        .setStatusCode(responseCode)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(response));
            } else {
                /**
                 * every validation pass! ready to inject
                 */
                String faultInstanceId = null;
                try {
                    faultInstanceId = object1.inject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /**
                 * success injection start
                 */
                HashMap<String, String> response = new HashMap<>();
                int responseCode;

                response.put("success", "Fault start injection");
                response.put("faultInstanceId", faultInstanceId);
                responseCode = SUCCESS;

                routingContext.response()
                        .setStatusCode(responseCode)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(response));

            }
        } else {
            /**
             * authentication pass.
             */
            HashMap<String, String> response = new HashMap<>();
            int responseCode;

            response.put("error", "Invalid token. No permission access.");
            responseCode = ERROR;

            routingContext.response()
                    .setStatusCode(responseCode)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(response));

        }
    }
}
