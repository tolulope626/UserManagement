package dev.tolulope;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import dev.tolulope.entity.User;

import java.util.List;
import java.util.Map;

public class UserService {

    private DynamoDBMapper dynamoDBMapper;
    private String jsonBody = null;

    //POST method to create a new user
    public APIGatewayProxyResponseEvent createUser(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        try{
            initDynamoDB();
            User user = Utility.convertStringToObj(apiGatewayRequest.getBody(), context);
            // validating parameters
            /*
            if(user.getFirstName() == null || user.getLastName() == null
                    || user.getPassword() == null || user.getEmail() == null || user.getUsername() == null){
                return createAPIResponse("Invalid User data", 400,Utility.createHeaders());
            }
            */

            dynamoDBMapper.save(user);
            jsonBody = Utility.convertObjToString(user, context);
            context.getLogger().log("user created successfully to dynamoDB:::" + jsonBody);
            return createAPIResponse(jsonBody, 201, Utility.createHeaders());
        } catch (Exception e){
            return createAPIResponse("Error creating user", 500, Utility.createHeaders());
        }

    }

    //GET[userId] to get a specific user
    public APIGatewayProxyResponseEvent getUserID(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        try{
            initDynamoDB();
            String userId = apiGatewayRequest.getPathParameters().get("userId");
            User user = dynamoDBMapper.load(User.class, userId);
            if(user != null){
                jsonBody = Utility.convertObjToString(user, context);
                context.getLogger().log("Getting User By ID:::"+ jsonBody);
                return createAPIResponse(jsonBody, 200, Utility.createHeaders());
            }else{
                jsonBody = "User not Found :" + userId;
                return createAPIResponse(jsonBody, 400, Utility.createHeaders());

            }

        } catch (Exception e){
            return createAPIResponse("Error fetching user", 500, Utility.createHeaders());
        }

    }

    //GET method to get all the Users
    public APIGatewayProxyResponseEvent getUsers(APIGatewayProxyRequestEvent apiGatewayRequest, Context context){
        try{
            initDynamoDB();
            List<User> users = dynamoDBMapper.scan(User.class, new DynamoDBScanExpression());
            jsonBody = Utility.convertListOfObjToString(users, context);
            context.getLogger().log("Get USer List:::"+ jsonBody);
            return createAPIResponse(jsonBody, 200, Utility.createHeaders());

        }catch (Exception e){
            return createAPIResponse("Error Fetching Users", 500, Utility.createHeaders());
        }
    }

    //DELETE[ID] method removes a user
    public APIGatewayProxyResponseEvent deleteUserById(APIGatewayProxyRequestEvent apiGatewayRequest, Context context){
        try{
            initDynamoDB();
            String userId = apiGatewayRequest.getPathParameters().get("userId");
            User user =  dynamoDBMapper.load(User.class, userId)  ;
            if(user!=null) {
                dynamoDBMapper.delete(user);
                context.getLogger().log("user deleted successfully :::" + userId);
                return createAPIResponse("User deleted successfully." + userId,200,Utility.createHeaders());
            }else{
                jsonBody = "User Not Found :" + userId;
                return createAPIResponse(jsonBody,400,Utility.createHeaders());
            }
        }catch (Exception e){
            return createAPIResponse("Error Deleting User",500,Utility.createHeaders());
        }

    }

    //UPDATE[ID] updates the user info by ID
    public APIGatewayProxyResponseEvent updateUserByID(APIGatewayProxyRequestEvent apiGatewayRequest, Context context) {
        try{
            initDynamoDB();
            String userId = apiGatewayRequest.getPathParameters().get("userId");
            User user = dynamoDBMapper.load(User.class, userId);
            // checks if user exists
            if(user != null){
                jsonBody = Utility.convertObjToString(user, context);

                User newuser = Utility.convertStringToObj(apiGatewayRequest.getBody(), context);
                /*
                if(newuser == null || newuser.getFirstName() == null
                        || user.getLastName() == null || user.getPassword() == null
                        || user.getEmail() == null || user.getUsername() == null){
                    return createAPIResponse("Invalid User data", 400,Utility.createHeaders());
                }
                **/
                user.setUsername(newuser.getUsername());
                user.setPassword(newuser.getPassword());
                user.setEmail(newuser.getEmail());
                user.setFirstName(newuser.getFirstName());
                user.setLastName(newuser.getLastName());

                dynamoDBMapper.save(user);
                jsonBody = Utility.convertObjToString(user, context);
                context.getLogger().log("Updated User By ID:::"+ jsonBody);
                return createAPIResponse(jsonBody, 200, Utility.createHeaders());
            }else{
                jsonBody = "User not Found to Update:" + userId;
                return createAPIResponse(jsonBody, 400, Utility.createHeaders());
            }

        } catch (Exception e){
            return createAPIResponse("Error Updating user", 500, Utility.createHeaders());
        }

    }


    private void initDynamoDB() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        dynamoDBMapper = new DynamoDBMapper(client);
    }

    private APIGatewayProxyResponseEvent createAPIResponse(String body, int statusCode, Map<String,String> headers){
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(body);
        responseEvent.setHeaders(headers);
        responseEvent.setStatusCode(statusCode);
        return responseEvent;
    }


}
