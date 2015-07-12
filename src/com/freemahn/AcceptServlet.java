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
import java.util.Arrays;
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
        String subj = "CVPicker - Status";
        String html = "Your CV has been approved. We'll contact you about interview soon";
        sendMessage(id, email, subj, html);


        resp.sendRedirect("/api/dashboard");
    }

    static void sendMessage(String id, String email_to, String subj, String html) {

        MandrillApi mandrillApi = new MandrillApi("14_i84eYVORJv2k7-lCQ8Q");

// create your message
        MandrillMessage message = new MandrillMessage();
        message.setSubject(subj);
        message.setHtml(html);
        message.setAutoText(true);
        message.setFromEmail("no-reply@cvpicker.com");
        message.setFromName("CVPicker");
// add recipients
        ArrayList<MandrillMessage.Recipient> recipients = new ArrayList<MandrillMessage.Recipient>();
        MandrillMessage.Recipient recipient = new MandrillMessage.Recipient();
        recipient.setEmail(email_to);
        recipients.add(recipient);
        message.setTo(recipients);
        message.setPreserveRecipients(true);

        try {
            MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(message, false);
        } catch (MandrillApiError mandrillApiError) {
            mandrillApiError.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
