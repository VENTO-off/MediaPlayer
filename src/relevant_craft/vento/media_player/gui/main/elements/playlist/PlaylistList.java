package relevant_craft.vento.media_player.gui.main.elements.playlist;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PlaylistList extends VBox {
    private int size;
    private int lastDragID;

    /**
     * Init music list
     */
    public PlaylistList() {
        super();
        this.size = 0;
        this.lastDragID = -1;

        this.setPadding(new Insets(0, 6.0, 0, 6.0));
    }

    /**
     * Add element to the end of list
     */
    public void addElement(PlaylistItem data) {
        this.getChildren().add(new PlaylistElement(this, data));
        size++;
    }

    /**
     * Set order of element
     */
    public void setOrder(PlaylistItem data, boolean isSelected) {
        Platform.runLater(() -> {
            this.getChildren().add(lastDragID, new PlaylistElement(this, data, isSelected));
            this.calculateOrderNumbers();
            this.lastDragID = -1;
        });
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
    public void onClick() {
        for (Node node : this.getChildren()) {
            if (node instanceof PlaylistElement) {
                PlaylistElement element = (PlaylistElement) node;
                element.setSelected(false);
            }
        }
    }

    /**
     * Calculate order numbers
     */
    public void calculateOrderNumbers() {
        int index = 1;
        for (Node node : this.getChildren()) {
            if (node instanceof PlaylistElement) {
                PlaylistElement element = (PlaylistElement) node;
                element.renderOrderNumber(index++);
            }
        }
    }
}
