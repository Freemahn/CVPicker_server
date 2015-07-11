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


@WebServlet("api/uploadCV")
@MultipartConfig
public class UploadCVServlet extends HttpServlet {


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
            /*2 params -name and file*/
            String name = request.getParameter("name");
            ArrayList<Part> parts = (ArrayList<Part>) request.getParts();
            for (int i = 1; i < request.getParts().size(); i++) {
                Part filePart = parts.get(i); // Retrieves <input type="file" name="file">

                String fileName = getFileName(filePart);
                if (fileName == null || fileName.isEmpty()) continue;
                Map<String, Object> doc = new HashMap<String, Object>();
                String id = UUID.randomUUID().toString();
                doc.put("_id", id);
                doc.put("owner", name);
                db.save(doc);
                HashMap<String, Object> obj = db.find(HashMap.class, id);
                db.saveAttachment(filePart.getInputStream(), fileName, filePart.getContentType(), id, (String) obj.get("_rev"));

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
