package dev.tolulope;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;


public class UserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        UserService userService = new UserService();
        switch(apiGatewayRequest.getHttpMethod()){
            case "POST":
                //create user
                return userService.createUser(apiGatewayRequest, context);
            case "GET":
                if(apiGatewayRequest.getPathParameters() != null){
                    // fetch users by id
                    return userService.getUserID(apiGatewayRequest, context);
                }
                // fetch all users
                return userService.getUsers(apiGatewayRequest, context);
            case "DELETE":
                if(apiGatewayRequest.getPathParameters() != null){
                    // delete users by id
                    return userService.deleteUserById(apiGatewayRequest, context);
                }
            case "PUT":
                if(apiGatewayRequest.getPathParameters() != null){
                    // delete users by id
                    return userService.updateUserByID(apiGatewayRequest, context);
                }

            default:
                // throw some error
                throw new Error("Unsupported Methods :::" + apiGatewayRequest.getHttpMethod());
        }
    }
}
