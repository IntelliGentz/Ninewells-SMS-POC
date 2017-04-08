package com.intelligentz.myturn.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.intelligentz.myturn.handler.SMSHandler;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet to handle File upload request from Client
 * @author Javin Paul
 */
public class UploadServlet extends HttpServlet {
    //private final String UPLOAD_DIRECTORY = "C:/uploads";
    private final String UPLOAD_DIRECTORY = "/media/ephemeral0/pdesilva/doclk_test/uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathname = "";
        final boolean[] success = {true};
        //process only if its multipart content
        if(ServletFileUpload.isMultipartContent(request)){
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request);

                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        String name = new File(item.getName()).getName();
                        pathname = UPLOAD_DIRECTORY + File.separator + name;
                        item.write( new File(pathname));
                    }
                }
                HashMap<String, String> patients = new HashMap<String, String>();

                Files.lines(Paths.get(pathname)).forEach((line) -> {
                    String[] cust = line.split("\\s+");
                    if(cust.length == 2 && cust[0].matches("[0-9]+") && cust[1].matches("[0-9]+")){
                        if (cust[1].startsWith("0")) {
                            cust[1] = cust[1].substring(1);
                        }
                        if (cust[1].length() == 9) {
                            patients.put(cust[0], cust[1]);
                        }else {
                            success[0] = false;
                        }
                    } else {
                        success[0] = false;
                    }
                });
                SMSHandler.patients = patients;
                //File uploaded successfully
                if (success[0] == true) {
                    request.setAttribute("message", "File Uploaded Successfully");
                }else {
                    request.setAttribute("message", "File has a format issue");
                }
            } catch (Exception ex) {
                request.setAttribute("message", "File Upload Failed due to " + ex);
            }

        }else{
            request.setAttribute("message",
                    "Sorry this Servlet only handles file upload request");
        }

        request.getRequestDispatcher("/upload_results.jsp").forward(request, response);

    }

}