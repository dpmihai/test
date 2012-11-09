package httpupload;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.params.HttpMethodParams;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Feb 26, 2007
 * Time: 5:32:45 PM
 */
public class FileUpload {

    public static void main(String[] args) {

        MultipartFileUploadFrame f = new MultipartFileUploadFrame();
        f.setTitle("HTTP multipart file upload application");
        f.pack();
        f.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
        f.setVisible(true);
    }

    public static class MultipartFileUploadFrame extends JFrame {

        private File targetFile;
        private JTextArea taTextResponse;
        //private DefaultComboBoxModel cmbURLModel;

        public MultipartFileUploadFrame() {
            String url = "http://localhost:8081/servlet/fileupload";
            Map<String, String> map = createServletParameters();
            url = addServletParametersToUrl(url, map);
            System.out.println("URL = " + url);


            JLabel lblTargetFile = new JLabel("Selected file:");

            final JTextField tfdTargetFile = new JTextField(30);
            tfdTargetFile.setEditable(false);

            final JCheckBox cbxExpectHeader = new JCheckBox("Use Expect header");
            cbxExpectHeader.setSelected(false);

            final JButton btnGetList = new JButton("Get");
            btnGetList.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
//                    GetMethod listGet = new GetMethod("http://localhost:8081/servlet/fileupload?query=list");
//                    try {
//                        HttpClient client = new HttpClient();
//                        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
//                        int status = client.executeMethod(listGet);
//                        if (status == HttpStatus.SC_OK) {
//                            appendMessage(
//                                    "Get list complete, response=" + listGet.getResponseBodyAsString()
//                            );
//                        } else {
//                            appendMessage(
//                                    "Get list failed, response=" + HttpStatus.getStatusText(status)
//                            );
//                        }
//                    } catch (Exception ex) {
//                        appendMessage("ERROR: " + ex.getClass().getName() + " " + ex.getMessage());
//                        ex.printStackTrace();
//                    } finally {
//                        listGet.releaseConnection();
//                    }
                    String fileName = "test.jpg";
                    GetMethod listGet = new GetMethod("http://localhost:8081/servlet/fileupload?query=upload/"+fileName);
                    try {
                        HttpClient client = new HttpClient();
                        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
                        int status = client.executeMethod(listGet);
                        if (status == HttpStatus.SC_OK) {
//                            appendMessage(
//                                    "Upload complete, response=" + listGet.getResponseBodyAsString()
//                            );
                            InputStream is = listGet.getResponseBodyAsStream();
                            saveFile("E:\\" + fileName, is);
                            appendMessage("File : " + fileName + " downloaded.");
                        } else {
                            appendMessage(
                                    "Download failed, response=" + HttpStatus.getStatusText(status)
                            );
                        }
                    } catch (Exception ex) {
                        appendMessage("ERROR: " + ex.getClass().getName() + " " + ex.getMessage());
                        ex.printStackTrace();
                    } finally {
                        listGet.releaseConnection();
                    }
                }
            });

            final JButton btnDoUpload = new JButton("Upload");
            btnDoUpload.setEnabled(false);


            final JButton btnSelectFile = new JButton("Select a file...");
            btnSelectFile.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            JFileChooser chooser = new JFileChooser();
                            chooser.setFileHidingEnabled(false);
                            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            chooser.setMultiSelectionEnabled(false);
                            chooser.setDialogType(JFileChooser.OPEN_DIALOG);
                            chooser.setDialogTitle("Choose a file...");
                            if (
                                    chooser.showOpenDialog(MultipartFileUploadFrame.this)
                                            == JFileChooser.APPROVE_OPTION
                                    ) {
                                targetFile = chooser.getSelectedFile();
                                tfdTargetFile.setText(targetFile.toString());
                                btnDoUpload.setEnabled(true);
                            }
                        }
                    }
            );

            taTextResponse = new JTextArea(10, 40);
            taTextResponse.setEditable(false);

            final String targetURL = url;
            btnDoUpload.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    PostMethod filePost = new PostMethod(targetURL);

                    filePost.getParams().setBooleanParameter(HttpMethodParams.USE_EXPECT_CONTINUE,
                            cbxExpectHeader.isSelected());
                    try {
                        appendMessage("Uploading " + targetFile.getName() + " to " + targetURL);
                        Part[] parts = {
                                new FilePart(targetFile.getName(), targetFile)
                        };
                        filePost.setRequestEntity(
                                new MultipartRequestEntity(parts, filePost.getParams())
                        );
                        HttpClient client = new HttpClient();
                        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
                        int status = client.executeMethod(filePost);
                        if (status == HttpStatus.SC_OK) {
                            appendMessage(
                                    "Upload complete, response=" + filePost.getResponseBodyAsString()
                            );
                        } else {
                            appendMessage(
                                    "Upload failed, response=" + HttpStatus.getStatusText(status)
                            );
                        }
                    } catch (Exception ex) {
                        appendMessage("ERROR: " + ex.getClass().getName() + " " + ex.getMessage());
                        ex.printStackTrace();
                    } finally {
                        filePost.releaseConnection();
                    }

                }
            });

            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            add(lblTargetFile, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(10, 5, 5, 0), 0, 0));
            add(tfdTargetFile, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
            add(btnSelectFile, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(5, 5, 5, 10), 0, 0));

            add(cbxExpectHeader, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(Box.createHorizontalGlue());
            panel.add(btnGetList);
            panel.add(Box.createRigidArea(new Dimension(5,5)));
            panel.add(btnDoUpload);
            panel.add(Box.createHorizontalGlue());
            add(panel, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));

            add(new JScrollPane(taTextResponse), new GridBagConstraints(0, 3, 3, 3, 1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
        }

        private void appendMessage(String m) {
            taTextResponse.append(m + "\n");
        }
    }

    public static void saveFile(String fileName, InputStream is) throws IOException {

        FileOutputStream out = new FileOutputStream(fileName);
        try {

            byte[] buf = new byte[4 * 1024];  // 4K buffer
            int bytesRead;
            while ((bytesRead = is.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String addServletParametersToUrl(String url, Map<String, String> servletParameters) {
        StringBuilder sb = new StringBuilder(url);
        List<String> names = new ArrayList<String>(servletParameters.keySet());

        for (int i=0, size = names.size(); i<size; i++) {
            String parameterName = names.get(i);
            String parameterValue = servletParameters.get(parameterName);
            if (i == 0) {
                sb.append("?");
            }
            sb.append(parameterName).append("=").append(parameterValue);
            if (i < size-1) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    //@todo
    private static Map<String, String> createServletParameters() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", "25");
        map.put("description", "test");
        return map;
    }

}


