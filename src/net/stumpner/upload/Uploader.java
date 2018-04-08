package net.stumpner.upload;

import javax.swing.*;

import net.stumpner.upload.ui.UploadPopup;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 17.11.2006
 * Time: 17:11:43
 * To change this template use File | Settings | File Templates.
 */
public interface Uploader {

    public void doUpload(UploadPopup uploadPopup);

    public void doUpload(UploadPopup uploadPopup, JProgressBar progressBar);

}
