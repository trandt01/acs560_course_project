package com.dtran.testtemplate1;

import android.os.AsyncTask;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class Sync extends AsyncTask<URL, String, String> {
    @Override
    protected String doInBackground(URL... urls) {
        HttpURLConnection urlConnection = null;
        String message = null;
        try{
            int count = urls.length;
            if(count > 0)
            {
                /*
                for (int i = 0; i < urls.length; i++) {
                    urlConnection = (HttpURLConnection)urls[i].openConnection();
                    //urlConnection.connect();

                    //urlConnection.setUseCaches(false);
                    //urlConnection.setDoOutput(true);
                    //urlConnection.setRequestMethod("POST");
                    //urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    //urlConnection.setRequestProperty("Cache-Control", "no-cache");
                    //urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);

                    message = urlConnection.getResponseMessage();
                    String test = "";
                    */

                String charset = "UTF-8";
                //String requestURL = "YOUR_URL";
                //String myDBFile = ("/data/data/com.dtran.testtemplate1/databases/workoutTracker.db");
                String myDBFile = "/data/user/0/com.dtran.testtemplate1/databases/workoutTracker.db";
/*
                //MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                MultipartUtility multipart = new MultipartUtility(urls[0], charset);
                //multipart.addFormField("param_name_1", "param_value");
                //multipart.addFormField("param_name_2", "param_value");
                //multipart.addFormField("param_name_3", "param_value");
                multipart.addFormField("form", "multipart/form-data");
                multipart.addFilePart("sqlitefile", new File(myDBFile));
                List<String> response = multipart.finish(); // response from server.
*/


                HttpURLConnection connection = null;
                DataOutputStream outputStream = null;
                DataInputStream inputStream = null;
                //String pathToOurFile = "/data/file_to_send.mp3";
                String pathToOurFile = myDBFile;
                //String urlServer = "http://192.168.1.1/handle_upload.php";
                String urlServer = urls[0].toString();
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary =  "*****";

                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1*1024*1024;

                try
                {
                    FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );

                    //URL url = new URL(urlServer);
                    URL url = urls[0];
                    connection = (HttpURLConnection) url.openConnection();

                    // Allow Inputs &amp; Outputs.
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);

                    // Set HTTP method to POST.
                    connection.setRequestMethod("POST");

                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

                    outputStream = new DataOutputStream( connection.getOutputStream() );
                    outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    //outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
                    outputStream.writeBytes("Content-Disposition: form-data; name=workoutTracker.db;filename=\"" + pathToOurFile +"\"" + lineEnd);
                    outputStream.writeBytes(lineEnd);

                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // Read file
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0)
                    //while (bytesRead &gt; 0)
                    {
                        outputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    outputStream.writeBytes(lineEnd);
                    outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    //serverResponseCode = connection.getResponseCode();
                    //serverResponseMessage = connection.getResponseMessage();

                    fileInputStream.close();
                    outputStream.flush();
                    outputStream.close();
                }
                catch (Exception ex)
                {
                    //Exception handling
                }
            }
        }
        catch(Exception e){}
        finally {
            urlConnection.disconnect();
        }
        return null;
    }
}