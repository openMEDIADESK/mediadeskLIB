//package net.stumpner.upload.Test;

import net.stumpner.suside.remoteapi.ApiSession;
import net.stumpner.suside.remoteapi.exceptions.ApiCallException;

import java.util.Date;

/**
 * User: stumpner
 * Date: 20.03.2008
 * Time: 11:22:45
 */
public class ApiTestclass {

    public static void main(String[] args) {


        //deleteCategory();
        //categoryDate();

        //burnInTest();

        ividTools();


    }

    public static void ividTools() {

        //ApiSession apiSession = new ApiSession("http://localhost:8080","admin","admin");
        ApiSession apiSession = new ApiSession("https://demo.openmediadesk.org","admin","admin");



        try {

            //Integer[] objects = apiSession.getObjectsFromCategory(146);
            Integer[] objects = apiSession.getObjectsFromCategory(146);

            System.out.println("Objects: "+objects.length);
            for (int a=0;a<objects.length;a++) {
                System.out.println("Object: "+objects[a]);
            }

            /*
            apiSession.removeObjectsFromCategory(4526);
            Integer[] ivid = new Integer[0];
            ivid = apiSession.getObjectsFromCategory(4545);
            if (ivid.length==0) {
                System.out.println("Keine Objekte in dieser Kategorie");
            }
            for (int p=0;p<ivid.length;p++) {
                System.out.println("ivid: "+ivid[p]);
                //apiSession.addObjectToCategory(ivid[p],4526);
            }
            */
        } catch (ApiCallException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static void burnInTest() {

        ApiSession apiSession = new ApiSession("http://localhost:8080","admin","admin");

        for (int a=0;a<5000;a++) {
            String testCat = "test/burnin/folder"+ a + "/sub1/sub2";
            System.out.println("Request No: "+a);

            try {
                apiSession.categoryExists(testCat);
            } catch (ApiCallException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    /**
     *
     */
    public static void categoryDate() {

        ApiSession apiSession = new ApiSession("http://localhost:8080","admin","admin");

        try {
            Date date = apiSession.getCategoryChangeDate("test1/datum");
            System.out.println("Changedate = "+ date);
            
             //System.out.print("categoryExists: "+apiSession.categoryExists("testcat2"));
        } catch (ApiCallException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static void categoryDelete() {

        ApiSession apiSession = new ApiSession("http://localhost:8080","admin","admin");

        try {
            apiSession.deleteCategory("test1/sub1");
             System.out.print("categoryExists: "+apiSession.categoryExists("testcat2"));
        } catch (ApiCallException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public static void categoryCreate() {

        ApiSession apiSession = new ApiSession("http://localhost:8080","admin","admin");

        try {
            apiSession.createCategory("testcat6/sub6","perfekto");
             System.out.print("categoryExists: "+apiSession.categoryExists("testcat2"));
        } catch (ApiCallException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
