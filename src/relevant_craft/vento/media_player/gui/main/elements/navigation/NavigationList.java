package relevant_craft.vento.media_player.gui.main.elements.navigation;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import relevant_craft.vento.media_player.manager.color.ColorManager;
import relevant_craft.vento.media_player.manager.font.FontManager;
import relevant_craft.vento.media_player.manager.font.Fonts;

public class NavigationList extends VBox {
    private int size;
    private int lastDragID;
    private ClickListener clickListener;
    private ColorManager colorManager;

    /**
     * Init navigation list
     */
    public NavigationList(String header) {
        super();
        this.size = 0;
        this.lastDragID = -1;
        this.colorManager = ColorManager.getInstance();

        this.setPadding(new Insets(0, 0, 0, 10.0));
        this.setHeader(header);
    }

    /**
     * Add element to the end of list
     */
    public void addElement(NavigationItem data) {
        this.getChildren().add(new NavigationListElement(this, data));
        size++;
    }

    /**
     * Clear all elements
     */
    public void clearElements() {
        this.getChildren().remove(1, this.getChildren().size());
    }

    /**
     * Set order of element
     */
    public void setOrder(NavigationItem data, boolean isSelected) {
        this.getChildren().add(lastDragID, new NavigationListElement(this, data, isSelected));
        this.lastDragID = -1;
    }

    /**
     * Set header
     */
    private void setHeader(String header) {
        Pane head = new Pane();
        head.setPrefHeight(47);

        final double fixY = 11;
        Text text = new Text(header.toUpperCase());
        text.setFill(Color.WHITE);
        text.setOpacity(0.35);
        text.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 17));
        text.setLayoutY(fixY + 31);
        head.getChildren().add(text);

        this.getChildren().add(head);
        size++;
    }

    /**
     * Get size of list
     */
    public int getSize() {
        return size;
    }

    /**
     * Get last drag ID
     */
    public int getLastDragId() {
        return lastDragID;
    }

    /**
     * Set last drag ID
     */
    public void setLastDragId(int lastDragId) {
        this.lastDragID = lastDragId;
    }

    /**
     * Calculate height
     */
    public double calculateHeight() {
        double height = 0.0;
        for (Node node : this.getChildren()) {
            if (node instanceof Pane) {
                Pane element = (Pane) node;
                height += element.getPrefHeight();
            }
        }

        return height;
    }

    /**
     * Event on mouse click
     */
    public void onClick(NavigationItem data) {
        //deselect all
        for (Node node : this.getChildren()) {
            if (node instanceof NavigationListElement) {
                NavigationListElement element = (NavigationListElement) node;
                element.setSelected(false);
            }
        }

        //notify click listener
        if (clickListener != null) {
            clickListener.onClick(data);
        }
    }

    /**
     * Click listener
     */
    public interface ClickListener {
        void onClick(NavigationItem data);
    }

    /**
     * Add click listener
     */
    public void addClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * Return color manager
     */
    public ColorManager getColorManager() {
        return colorManager;
    }
}
