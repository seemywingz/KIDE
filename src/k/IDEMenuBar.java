package k;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Kevin Jayne on 1/23/15.
 *
 * IDE menubar
 *
 */
public class IDEMenuBar extends JMenuBar{

    private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
    private String currentFile = "Untitled";
    private Action Open,Quit;
    private IDEPanel idePanel;

    IDEMenuBar(IDEPanel idePanel){
        this.idePanel = idePanel;
        createActions();
        mkOptions();


    }

    protected void createActions(){
      Open  = new AbstractAction("Open", new ImageIcon("open.gif")) {
            public void actionPerformed(ActionEvent e) {
//                saveOld();
                if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
                    readInFile(dialog.getSelectedFile().getAbsolutePath());
                }
//                SaveAs.setEnabled(true);
            }
        };

        Quit = new AbstractAction("Quit") {
            public void actionPerformed(ActionEvent e) {
//                saveOld();
                System.exit(0);
            }
        };
    }//..

    protected void mkOptions(){

        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");

        add(file); add(edit);

        file.add(Open);

//        file.add(New);
//        file.add(Save);
        file.add(Quit);
//        file.add(SaveAs);

        file.addSeparator();

//        for(int i=0; i<4; i++)
//            file.getItem(i).setIcon(null);
    }//..

    private void readInFile(String fileName) {
        try {
            FileReader r = new FileReader(fileName);
            idePanel.editor.editor.read(r, null);
            r.close();
            currentFile = fileName;
            idePanel.ide.setTitle(currentFile);
//            changed = false;
        }
        catch(IOException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);
        }
    }


}// IDEMenuBar
