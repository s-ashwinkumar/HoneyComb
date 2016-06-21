package fi.vertx;

import fi.core.DbConnection;
import fi.core.FaultInjector;
import fi.core.FaultModel;
import fi.core.User;
import fi.core.Utils;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.List;

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
    boolean validUser = user.isValidUser(User.getFileName());
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
    returnResponse(routingContext, responseCode, response);
  }

  /**
   * FaultList method responding to the get action.
   *
   * @param routingContext receives routing context from vertx.
   */
  static void faultList(final RoutingContext routingContext) {
    HttpServerRequest request = routingContext.request();
    HashMap<String, String> response = new HashMap<>();
    String token = request.getParam("token");
    int responseCode;
    try {
      boolean validUser = User.isValidUser(token, User.getFileName());
      if (validUser) {
        DbConnection dbCon = Utils.returnDbconnection();
        List<FaultModel> list = FaultModel.getFaults(dbCon);
        responseCode = SUCCESS;
        dbCon.getConn().close();
        returnResponse(routingContext, responseCode, list);

      } else {
        response.put("error", "You are not unauthorized to make this request.");
        responseCode = ERROR;
        returnResponse(routingContext, responseCode, response);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
      response.put("error", "Something went wrong.Please try again later");
      responseCode = ERROR;
      returnResponse(routingContext, responseCode, response);
    }
  }

  /**
   * Routing method for removing a fault.
   *
   * @param routingContext receives routing context from vertx.
   */
  static void removeFault(final RoutingContext routingContext) {
    HttpServerRequest request = routingContext.request();
    HashMap<String, String> response = new HashMap<>();
    String faultId = request.getParam("faultId");
    String token = request.getParam("token");
    int responseCode;
    try {
      if (!Utils.isNumeric(faultId)) {
        response.put("error", "The parameter fault ID is not a number");
        responseCode = ERROR;
        returnResponse(routingContext, responseCode, response);
      }
      boolean validUser = User.isValidUser(token, User.getFileName());
      if (validUser) {
        DbConnection dbCon = Utils.returnDbconnection();
        Integer res = FaultModel.removeFault(faultId, dbCon);
        if (res == 0) {
          responseCode = ERROR;
          response.put("error", "No fault for given fault ID.");
        } else {
          responseCode = SUCCESS;
          response.put("response", "Fault " + faultId + " has been marked "
              + "unusable in the system");
        }
        dbCon.getConn().close();
      } else {
        response.put("error", "You are not unauthorized to make this request.");
        responseCode = ERROR;
      }

    } catch (Exception ex) {
      ex.printStackTrace();
      response.put("error", "Something went wrong.Please try again later");
      responseCode = ERROR;
      returnResponse(routingContext, responseCode, response);
    }
    returnResponse(routingContext, responseCode, response);

  }

  /**
   * inject method responding to post action
   *
   * @param routingContext receives routing context from vertx.
   */
  static void inject(RoutingContext routingContext) {
    HttpServerRequest request = routingContext.request();
    HashMap<String, String> response = new HashMap<>();
    String token = request.getParam("token");
    String faultId = request.getParam("faultId");
    String faultInstanceId = null;
    int responseCode;
    try {
      if (!Utils.isNumeric(faultId)) {
        response.put("error", "The parameter fault ID is not a number");
        responseCode = ERROR;
        returnResponse(routingContext, responseCode, response);
      }
      boolean validUser = User.isValidUser(token, User.getFileName());
      if (validUser) {
        StringBuilder reason = new StringBuilder();
        FaultInjector injector = new FaultInjector(faultId, request.params());
        if (injector.validate(reason)) {
          faultInstanceId = injector.inject();
          response.put("success", "Fault start injection");
          response.put("faultInstanceId", faultInstanceId);
          responseCode = SUCCESS;
          returnResponse(routingContext, responseCode, response);
        } else {
          response.put("error", "Missing arguments: " + reason.toString());
          responseCode = BADREQUEST;
          returnResponse(routingContext, responseCode, response);
        }

      } else {
        response.put("error", "You are not unauthorized to make this request.");
        responseCode = ERROR;
        returnResponse(routingContext, responseCode, response);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      response.put("error", "Something went wrong.Please try again later");
      responseCode = ERROR;
      returnResponse(routingContext, responseCode, response);
    }
  }

  /**
   * A Helper method to respond to request.
   *
   * @param routingContext Routing context object from vertx
   * @param responseCode   response code to send
   * @param response       response message to send
   */
  private static void returnResponse(final RoutingContext routingContext, int
      responseCode, Object response) {
    routingContext.response()
        .setStatusCode(responseCode)
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(Json.encodePrettily(response));
  }

}
