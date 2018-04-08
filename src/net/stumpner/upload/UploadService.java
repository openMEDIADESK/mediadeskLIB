package net.stumpner.upload;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 27.07.2007
 * Time: 17:41:23
 * To change this template use File | Settings | File Templates.
 */
public class UploadService extends Thread {

    String url = "localhost:8080";
    File file = null;
    UploadObserver uploadObserver = null;
    List stringParts = new LinkedList();
    private String response = "";

    public UploadService(String url) {

        if (!url.startsWith("http://")) {
            url = "http://"+url;
        }

        this.url = url;
    }

    public void setObserver(UploadObserver uploadObserver) {
        this.uploadObserver = uploadObserver;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Um ein zusätzliches Form-Feld zum Post Request hinzuzufügen
     * @param stringPart
     */
    public void addStringPart(StringPart stringPart) {
        stringParts.add(stringPart);
    }

    public void run() {
        try {
            String response = doUpload(this.file);
            if (this.uploadObserver != null) {
                uploadObserver.uploadFinished(file, response);
            }
        } catch (FileNotFoundException e) {
            if (this.uploadObserver != null) {
                uploadObserver.uploadFailed(file, e);
            }
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UploadException e) {
            if (this.uploadObserver != null) {
                uploadObserver.uploadFailed(file, e);
            }
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String doUpload(File file) throws FileNotFoundException, UploadException {

        String responseString = "";
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);

        Part[] parts = this.getAllParts();

        method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));

        // Eventuell Retry-Handler:
        //todo: retry-handler
        /*
        DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
        retryhandler.setRequestSentRetryEnabled(false);
        retryhandler.setRetryCount(3);
        method.setMethodRetryHandler(retryhandler);
        */

        try {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                if (statusCode == 403) {
                    throw new UploadException("HTTP Failed: " + statusCode + " " + method.getStatusLine(), UploadException.AUTH_ERROR);
                } else {
                    throw new UploadException("HTTP Failed: " + statusCode + " " + method.getStatusLine(), UploadException.HTTP_ERROR);
                }
            }

            byte[] responseBody = method.getResponseBody();
            responseString = new String(responseBody);
            this.parseResponseString(responseString);

        } catch (HttpException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new UploadException("HTTP Exception: " + e.getMessage(), UploadException.HTTP_ERROR);

        } catch (IOException e) {
            throw new UploadException("IO Exception: " + e.getMessage(), UploadException.HTTP_ERROR);
        } finally {
            method.releaseConnection();
        }

        return responseString;
    }

    protected void parseResponseString(String responseString) throws UploadException {
    }

    private Part[] getAllParts() throws FileNotFoundException {

        FilePartProgress filePart = new FilePartProgress(file.getName(), file);
        filePart.setProgressListener(new FileProgressListener() {
            //Progress-Listener erstellen, dem die aktuelle Übertragung mitgeteilt wird
            public void donePing(int percentage) {
                if (uploadObserver!=null) {
                    uploadObserver.percentage(percentage);
                }
            }
        });

        Part[] parts = new Part[getStringParts().size()+1];
        Iterator stringParts = getStringParts().iterator();
        int counter=0;
        while (stringParts.hasNext()) {
            parts[counter]=(StringPart)stringParts.next();
            counter++;
        }
        parts[counter]=filePart;

        return parts;
    }

    protected List getStringParts() {
        return stringParts;
    }


}
