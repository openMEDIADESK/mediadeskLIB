package net.stumpner.upload.ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import net.stumpner.upload.Uploader;

public class UploadPopup extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField url;
    private JTextField username;
    private JTextField password;
    private JPanel panelUploadstate;
    private JPanel panelServerlogin;


    private JTextPane uploadLog;
    private Uploader uploaderClass;

    boolean finishState = false;
    private JProgressBar uploadProgress;
    private JTextField categoryName;
    private JCheckBox catUsePath;
    private JTextField folderName;
    private JCheckBox folderUsePath;

    public UploadPopup(Uploader uploaderClass) {

        //this.setAlwaysOnTop(true);
        this.uploaderClass = uploaderClass;
        setContentPane(contentPane);
        setTitle("SuSIDE Upload");
        setModal(true);
        //getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void log(String logEntry) {
        this.uploadLog.setText(logEntry +"\n"+this.uploadLog.getText());
        this.uploadLog.scrollRectToVisible(new Rectangle(0,0,10,10));
    }

    public void disableButtons() {
        this.buttonOK.setEnabled(false);
        this.buttonCancel.setEnabled(false);
    }

    public void setFinishState(boolean finishState) {
        if (finishState) buttonOK.setEnabled(true);
        this.finishState=finishState;
    }

    public JTextField getUrl() {
        return url;
    }

    public void setUrl(JTextField url) {
        this.url = url;
    }

    public JTextField getUsername() {
        return username;
    }

    public void setUsername(JTextField username) {
        this.username = username;
    }

    public JTextField getPassword() {
        return password;
    }

    public void setPassword(JTextField password) {
        this.password = password;
    }

    public JPanel getPanelUploadstate() {
        return panelUploadstate;
    }

    public void setPanelUploadstate(JPanel panelUploadstate) {
        this.panelUploadstate = panelUploadstate;
    }

    public JPanel getPanelServerlogin() {
        return panelServerlogin;
    }

    public void setPanelServerlogin(JPanel panelServerlogin) {
        this.panelServerlogin = panelServerlogin;
    }

    private void onOK() {
// add your code here

        if (!finishState) {
            //System.out.println("Before OK pressed!");
            uploaderClass.doUpload(this,uploadProgress);
            //System.out.println("after OK pressed!");
        } else {
            dispose();
        }
        //dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public JCheckBox getFolderUsePath() {
        return folderUsePath;
    }

    public void setFolderUsePath(JCheckBox folderUsePath) {
        this.folderUsePath = folderUsePath;
    }

    public JTextField getFolderName() {
        return folderName;
    }

    public void setFolderName(JTextField folderName) {
        this.folderName = folderName;
    }

    public JCheckBox getCatUsePath() {
        return catUsePath;
    }

    public void setCatUsePath(JCheckBox catUsePath) {
        this.catUsePath = catUsePath;
    }

    public JTextField getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(JTextField categoryName) {
        this.categoryName = categoryName;
    }
}
