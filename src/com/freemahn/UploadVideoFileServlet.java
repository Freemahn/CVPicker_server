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
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by freeemahn on 11.07.15.
 */


@WebServlet("api/uploadVideo")
@MultipartConfig
public class UploadVideoFileServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            //creates a database with the specified name
            Database db = null;
            try {
                db = CloudantClientMgr.getDB();
            } catch (Exception e) {
                response.getWriter().print(e.getMessage());
                return;

            }
            /*more params - name, video(blob)*/
            String name = request.getParameter("id");
            String result = request.getParameter("result");

            ArrayList<Part> parts = (ArrayList<Part>) request.getParts();
            for (int i = 2; i < request.getParts().size(); i++) {
                Part filePart = parts.get(i); // Retrieves <input type="file" name="file">
                //working with blob
                String fileName = getFileName(filePart);
                if (fileName == null || fileName.isEmpty()) continue;
                Map<String, Object> doc = new HashMap<String, Object>();
                String id_file = UUID.randomUUID().toString();
                doc.put("_id", id_file);
                doc.put("owner", name);
                db.save(doc);
                HashMap<String, Object> obj = db.find(HashMap.class, name);
                db.saveAttachment(filePart.getInputStream(), fileName, filePart.getContentType(), name, (String) obj.get("_rev"));

            }
        } catch (Exception e) {
            response.getWriter().println(e.getMessage());
            return;
        }
        response.sendRedirect("test?" + request.getParameter("name"));
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

    public byte[] readContent(Part filePart) throws IOException {
        ByteArrayOutputStream out = null;
        InputStream input = null;
        try {
            out = new ByteArrayOutputStream();
            input = new BufferedInputStream(filePart.getInputStream());
            int data = 0;
            while ((data = input.read()) != -1) {
                out.write(data);
            }
        } finally {
            if (null != input) {
                input.close();
            }
            if (null != out) {
                out.close();
            }
        }
        return out.toByteArray();
    }

}
