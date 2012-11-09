package vista;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mihai.panaitescu
 * Date: Dec 7, 2007
 * Time: 12:11:05 PM
 */
public class VistaDialogContent {

    private List<VistaButton> buttons;
    private String text;
    private String description;

    public VistaDialogContent(List<VistaButton> buttons, String text) {
        this(buttons, text, null);
    }

    public VistaDialogContent(List<VistaButton> buttons, String text, String description) {
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null!");
        }
        this.buttons = buttons;
        this.text = text;
        this.description = description;
    }

    public List<VistaButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<VistaButton> buttons) {
        this.buttons = buttons;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
