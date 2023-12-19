package ContactsMS;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class MyFunc {

    String imgPath;
    public ImageIcon resizePic(String picPath, byte[] BLOBpic, int wdth, int hgth) {
        ImageIcon myImg;
        if (picPath != null){
            myImg = new ImageIcon(picPath);
        }else {
            myImg = new ImageIcon(BLOBpic);
        }

        Image img = myImg.getImage().getScaledInstance(wdth, hgth, Image.SCALE_SMOOTH);
        ImageIcon myPicture = new ImageIcon(img);

        return myPicture;
    }

    public String browseImage(JLabel lbl){
        String path = "";
        JFileChooser filec = new JFileChooser();
        filec.setCurrentDirectory(new File(System.getProperty("user.home")));
        // file extension
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.images", "jpg", "png", "gif");
        filec.addChoosableFileFilter(filter);

        int fileState = filec.showOpenDialog(null);
        // if the user select a file
        if (fileState == JFileChooser.APPROVE_OPTION) {
            File selectedFile = filec.getSelectedFile();
            path = selectedFile.getAbsolutePath();
            // imagePath = path;

            lbl.setIcon(resizePic(path, null, lbl.getWidth(), lbl.getHeight()));

        } else if (fileState == JFileChooser.CANCEL_OPTION) {// if the user cancel
            System.out.println("No Image Selected");
        }

        return path;
    }
}
