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

        initTextArea("KIDE: Error Pane...",
                100,w,false,
                super.mkKeyAdapter(keyBuffer,actionMap));

        initScrollPane(new Rectangle(2, idePanel.editor.getScrollPane().getHeight() + 5, w - 10, h = h - (idePanel.editor.getHeight() + 55)));

        Utils.startThreadLoop(new Logic() {
            @Override
            public void apply() throws Exception {
                textArea.setText(title);
                textArea.append(idePanel.lex.getErrorMsg());
            }
        }, 20);
    }//..

}// ErrorPane
