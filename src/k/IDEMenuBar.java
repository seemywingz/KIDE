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

    private Action open, quit, save, saveAs;
    private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
    private IDEPanel idePanel;

    IDEMenuBar(IDEPanel idePanel){
        this.idePanel = idePanel;
        createActions();
        mkOptions();

        idePanel.add(this);
    }//..

    protected void createActions(){
       open = new AbstractAction("Open", new ImageIcon("open.gif")) {
            public void actionPerformed(ActionEvent e) {
                comfirmSave();
                if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
                    readInFile(dialog.getSelectedFile().getAbsolutePath());
                }
                idePanel.editor.open();
            }
        };

        quit = new AbstractAction("Exit") {
            public void actionPerformed(ActionEvent e) {
                comfirmSave();
                System.exit(0);
            }
        };

        saveAs = new AbstractAction("Save As...") {
            public void actionPerformed(ActionEvent e) {
                saveFileAs();
            }
        };

        save = new AbstractAction("Save", new ImageIcon("save.gif")) {
            public void actionPerformed(ActionEvent e) {
                if(!idePanel.editor.getCurrentFile().equals("Untitled"))
                    saveFile(idePanel.editor.getCurrentFile());
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
                idePanel.editor.getTextArea().write(w);
                w.close();
                idePanel.editor.setCurrentFile(fileName);
                idePanel.ide.setTitle(idePanel.editor.getCurrentFile());
                idePanel.editor.setFileChanged(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }//..

    private void comfirmSave() {
        if(idePanel.editor.getFileChanged())
            if (JOptionPane.showConfirmDialog(this, "Would you like to save " + idePanel.editor.getCurrentFile() + " ?", "save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                saveFile(idePanel.editor.getCurrentFile());

    }//..

    private void saveFileAs() {
        if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
            saveFile(dialog.getSelectedFile().getAbsolutePath());
    }//..

    private void readInFile(String fileName) {
        try {
            FileReader r = new FileReader(fileName);
            idePanel.editor.getTextArea().read(r, null);
            r.close();
            idePanel.editor.setCurrentFile(fileName);
            idePanel.ide.setTitle(idePanel.editor.getCurrentFile());
            idePanel.editor.setFileChanged(false);
        }
        catch(IOException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);
        }
    }//..

}// IDEMenuBar
