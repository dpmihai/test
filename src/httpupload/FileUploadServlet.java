package httpupload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.SizeLimitExceededException;
import java.io.*;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 27, 2007
 * Time: 11:15:14 AM
 */
public class FileUploadServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintStream out = new PrintStream(response.getOutputStream());
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        System.out.println("isMultipart=" + isMultipart);
        if (isMultipart) {
            try {
                String where = request.getParameter("name");
                System.out.println("where=" + where);
                
                // Create a new file upload handler
                ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());

                // set a limit file size -> throws SizeLimitExceededException if size is exceeded
                //servletFileUpload.setSizeMax(10*1024*1024); //bytes
                List fileItemsList = servletFileUpload.parseRequest(request);
                // Process the uploaded items               
                for (Object obj : fileItemsList) {
                    FileItem item = (FileItem) obj;
                    if (item.isFormField()) {
                        request.setAttribute(item.getFieldName(), item.get());
                    } else {
                        String fileName = item.getName();
                        String type = item.getContentType();
                        String fileNameOnly = "";
                        fileNameOnly = fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.length());

                        String deploymentDirectory = where;//"upload";
                        File dir = new File(deploymentDirectory);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        System.out.println("path=" + dir.getAbsolutePath());
                        File uploadedFile = new File(deploymentDirectory, fileNameOnly);
                        item.write(uploadedFile);
                        out.print("File " + fileName + " successfully uploaded");
                    }
                }

            }
            catch (SizeLimitExceededException ex) {
                out.print("File size exceeded!");
            }

            catch (ServletException srvEx) {
                throw srvEx;
            }
            catch (IOException ioEx) {
                throw ioEx;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // not a multipart content
            String query = request.getParameter("query");
            System.out.println("query=" + query);
            if ("list".equals(query)) {
                out.print("List queried.");
            } else {
                // query is the relative path of the file to download

                // set the Content-Type header to a nonstandard value such as
                // application/x-download. It's very important that this header is
                // something unrecognized by browsers because browsers often try to do
                // something special when they recognize the content type

                // The HTTP specification recommends setting the Content-Type to
                // application/octet-stream. Unfortunately, this causes problems with Opera 6 on Windows
                // (which will display the raw bytes for any file whose extension it doesn't recognize)
                // and on Internet Explorer 5.1 on the Mac (which will display inline content that would
                // be downloaded if sent with an unrecognized type).
                response.setContentType("application/x-download");
                String filename = (new File(".")).getAbsolutePath() + File.separator + query;

                // set the Content-Disposition header to the value attachment; filename,
                // to be used by default in the Save As dialog
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);

                // Send the file.                
                returnFile(filename, out);
            }
        }
        //request.getRequestDispatcher("/").forward(request, response);
    }

    public static void returnFile(String filename, OutputStream out)
            throws IOException {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(filename));
            byte[] buf = new byte[4 * 1024];  // 4K buffer
            int bytesRead;
            while ((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
        }
        finally {
            if (in != null) in.close();
        }
    }
}
