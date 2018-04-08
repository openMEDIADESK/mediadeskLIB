package net.stumpner.upload;

import org.apache.commons.httpclient.methods.multipart.FilePart;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 19.12.2006
 * Time: 22:57:25
 * To change this template use File | Settings | File Templates.
 */
public class FilePartProgress extends FilePart {

    private File file = null;
    private FileProgressListener progressListener = null;

    public FilePartProgress(String s, File file) throws FileNotFoundException {
        super(s, file);
        this.file = file;
    }

    public void setProgressListener(FileProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    /**
     * Write the data in "source" to the specified stream.
     * @param out The output stream.
     * @throws IOException if an IO problem occurs.
     * @see org.apache.commons.httpclient.methods.multipart.Part#sendData(OutputStream)
     */
    protected void sendData(OutputStream out) throws IOException {
        //System.out.println("enter sendData(OutputStream out)");
        if (lengthOfData() == 0) {

            // this file contains no data, so there is nothing to send.
            // we don't want to create a zero length buffer as this will
            // cause an infinite loop when reading.
            //System.out.println("No data to send.");
            return;
        }

        byte[] tmp = new byte[4096];

        InputStream instream = createInputStream();
        float fileLen = file.length();
        float transferred = 0;
        try {
            int len;
            while ((len = instream.read(tmp)) >= 0) {
                out.write(tmp, 0, len);
                transferred += len;
                //System.out.println("written: "+transferred);
                //System.out.println("length : "+fileLen);
                if (progressListener!=null) {
                    float percentage = (transferred/fileLen)*100;
                    //System.out.println("perc: "+percentage);
                    progressListener.donePing((int)percentage);
                }
            }
        } finally {
            // we're done with the stream, close it
            instream.close();
        }
    }

    /**
     * Return a new {@link FileInputStream} for the current filename.
     * @return the new input stream.
     * @throws IOException If an IO problem occurs.
     * @see org.apache.commons.httpclient.methods.multipart.PartSource#createInputStream()
     */
    public InputStream createInputStream() throws IOException {
        if (this.file != null) {
            return new FileInputStream(this.file);
        } else {
            return new ByteArrayInputStream(new byte[] {});
        }
    }
}
