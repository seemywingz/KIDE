package k;

import javax.swing.*;
import java.awt.*;


interface Logic {
    public abstract void apply() throws Exception;
}// end interface Logic

public final class Utils {

    static GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    static int ScreenWidth = graphicsDevice.getDisplayMode().getWidth();
    static int ScreenHeight = graphicsDevice.getDisplayMode().getHeight();


    static public boolean isInt(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static ImageIcon scaleImageIcon(ImageIcon icon, int w, int h){
        Image img = icon.getImage() ;
        return new ImageIcon(  img.getScaledInstance( w, h,  Image.SCALE_SMOOTH )  );
    }//..

    public static void startThread(final Logic logic){
        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        logic.apply();
                    }catch (Exception e){e.printStackTrace();}
            }
        }).start();
    }//..

    public static void startThreadLoop(final Logic logic, final int waitTime){
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

    public static void wait(int mils){
        try {
            Thread.sleep(mils);
        }catch (Exception e){}
    }//..

}// end Class wrld.Utils
