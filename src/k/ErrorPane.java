package k;

import java.awt.*;

/**
 * Created by kevin on 1/27/15.
 *
 * Lex and Parse errors show here in a more readable form
 *
 *
 */
public class ErrorPane extends ScrollableOutput{

    ErrorPane(final IDEPanel idePanel) {
        super(idePanel);
        w = Utils.ScreenWidth;
        h = Utils.ScreenHeight;
        frame.setTitle("KIDE: Error Pane");
        frame.setSize(idePanel.ide.getWidth(),200);


        initTextArea("KIDE: Error Pane...",
                20, w / 11, false,
                super.mkKeyAdapter(keyBuffer, actionMap));

        initScrollPane(new Rectangle(2, idePanel.editor.getScrollPane().getHeight(), w - 10,(h/2)-140 ));

        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {
                if(idePanel.editor.getFileChanged()) {
                    textArea.setText(title);
                    textArea.append(idePanel.lex.getLexErrors());
                    textArea.append(idePanel.parser.getParseErrors());
                }
            }
        }, 20);
    }//..

}// ErrorPane
