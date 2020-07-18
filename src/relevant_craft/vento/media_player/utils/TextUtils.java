package relevant_craft.vento.media_player.utils;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.text.Text;

public class TextUtils {
    private static final FontLoader FONT_LOADER = Toolkit.getToolkit().getFontLoader();

    /**
     * Set text according to width
     */
    public static void setWidthText(Text textArea, String value, double maxWidth) {
        double textWidth = FONT_LOADER.computeStringWidth(value, textArea.getFont());

        while (textWidth > maxWidth) {
            value = value.substring(0, value.length() - 4) + "...";
            textWidth = FONT_LOADER.computeStringWidth(value, textArea.getFont());
        }

        textArea.setText(value);
    }
}
