package net.stumpner.suside.remoteapi.exceptions;

/**
 * User: stumpner
 * Date: 20.03.2008
 * Time: 11:50:31
 *
 * Ein Fehler der beim Aufruf einer Api-Funktion aufgetreten ist
 */
public class ApiCallException extends Exception {

    private int httpStatus = 0;
    private String responseString = "";

    public ApiCallException(int httpStatus, String responseString) {
        this.httpStatus = httpStatus;
        this.responseString = responseString;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getResponseString() {
        return responseString;
    }
}
