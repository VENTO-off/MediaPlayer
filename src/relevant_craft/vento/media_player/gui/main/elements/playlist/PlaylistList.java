package relevant_craft.vento.media_player.gui.main.elements.playlist;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import relevant_craft.vento.media_player.manager.color.ColorManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlaylistList extends VBox {
    private final ColorManager colorManager;

    private int size;
    private int lastDragID;
    private ClickListener clickListener;
    private DropFilesListener dropFilesListener;
    private ChangeOrderListener changeOrderListener;

    /**
     * Init music list
     */
    public PlaylistList() {
        super();
        this.colorManager = ColorManager.getInstance();

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
     * Add list of elements to the end of list (starts in thread)
     */
    public void addAllElements(List<PlaylistItem> data) {
        //render playlist elements
        List<PlaylistElement> rendered = new ArrayList<>();
        for (PlaylistItem item : data) {
            rendered.add(new PlaylistElement(this, item));
        }
        size = rendered.size();

        //add to playlist
        Platform.runLater(() -> this.getChildren().addAll(rendered));
    }

    /**
     * Set order of element
     */
    public void setOrder(PlaylistItem data, boolean isSelected) {
        //notify change order listener
        if (changeOrderListener != null) {
            changeOrderListener.onChangeOrder(data, lastDragID);
        }

        //set order
        Platform.runLater(() -> {
            this.getChildren().add(lastDragID, new PlaylistElement(this, data, isSelected));
            this.calculateOrderNumbers();
            this.lastDragID = -1;
        });
    }

    /**
     * Clear all elements
     */
    public void clear() {
        this.getChildren().clear();
        this.size = 0;
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

        return height + PlaylistElement.getElementHeight();
    }

    /**
     * Event on mouse click
     */
    protected void onClick(PlaylistItem data, int index) {
        //deselect all
        deselectAllElements();

        //notify click listener
        if (clickListener != null) {
            clickListener.onClick(data, index);
        }
    }

    /**
     * Event on drop files
     */
    protected void onDropFiles(List<File> files, int index) {
        if (dropFilesListener != null) {
            dropFilesListener.onDrop(files, index);
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

    /**
     * Select element
     */
    public int selectElement(PlaylistItem item) {
        if (item == null) {
            return 0;
        }

        //deselect all
        deselectAllElements();

        //get index of selected element
        int index = 1;
        for (Node node : this.getChildren()) {
            if (node instanceof PlaylistElement) {
                PlaylistElement element = (PlaylistElement) node;
                if (element.getData().getHash().equals(item.getHash())) {
                    element.setSelected(true);
                    return index;
                }

                index++;
            }
        }

        return 0;
    }

    /**
     * Deselect all elements
     */
    private void deselectAllElements() {
        for (Node node : this.getChildren()) {
            if (node instanceof PlaylistElement) {
                PlaylistElement element = (PlaylistElement) node;
                element.setSelected(false);
            }
        }
    }

    /**
     * Click listener
     */
    public interface ClickListener {
        void onClick(PlaylistItem data, int index);
    }

    /**
     * Add click listener
     */
    public void addClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * Drop files listener
     */
    public interface DropFilesListener {
        void onDrop(List<File> files, int index);
    }

    /**
     * Add drop files listener
     */
    public void addDropFilesListener(DropFilesListener dropFilesListener) {
        this.dropFilesListener = dropFilesListener;
    }

    /**
     * Change order listener
     */
    public interface ChangeOrderListener {
        void onChangeOrder(PlaylistItem data, int index);
    }

    /**
     * Add change order listener
     */
    public void addChangeOrderListener(ChangeOrderListener changeOrderListener) {
        this.changeOrderListener = changeOrderListener;
    }

    /**
     * Return color manager
     */
    public ColorManager getColorManager() {
        return colorManager;
    }
}
