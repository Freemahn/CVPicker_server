package com.freemahn;

import com.cloudant.client.api.Database;
import example.nosql.CloudantClientMgr;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@WebServlet("api/uploadCV")
@MultipartConfig
public class UploadCVServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = null;
        String result = null;
        String email = null;
        try {

            //creates a database with the specified name
            Database db = null;
            db = CloudantClientMgr.getDB();


            id = request.getParameter("id");
            result = request.getParameter("result");
            email = request.getParameter("email");
            ArrayList<Part> parts = (ArrayList<Part>) request.getParts();
            response.getWriter().write(id + " " + result);
            if (id == null)
                id = UUID.randomUUID().toString();
            if (result == null) {
                //trying to upload CV
                for (int i = 1; i < request.getParts().size(); i++) {
                    Part filePart = parts.get(i); // Retrieves <input type="file" name="file">
                    String fileName = getFileName(filePart);
                    if (fileName == null || fileName.isEmpty()) continue;
                    Map<String, Object> doc = new HashMap<String, Object>();
                    doc.put("_id", id);
                    doc.put("email", email);
                    db.save(doc);
                    HashMap<String, Object> obj = db.find(HashMap.class, id);
                    db.saveAttachment(filePart.getInputStream(), fileName, filePart.getContentType(), id, (String) obj.get("_rev"));
                    response.sendRedirect("/samplequiz.html?id=" + id);
                    //redirect to quiz
                }
            } else {
                //trying to upload Video

                for (int i = 1; i < request.getParts().size(); i++) {
                    Part filePart = parts.get(i); // Retrieves <input type="file" name="file">
                    response.getWriter().write(filePart.getName());

                    String fileName = getFileName(filePart);
                    if (fileName == null || fileName.isEmpty()) continue;
                    //Map<String, Object> doc = new HashMap<String, Object>();
                    HashMap<String, Object> obj = null;
                    obj = db.find(HashMap.class, id);
                    if (obj == null) {

                        obj = new HashMap<String, Object>();
                        obj.put("_id", id);
                        obj.put("result", result);
                        db.save(obj);
                        obj = db.find(HashMap.class, id);
                        db.saveAttachment(filePart.getInputStream(), fileName, filePart.getContentType(), id, (String) obj.get("_rev"));
                    } else {
                        // if existing document
                        //attach the attachment object
                        db.saveAttachment(filePart.getInputStream(), fileName, filePart.getContentType(), id, (String) obj.get("_rev"));
                        obj = db.find(HashMap.class, id + "");
                        obj.put("result", result);
                        db.update(obj);
                        //response.getWriter().write();

                    }

                }
            }
        } catch (IOException e) {
            response.getWriter().println(e.getMessage());
            //  return;
            throw e;
        } catch (ServletException e) {
            throw e;
        }

    }

    private static String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }


}
