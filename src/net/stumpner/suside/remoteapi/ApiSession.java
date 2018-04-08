package net.stumpner.suside.remoteapi;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.io.File;
import java.util.Date;

import net.stumpner.upload.suside.SusideUploadService;
import net.stumpner.suside.remoteapi.exceptions.ApiCallException;
import net.stumpner.suside.remoteapi.exceptions.ObjectNotFoundException;
import net.stumpner.suside.remoteapi.exceptions.ApiException;

/**
 * Klasse zum Zugriff auf die SUSIDE-API.
 *
 * Eine Session wird instanziert indem im Konstruktor die URL der Datenbank
 * sowie Benutzername und Passwort
 *
 * Beispiel:
 * <code>
 *
 *    ApiSession session = new ApiSession("http://demo.suside.net","admin","admin");
 *    session.categoryExists("maincat/subcat");
 *
 * </code>
 */
public class ApiSession {

    private String username = "";
    private String password = "";
    private String URL = "";

    HttpClient client = new HttpClient();

    /**
     * Beginnt eine neue API-Session um Daten mit der Bilddatenbank austauschen
     * zu k�nnen und auf Objekte zugreifen zu k�nnen.
     * @param URL Internet-Adresse der Bilddatenbank (z.b. http://demo.suside.net)
     * @param username Administrator Benutzername, Muss Admin-Berechtigung haben
     * @param password Passwort Benutzername
     */
    public ApiSession(String URL, String username, String password) {
        this.URL = URL;
        this.password = password;
        this.username = username;
    }

    /**
     * L�schen einer Kategorie unter angabe des Kategorie-Namen oder des
     * Kategorie-Pfades.
     * Ein Kategoriepfad setzt sich auch den Namen aller Kategorien und
     * Hauptkategorien zusammen und wird durch / getrennt.
     * Beispiel:
     * maincategory/sub1/sub2/sub3
     * @param categoryName
     * @throws ApiCallException
     */
    public void deleteCategory(String categoryName) throws ApiCallException {
        invoke("categoryDelete",new String[] { categoryName});
    }

    /**
     * Entfernt alle Objekte (Images) von der angegebenen Kategorie
     * @param categoryId
     * @throws ApiCallException
     */
    public void removeObjectsFromCategory(int categoryId) throws ApiCallException {
        invoke("removeObjectsFromCategory",new String[] { String.valueOf(categoryId) });
    }

    /**
     * Datei in die angegebene Kategorie hochladen
     * @param categoryId Kategorie in der die Datei hochgeladen werden soll
     * @param file Lokale Datei die hochgeladen werden soll
     */
    public int uploadFile(int categoryId, File file) {

        SusideUploadService uploadService = new SusideUploadService(URL,username,password);
        uploadService.setCategoryId(categoryId);
        uploadService.setFile(file);
        uploadService.run();
        System.out.println("Upload fertig...");
        return uploadService.getIvId();

    }

    /**
     * Test-Methode um einen "Echo-Request" an die Datenbank zu senden,
     * die Datenbank gibt den �bergebenen Text als "Echo" zur�ck
     * @param text Echotext, der zur�ckgegeben werden soll
     * @throws ApiCallException Wenn ein Fehler aufgetreten ist
     * @return Text der beim Echorequest �bergeben wurde
     */
    public String echo(String text) throws ApiCallException {
        return invoke("echo",new String[] { text });
    }

    /**
     * Pr�ft ob die angegebene Kategorie existiert.
     * Die Kategorie kann in form des Kategorie-Namens oder des Kategorie-Pfades
     * angegeben werden
     * @param categoryName Kategoriename oder Pfadangabe (z.b. maincat/subcat1/subcat2)
     * @return true, wenn die Kategorie existiert, andernfalls false
     * @throws ApiCallException Wenn ein Fehler aufgetreten ist
     */
    public boolean categoryExists(String categoryName) throws ApiCallException {

        String string = invoke("categoryExist",new String[] { categoryName });

        if (string.startsWith("true")) {
            return true;
        } else if (string.startsWith("false")) {
            return false;
        } else {
            throw new ApiCallException(200,string);
        }
    }

    /**
     * Gibt die ID-Nummer der Kategorie unter angabe des Kategorie-Namen oder
     * Kategorie-Pfades zur�ck
     * @param categoryName categoryName Kategoriename oder Pfadangabe (z.b. maincat/subcat1/subcat2)
     * @return Eindeutige ID-Nummer der Kategorie
     * @throws ObjectNotFoundException Wenn es keine Kategorie mit diesen Namen gibt
     * @throws ApiCallException Wenn ein Fehler aufgetreten ist
     */
    public int getCategoryId(String categoryName) throws ObjectNotFoundException, ApiCallException {

        String string = invoke("categoryExist",new String[] { categoryName });

        if (string.startsWith("true")) {
            String[] parts = string.split(";");
            String[] idPart = parts[1].split("=");
            int categoryId = Integer.parseInt(idPart[1]);
            return categoryId;
        } else {
            throw new ObjectNotFoundException();
        }
    }

    /**
     * Erstellt eine neue Kategorie.
     * Es kann entweder ein Kategorienamen angegeben werden, dann wird diese
     * Kategorie als Hauptkategorie angelegt
     * @param categoryName Kategoriename oder Pfadangabe (z.b. maincat/subcat1/subcat2)
     * @throws ApiCallException Wenn ein Fehler aufgetreten ist
     */
    public void createCategory(String categoryName) throws ApiCallException {

        String string = invoke("categoryCreate", new String[] { categoryName });

        if (string.startsWith("OK")) {
            //ok;
        } else {
            throw new ApiCallException(200,string);
        }
    }

    /**
     * Erstellt eine neue Kategorie und setzt den Category-Titel (jener Text
     * der als Titel auf der Website angezeigt wird).
     * @param categoryName Kategoriename oder Pfadangabe (z.b. maincat/subcat1/subcat2)
     * @param categoryTitle Titel der neuen Kategorie so wie er im Web angezeigt werden soll
     * @throws ApiCallException Wenn ein Fehler aufgetreten ist
     */
    public void createCategory(String categoryName, String categoryTitle) throws ApiCallException {

        String string = invoke("categoryCreate", new String[] { categoryName, categoryTitle });

        if (string.startsWith("OK")) {
            //ok;
        } else {
            throw new ApiCallException(200,string);
        }
    }

    /**
     * Gibt das Datum zur�ck an dem die Kategorie ge�ndert wurde.
     * <p>�nderungen sind:</p>
     * <ul>
     *   <li>Kategorie wurde erstellt</li>
     *   <li>Der Kategorie wurde eine neue Datei hinzugef�gt</li>
     *   <li>Aus der Kategorie wurde eine Datei entfernt</li>
     * </ul>
     * @param categoryName Kategoriename oder Pfadangabe (z.b. maincat/subcat1/subcat2)
     * @return Datum, wann die Kategorie das letzte mal ver�ndert wurde
     * @throws ApiCallException Wenn ein Fehler aufgetreten ist
     */
    public Date getCategoryChangeDate(String categoryName) throws ApiCallException {

        String string = invoke("categoryChangeDate", new String[] { categoryName });

        long time = Long.parseLong(string);
        Date date = new Date(time);
        return date;

    }

    /**
     * Gibt eine Liste mit ivid's (Objekten) zur�ck, die in dieser Kategorie sind
     * @param categoryId
     * @return
     */
    public Integer[] getObjectsFromCategory(int categoryId) throws ApiCallException {

        String string = invoke("getObjectsFromCategory", new String[] { String.valueOf(categoryId) });
        String[] idStrings = string.split(";");
        Integer[] ivids = new Integer[idStrings.length];
        System.out.println(string);
        if (idStrings.length>0 && string.length()>0) {
            //Mindestens 1 Objekt muss gefunden werden, aonsten wird eine lere Kategorie zur�ckgegeben
            for (int p=0;p<idStrings.length;p++) {
                ivids[p] = new Integer(idStrings[p]);
            }
            return ivids;
        } else {
            return new Integer[0];
        }

    }

    /**
     * F�gt die angegebene Ivid (Objekt) der Kategorie hinzu
     * @param ivid
     * @param categoryId
     * @throws ApiCallException
     */
    public void addObjectToCategory(int ivid, int categoryId) throws ApiCallException {

        invoke("addObjectToCategory", new String[] { String.valueOf(ivid), String.valueOf(categoryId) });

    }

    public MediaObject getMediaObjectById(int ivid) throws ApiCallException {

        MediaObject mediaObject = new MediaObject();
        String result = invoke("getMediaObjectInfo", new String[] { String.valueOf(ivid) } );
        String[] pairs = result.split(";");

        mediaObject.setIvid(ivid);

        for (int p=0;p<pairs.length;p++) {

            String[] pair = pairs[p].split("=");
            String key = pair[0];
            String value = "";
            if (pair.length==2) { value = pair[1]; }

            if (key.equalsIgnoreCase("createdate")) {
                long longValue = Long.parseLong(value);
                mediaObject.setCreateDate(new Date(longValue));
            }
            if (key.equalsIgnoreCase("versionname")) {
                mediaObject.setVersionName(value);
            }
            if (key.equalsIgnoreCase("versiontitlelng1")) {
                mediaObject.setVersionTitleLng1(value);
            }
            if (key.equalsIgnoreCase("versiontitlelng2")) {
                mediaObject.setVersionTitleLng2(value);
            }
            if (key.equalsIgnoreCase("notelng1")) {
                mediaObject.setNoteLng1(value);
            }
            if (key.equalsIgnoreCase("notelng2")) {
                mediaObject.setNoteLng2(value);
            }
        }

        return mediaObject;
    }

    public void setMediaObject(int ivid, MediaObject mediaObject) throws ApiCallException {

        String result = "";
        result=invoke("setMediaObjectInfo", new String[] { String.valueOf(ivid), "versionname", mediaObject.getVersionName() });
        if (result.equalsIgnoreCase("ERROR")) throw new ApiCallException(200,"ERROR");
        result=invoke("setMediaObjectInfo", new String[] { String.valueOf(ivid), "versiontitlelng1", mediaObject.getVersionTitleLng1() });
        if (result.equalsIgnoreCase("ERROR")) throw new ApiCallException(200,"ERROR");
        result=invoke("setMediaObjectInfo", new String[] { String.valueOf(ivid), "versiontitlelng2", mediaObject.getVersionTitleLng2() });
        if (result.equalsIgnoreCase("ERROR")) throw new ApiCallException(200,"ERROR");
        result=invoke("setMediaObjectInfo", new String[] { String.valueOf(ivid), "notelng1", mediaObject.getNoteLng1() });
        if (result.equalsIgnoreCase("ERROR")) throw new ApiCallException(200,"ERROR");
        result=invoke("setMediaObjectInfo", new String[] { String.valueOf(ivid), "notelng2", mediaObject.getNoteLng2() });
        if (result.equalsIgnoreCase("ERROR")) throw new ApiCallException(200,"ERROR");

    }

    public void deleteMediaObject(int ivid) throws ApiCallException {

        String result = invoke("deleteMediaObject", new String[] { String.valueOf(ivid) });
        if (result.equalsIgnoreCase("ERROR")) throw new ApiCallException(200,"ERROR");
    }

    /**
     * Kategorien in denen dieses Objekt vorkommt
     * @param ivid
     * @return
     * @throws ApiCallException
     */
    public int[] getMediaObjectCategories(int ivid) throws ApiCallException {

        String result = invoke("getMediaObjectCategories", new String[] { String.valueOf(ivid) });
        if (result.equalsIgnoreCase("ERROR")) throw new ApiCallException(200,"ERROR");

        String[] values = result.split(";");
        int[] intValues = new int[values.length];
        for (int a=0;a<values.length;a++) {
            intValues[a] = Integer.parseInt(values[a]);
        }
        return intValues;
    }

    protected String invoke(String method, String[] parameter) throws ApiCallException {


        PostMethod get = new PostMethod(URL+"/gateway/api/");


        get.setParameter("USERNAME",username);
        get.setParameter("PASSWORD",password);
        get.setParameter("method",method);
        for (int a=0;a<parameter.length;a++) {
            get.addParameter("param",parameter[a]);
        }

        int statusCode = 0;

        try {

            statusCode = client.executeMethod(get);

            if (statusCode == HttpStatus.SC_OK) {

                byte[] responseBody = get.getResponseBody();
                String response = new String(responseBody);
                return response;
            } else {
                byte[] responseBody = get.getResponseBody();
                String response = new String(responseBody);
                throw new ApiCallException(statusCode,response);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR";
        } finally {
            get.releaseConnection();
        }
    }

}
