package com.intelligentz.myturn.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intelligentz.myturn.constants.AuthorizationTypes;
import com.intelligentz.myturn.constants.ContentTypes;
import com.intelligentz.myturn.constants.IdeaBizConstants;
import com.intelligentz.myturn.constants.URLs;
import com.intelligentz.myturn.exception.IdeabizException;
import com.intelligentz.myturn.model.RequestMethod;
import org.apache.log4j.Logger;

/**
 * Created by lakshan on 11/13/16.
 */
public class SubscriptionHandler {
    public static Logger logger = Logger.getLogger(SMSHandler.class);

    //sending sms with custom sender name and address
    public boolean suscribe(String msisdn) throws IdeabizException {

        JsonObject requestBodyObject = new JsonObject();
        requestBodyObject.addProperty("message", "WEB");
        requestBodyObject.addProperty("msisdn", msisdn);

        String reqBody  = new Gson().toJson(requestBodyObject);
        logger.info("Subscribing msisdn :"+ reqBody);
        String response = new IdeaBizAPIHandler().sendAPICall(URLs.SUBSCRIPTION_URL, RequestMethod.POST, reqBody,"", ContentTypes.TYPE_JSON,ContentTypes.TYPE_JSON, AuthorizationTypes.TYPE_BEARER);
        logger.info("SMS Response :"+ response);
        JsonParser jsonParser = new JsonParser();
        JsonObject tokenOut;
        try{
             tokenOut = (JsonObject)jsonParser.parse(response);
        }catch (Exception ex){
            throw new IdeabizException("Invalid Response for subscription.");
        }
        if (tokenOut.get("statusCode").getAsString().equals(IdeaBizConstants.SUCCESS_TAG)){
            return true;
        }
        throw new IdeabizException(response);
    }

}
