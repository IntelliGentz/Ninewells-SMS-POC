package com.intelligentz.myturn.rest_resources;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intelligentz.myturn.constants.IdeaBizConstants;
import com.intelligentz.myturn.controller.SMSSender;
import com.intelligentz.myturn.exception.IdeabizException;
import com.intelligentz.myturn.handler.SMSHandler;
import com.intelligentz.myturn.handler.SubscriptionHandler;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

/**
 * Created by Lakshan on 2017-04-08.
 */
@Path("/")
public class myturn_resource {
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @POST
    @Path("/")
    public void get(String request) {
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        JsonObject mergeData = jsonObject.get("merge_data").getAsJsonObject();
        int patient_no = Integer.parseInt(mergeData.get("patent_no").getAsString());
        new SMSSender().sendSMStoPatients(patient_no);
    }

}
