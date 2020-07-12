package relevant_craft.vento.media_player.gui.main.elements.control;

import javafx.scene.input.MouseEvent;
import relevant_craft.vento.media_player.manager.picture.PictureManager;
import relevant_craft.vento.media_player.manager.picture.Pictures;

public class ChangeableControlButton extends ControlButton {
    private final Pictures icon_default;
    private final Pictures icon_click;

    private boolean isSelected;

    /**
     * Init button for control bar
     */
    public ChangeableControlButton(double positionX, double positionY, Pictures icon1, Pictures icon2) {
        super(positionX, positionY, icon1);
        this.icon_default = icon1;
        this.icon_click = icon2;
        this.isSelected = false;
    }

    /**
     * Change icon on click
     */
    protected void onClick(MouseEvent e) {
        this.setValue(!this.isSelected);
        super.onClick(e);
    }

    /**
     * Set selected
     */
    public void setSelected(boolean isSelected) {
        this.setValue(isSelected);
    }

    /**
     * Set button value
     */
    private void setValue(boolean isSelected) {
        if (isSelected) {
            image.setImage(PictureManager.loadImage(icon_click.getIconName()));
        } else {
            image.setImage(PictureManager.loadImage(icon_default.getIconName()));
        }

        this.isSelected = isSelected;
    }

    /**
     * Get selected value
     */
    public boolean isSelected() {
        return isSelected;
    }
}
