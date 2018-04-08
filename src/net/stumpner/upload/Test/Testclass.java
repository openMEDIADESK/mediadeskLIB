
import net.stumpner.upload.UploadObserver;
import net.stumpner.upload.suside.SusideUploadService;
import net.stumpner.suside.remoteapi.ApiSession;
import net.stumpner.suside.remoteapi.exceptions.ApiCallException;
import net.stumpner.suside.remoteapi.exceptions.ObjectNotFoundException;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 27.07.2007
 * Time: 16:30:26
 * To change this template use File | Settings | File Templates.
 */
public class Testclass {

    public static void main(String parameter[]) {

        System.out.println("Testclass for SLibUpload by franz.stumpner");

        ApiSession apiSession = new ApiSession("http://localhost:8080","admin","admin");
        int categoryId = 0;
        try {
            apiSession.createCategory("ERste Kategorie/zwei","perfekt");
            categoryId = apiSession.getCategoryId("ERste Kategorie/zwei");
        } catch (ApiCallException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ObjectNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        SusideUploadService uploadService = new SusideUploadService("localhost:8080","admin","admin");

        //uploadService.setCategoryName("AutoUp1");
        uploadService.setCategoryId(categoryId);
        uploadService.setFile(new File("C:\\DSC04790.JPG"));
        uploadService.setObserver(new UploadObserver() {

            public void uploadFinished(File file, String response) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void uploadFailed(File file, Exception e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void percentage(int percentValue) {
                System.out.println(percentValue+"%");
            }
        });

        uploadService.start();
        try {
            uploadService.join();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        System.out.println("Uploadbeendet");

    }

}
