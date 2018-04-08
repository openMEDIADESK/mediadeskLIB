package net.stumpner.upload.Test;

import net.stumpner.suside.remoteapi.ApiSession;
import net.stumpner.suside.remoteapi.exceptions.ApiCallException;
import net.stumpner.suside.remoteapi.exceptions.ObjectNotFoundException;

import java.io.File;

/**
 * User: stumpner
 * Date: 20.03.2008
 * Time: 10:43:05
 */
public class ApiSessionUploadTest {

    public static void main(String[] args) {

        ApiSession apiSession = new ApiSession("http://localhost:8080","admin","admin");
        try {
            apiSession.createCategory("APIe Kategorie/zwei","perfekt");
            int categoryId = apiSession.getCategoryId("APIe Kategorie/zwei");
            apiSession.uploadFile(categoryId,new File("C:\\DSC04790.JPG"));
            System.out.println("Upload beendet");
        } catch (ApiCallException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

}
