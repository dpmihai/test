package vista;

import vista.action.CancelAction;
import vista.action.NoteAction;
import vista.action.SaveAction;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Dec 7, 2007
 * Time: 2:49:52 PM
 */
public class Test {

    public static void main(String[] args) {

//        VistaButton buttonSave = new VistaButton(new ImageIcon(VistaButton.class.getResource("save.png")),
//                "Save", "(Recommended)");
//        VistaButton button = new VistaButton(new ImageIcon(VistaButton.class.getResource("cancel.png")),
//                "Don't save", "Your changes will be lost");

        SaveAction saveAction = new SaveAction();
        VistaButton buttonSave = new VistaButton(saveAction, "(Recommended)");
        NoteAction noteAction = new NoteAction();
        VistaButton buttonMark = new VistaButton(noteAction, "A note will be saved as report usage");
        CancelAction cancelAction = new CancelAction();
        VistaButton button = new VistaButton(cancelAction, "Your changes will be lost");

        List<VistaButton> list = new ArrayList<VistaButton>();
        list.add(buttonSave);
        list.add(buttonMark);
        list.add(button);


        JFrame frame = new JFrame("Windows Vista");
        frame.setIconImage(new ImageIcon(VistaButton.class.getResource("report.png")).getImage());


        VistaDialogContent content = new VistaDialogContent(list, "Do you want to save the report?",
                "You have changed the report. If you want to keep <br>changes you should save it.");
        VistaDialog dialog = new VistaDialog(content, frame);        
        dialog.selectButton(buttonSave);

        dialog.setDispose(false);
        dialog.setEscapeOption(true);

        saveAction.setDialog(dialog);
        noteAction.setDialog(dialog);

        dialog.pack();
        dialog.setVisible(true);
    }
    
}
