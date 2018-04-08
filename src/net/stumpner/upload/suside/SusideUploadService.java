package net.stumpner.upload.suside;

import net.stumpner.upload.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 27.07.2007
 * Time: 17:38:38
 * To change this template use File | Settings | File Templates.
 */
public class SusideUploadService extends UploadService {

    String user = "";
    String password = "";

    /* Weitere einstellmöglichkeiten */

    String categoryName = "";
    String folderName = "";

    int categoryId = -1;

    /* Zurückgelieferte Werte */

    int ivid = -1;

    public SusideUploadService(String susideHost, String user, String password) {

        super(susideHost+ "/gateway/upload/");
        this.addStringPart(new StringPart("USERNAME",user));
        this.addStringPart(new StringPart("PASSWORD",password));
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public void setCategoryId(int id) {
        this.categoryId = id;
    }

    public void run() {

        this.addStringPart(new StringPart("CATEGORYID",String.valueOf(categoryId)));
        this.addStringPart(new StringPart("FOLDERNAME",folderName));
        this.addStringPart(new StringPart("CATEGORYNAME",categoryName));
        
        super.run();    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void parseResponseString(String responseString) throws UploadException {

        if (responseString.indexOf("OK")==-1) {
            //SMS konnte nicht gesendet werden:
            throw new UploadException("Request Failed: "+responseString,UploadException.REQUEST_ERROR);
        } else {
            try {
                String[] resp = responseString.split(";");
                this.ivid = Integer.parseInt(resp[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        

    }

    public int getIvId() {
        return ivid;
    }


}
