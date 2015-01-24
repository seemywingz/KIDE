package k;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Kevin Jayne on 1/23/15.
 *
 * IDE menubar
 *
 */
public class IDEMenuBar extends JMenuBar{

    private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
    public String currentFile = "Untitled";
    public Action open, quit, save, saveAs;
    private IDEPanel idePanel;
    public boolean changed;


    IDEMenuBar(IDEPanel idePanel){
        this.idePanel = idePanel;
        createActions();
        mkOptions();
    }//..



    protected void createActions(){
      open = new AbstractAction("open", new ImageIcon("open.gif")) {
            public void actionPerformed(ActionEvent e) {
                saveOld();
                if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
                    readInFile(dialog.getSelectedFile().getAbsolutePath());
                }
                idePanel.editor.open();
            }
        };

        quit = new AbstractAction("quit") {
            public void actionPerformed(ActionEvent e) {
                saveOld();
                System.exit(0);
            }
        };

        saveAs = new AbstractAction("save as...") {
            public void actionPerformed(ActionEvent e) {
                saveFileAs();
            }
        };

        save = new AbstractAction("save", new ImageIcon("save.gif")) {
            public void actionPerformed(ActionEvent e) {
                if(!currentFile.equals("Untitled"))
                    saveFile(currentFile);
                else
                    saveFileAs();
            }
        };
    }//..

    protected void mkOptions(){

        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");

        add(file); add(edit);

        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(quit);
    }//..

    public void saveFile(String fileName) {
            try {
                FileWriter w = new FileWriter(fileName);
                idePanel.editor.textArea.write(w);
                w.close();
                currentFile = fileName;
                idePanel.ide.setTitle(currentFile);
                changed = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
    }//..

    private void saveOld() {
        if(changed)
        if(JOptionPane.showConfirmDialog(this, "Would you like to save "+ currentFile +" ?","save",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
            saveFile(currentFile);
    }//..

    private void saveFileAs() {
        if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
            saveFile(dialog.getSelectedFile().getAbsolutePath());
    }//..

    private void readInFile(String fileName) {
        try {
            FileReader r = new FileReader(fileName);
            idePanel.editor.textArea.read(r, null);
            r.close();
            currentFile = fileName;
            idePanel.ide.setTitle(currentFile);
            changed = false;
        }
        catch(IOException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);
        }
    }//..

}// IDEMenuBar
