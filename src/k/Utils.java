package k;

import sun.audio.AudioPlayer;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


interface Logic {
    public abstract void apply() throws Exception;
}// end interface Logic

public final class Utils {

    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
    // stringName.split(String.format(Utils.WITH_DELIMITER,";"));
    static GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

    static public boolean isInt(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    };


    public static Clip mkClip(String soundFile){
        Clip clip = null;
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(Utils.class.getResource(soundFile));
            DataLine.Info lineInfo = new DataLine.Info(Clip.class, ais.getFormat());
            clip = (Clip) AudioSystem.getLine(lineInfo);
            clip.open(ais);
        }catch (Exception e){
            e.printStackTrace();
        }
        return clip;
        //clip.loop(2);
        //Clip theme = AudioSystem.getClip();
    }//..

    public static void setClipVolume(Clip clip,double gain){
        float db = (float) (Math.log(gain)/Math.log(10.0)*20.0);
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(db); // Reduce by 'volume' decibels.
    }//..

    public static JLabel mkGraphic(Class c,String image,int x,int y,int w, int h){
        try {
            ImageIcon img;
            img = new ImageIcon(ImageIO.read(c.getResourceAsStream(image)));
            img = Utils.scaleImageIcon(img, w, h);
            JLabel graphic = new JLabel(img);
            graphic.setBounds(x, y, w, h);
            return graphic;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }//..

    public static ImageIcon scaleImageIcon(ImageIcon icon, int w, int h){
        Image img = icon.getImage() ;
        return new ImageIcon(  img.getScaledInstance( w, h,  Image.SCALE_SMOOTH )  );
    }//..

    protected static void startThread(final Logic logic){
        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        logic.apply();
                    }catch (Exception e){e.printStackTrace();}
            }
        }).start();
    }//..

    protected static void startThreadLoop(final Logic logic, final int waitTime){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        logic.apply();
                        Thread.sleep(waitTime);
                    }catch (Exception e){e.printStackTrace();}
                }
            }
        }).start();
    }//..

    protected static FloatBuffer mkFloatBuffer(float vertices[]){
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());    // use the device hardware's native byte order
        FloatBuffer fb = vbb.asFloatBuffer();  // create a floating point buffer from the ByteBuffer
        fb.put(vertices);    // add the coordinates to the FloatBuffer
        fb.position(0);      // set the buffer to read the first coordinate
        return fb;
    }//..

    public static void wait(int mils){
        try {
            Thread.sleep(mils);
        }catch (Exception e){}
    }//..

    public static void playSound(String path){
        AudioPlayer.player.start(Utils.class.getResourceAsStream(path));
    }//..

    protected static float random(int max){
        double rand =  Math.random()*max;
        if((int)(Math.random()*100) < 50)
            rand = -rand;
        return (float) rand;
    }//..

}// end Class wrld.Utils
