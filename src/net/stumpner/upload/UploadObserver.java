package net.stumpner.upload;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 17.11.2006
 * Time: 17:43:41
 * To change this template use File | Settings | File Templates.
 */
public interface UploadObserver {

    public void uploadFinished(File file,String response);
    public void uploadFailed(File file, Exception e);
    public void percentage(int percentValue);

}
