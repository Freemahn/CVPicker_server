package com.freemahn;


import com.cloudant.client.api.Database;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import example.nosql.CloudantClientMgr;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by freeemahn on 12.07.15.
 */
public class AcceptServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Database db = CloudantClientMgr.getDB();

        String id = req.getParameter("id");
        HashMap<String, Object> user = db.find(HashMap.class, id + "");
        String email = user.get("email").toString();
        MandrillApi mandrillApi = new MandrillApi("14_i84eYVORJv2k7-lCQ8Q");

// create your message
        MandrillMessage message = new MandrillMessage();
        message.setSubject("CVPicker - Status");
        message.setHtml("Your CV has been approved. We'll contact you about interview soon");
        message.setAutoText(true);
        message.setFromEmail("no-reply@cvpicker.com");
        message.setFromName("CVPicker");
// add recipients
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(email);
        recipients.add(recipient);
        message.setTo(recipients);
        message.setPreserveRecipients(true);
     /*   ArrayList<String> tags = new ArrayList<String>();
        tags.add("test");
        tags.add("helloworld");
        message.setTags(tags);*/
// ... add more message details if you want to!
// then ... send
        try {
            MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(message, false);
        } catch (MandrillApiError mandrillApiError) {
            mandrillApiError.printStackTrace();
        }
        resp.sendRedirect("/api/dashboard");
    }
}
