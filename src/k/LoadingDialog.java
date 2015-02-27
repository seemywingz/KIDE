package k;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by KevAdmin on 2/27/2015.
 */
public class LoadingDialog extends JDialog {


    ArrayList<ImageIcon> spinnerImgs = new ArrayList<ImageIcon>();
    JLabel spinner;
    int w=600,h=600,imgNum =0;



    LoadingDialog(){
        setLayout(null);
        setLocation(550,300);
//        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setUndecorated(true);
        add(new JLabel("Loading"));
        setSize(w,h);
        setBackground(new Color(0,0,0,0));
        addLoadingSprite();
        setVisible(true);

        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {
//                System.out.println(imgNum);
                spinner.setIcon(spinnerImgs.get(imgNum));
                imgNum++;

                imgNum = imgNum < (spinnerImgs.size()-1)?imgNum:0;

            }
        },50);
    }//..


    protected void addLoadingSprite(){
        BufferedImage i;
        ImageIcon img;

        int imgWidth = 128;
        int imgHeight = 128;
        int sx = 0;
        int sy = 0;

        try {

            img = new ImageIcon(ImageIO.read(getClass().getResource("/k/img/ide.png")));
            img = Utils.scaleImageIcon(img,w,h);


            JLabel icon = new JLabel(img);
            icon.setBounds(0,0,w,h);


            i = ImageIO.read(getClass().getResourceAsStream("/k/img/sprites.png"));

            for(int x=0;x<2304;x+=128) {
                img = new ImageIcon(i.getSubimage(x, sy, imgWidth, imgHeight));
                img = Utils.scaleImageIcon(img, imgWidth, imgHeight);

                spinnerImgs.add(img);
            }

            spinner = new JLabel(spinnerImgs.get(0));
            spinner.setOpaque(false);
            spinner.setBackground(new Color(0, 0, 0, 0));
            spinner.setBounds(340, 120, imgWidth, imgHeight);
            add(spinner);
            add(icon);

//            JOptionPane.showMessageDialog(null,spinnerImgs.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//..

}//..
