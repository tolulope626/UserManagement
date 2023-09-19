package dev.tolulope;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tolulope.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {
    public static Map<String,String> createHeaders(){
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("X-amazon-author","Tolu");
        headers.put("X-amazon-apiVersion","v1");
        return  headers ;
    }

    public static String convertObjToString(User user, Context context){
        String jsonBody = null;
        try {
            jsonBody =   new ObjectMapper().writeValueAsString(user);
        } catch (JsonProcessingException e) {
            context.getLogger().log( "Error while converting obj to string:::" + e.getMessage());
        }
        return jsonBody;
    }
    public static User convertStringToObj(String jsonBody,Context context){
        User user = null;
        try {
            user =   new ObjectMapper().readValue(jsonBody,User.class);
        } catch (JsonProcessingException e) {
            context.getLogger().log( "Error while converting string to obj:::" + e.getMessage());
        }
        return user;
    }
    public static String convertListOfObjToString(List<User> employees, Context context){
        String jsonBody = null;
        try {
            jsonBody =   new ObjectMapper().writeValueAsString(employees);
        } catch (JsonProcessingException e) {
            context.getLogger().log( "Error while converting obj to string:::" + e.getMessage());
        }
        return jsonBody;
    }
}