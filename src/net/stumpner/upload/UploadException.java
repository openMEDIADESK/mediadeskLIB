package net.stumpner.upload;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 16.11.2006
 * Time: 17:48:15
 * To change this template use File | Settings | File Templates.
 */
public class UploadException extends Exception {

    public static final int AUTH_ERROR = 1;
    public static final int HTTP_ERROR = 2;
    public static final int REQUEST_ERROR = 3;

    int error = 0;

    public UploadException(String message, int error) {
        super(message);
        this.error = error;
    }

    public int getError() {
        return error;
    }

}
