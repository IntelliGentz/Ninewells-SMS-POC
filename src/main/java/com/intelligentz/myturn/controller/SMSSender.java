package com.intelligentz.myturn.controller;

import com.google.gson.JsonArray;
import com.google.gson.internal.Streams;
import com.intelligentz.myturn.constants.DefaultMessages;
import com.intelligentz.myturn.constants.IdeaBizConstants;
import com.intelligentz.myturn.exception.IdeabizException;
import com.intelligentz.myturn.handler.SMSHandler;
import com.intelligentz.myturn.handler.SubscriptionHandler;
import com.intelligentz.myturn.servlet.UploadServlet;

/**
 * Created by Lakshan on 2017-04-08.
 */
public class SMSSender {
    public void sendSMStoPatients(int current_no) {
        String nextMessage = String.format(DefaultMessages.NEXT_MESSAGE, "A. B. C. Perera", "10", "Ninewells Hospital", String.valueOf(current_no));
        String fifthMessage = String.format(DefaultMessages.FIFTH_MESSAGE, "A. B. C. Perera", "10", "Ninewells Hospital", String.valueOf(current_no));
        sendSMStoNext(current_no+1, nextMessage);
        sendSMStoNext(current_no+5, fifthMessage);
    }

    private void sendSMStoNext(int next_no, String message){
        String mobileNo = SMSHandler.patients.get(String.valueOf(next_no));
        if (mobileNo != null) {
            mobileNo = IdeaBizConstants.MSISDN_PREFIX+mobileNo;
            JsonArray numbers = new JsonArray();
            numbers.add(mobileNo);
            try {
                if (new SubscriptionHandler().suscribe(mobileNo)) {
                    new SMSHandler().sendSMS(numbers, message);
                } else {
                    throw new IdeabizException("User Could Not be Subscribed");
                }
            } catch (IdeabizException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
