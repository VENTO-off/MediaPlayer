package relevant_craft.vento.media_player.gui.main.elements.navigation;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import relevant_craft.vento.media_player.manager.font.FontManager;
import relevant_craft.vento.media_player.manager.font.Fonts;
import relevant_craft.vento.media_player.manager.picture.PictureManager;

public class NavigationListElement extends Pane {
    private static final DataFormat ITEM_LIST = new DataFormat("navigation/item");
    private static final CornerRadii CORNER_RADII = new CornerRadii(3.0, 0, 0, 3.0, false);
    private static int lastID = 0;

    private final int id;
    private final NavigationList list;
    private final NavigationItem data;
    private final ImageView image;
    private final Text text;

    private boolean isSelected;
    private long lastUpdate;

    /**
     * Init navigation list element
     */
    public NavigationListElement(NavigationList list, NavigationItem data) {
        super();

        this.id = lastID++;
        this.list = list;
        this.data = data;
        this.image = new ImageView();
        this.text = new Text();
        this.isSelected = false;
        this.lastUpdate = System.currentTimeMillis();

        this.initStyle();

        this.setOnDragDetected(this::onDragDetected);
        this.setOnDragOver(this::onDragOver);
        this.setOnDragDropped(this::onDragDropped);
        this.setOnDragDone(this::onDragDone);
        this.setOnMouseClicked(this::onClick);
    }

    /**
     * Init style
     */
    private void initStyle() {
        //init layout
        this.setPrefWidth(154);
        this.setPrefHeight(35);
        this.setPadding(new Insets(2.0, 0, 2.0, 0));
        this.setCursor(Cursor.HAND);
        this.setSelected(isSelected);

        if (isBlank()) {
            return;
        }

        //render image
        this.image.setImage(PictureManager.loadImage(data.getIcon().getIconName()));
        this.image.setLayoutX(8);
        this.image.setLayoutY(this.getPrefHeight() / 2 - this.image.getImage().getHeight() / 2);
        this.getChildren().add(this.image);

        //render text
        final double fixY = 11;
        this.text.setText(data.getDisplayName());
        this.text.setFill(Color.WHITE);
        this.text.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 17));
        this.text.setLayoutX(30);
        this.text.setLayoutY(fixY + 12);
        this.text.setDisable(true);
        this.getChildren().add(this.text);
    }

    /**
     * Calculate current index
     */
    private int getIndex(DragEvent e) {
        int index = 0;

        NavigationListElement target = (NavigationListElement) e.getTarget();
        if (target.isBlank() && e.getY() < this.getPrefHeight()) {
            return list.getLastDragId();
        }

        for (Node node : list.getChildren()) {
            if (node instanceof NavigationListElement) {
                NavigationListElement element = (NavigationListElement) node;
                if (element.equals(target)) {
                    break;
                }
            }
            index++;
        }

        if (e.getY() > this.getPrefHeight()) {
            index++;
        }

        if (index >= list.getSize()) {
            index = list.getSize() - 1;
        }

        return index;
    }

    private void setBlank(int index) {
        if (index == -1) {
            return;
        }
        if (list.getLastDragId() == index) {
            return;
        }

        //clear all blank elements
        clearBlank();

        //add new blank element
        Platform.runLater(() -> list.getChildren().add(index, new NavigationListElement(list, null)));

        list.setLastDragId(index);
    }

    private void clearBlank() {
        for (Node node : list.getChildren()) {
            if (node instanceof NavigationListElement) {
                NavigationListElement element = (NavigationListElement) node;
                if (element.isBlank()) {
                    Platform.runLater(() -> list.getChildren().remove(node));
                }
            }
        }
    }

    /**
     * Event on drag detected
     */
    private void onDragDetected(MouseEvent e) {
        Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.put(ITEM_LIST, data);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        dragboard.setDragView(this.snapshot(params, null));
        dragboard.setContent(content);

        list.getChildren().remove(this);
    }

    /**
     * Event on drag over
     */
    private void onDragOver(DragEvent e) {
        e.acceptTransferModes(TransferMode.MOVE);

        if (System.currentTimeMillis() - lastUpdate < 250) {
            return;
        }
        lastUpdate = System.currentTimeMillis();

        setBlank(getIndex(e));
    }

    /**
     * Event on drag dropped
     */
    private void onDragDropped(DragEvent e) {
        Dragboard dragboard = e.getDragboard();
        boolean isSuccess = false;

        if (dragboard.hasContent(ITEM_LIST)) {
            clearBlank();
            list.setOrder((NavigationItem) dragboard.getContent(ITEM_LIST));
            isSuccess = true;
        }

        e.setDropCompleted(isSuccess);
    }

    /**
     * Event on drag done
     */
    private void onDragDone(DragEvent e) {
        Dragboard dragboard = e.getDragboard();

        if (e.getAcceptedTransferMode() == null) {
            if (dragboard.hasContent(ITEM_LIST)) {
                clearBlank();
                list.setOrder((NavigationItem) dragboard.getContent(ITEM_LIST));
            }
        }
    }

    /**
     * Event on mouse click
     */
    private void onClick(MouseEvent e) {
        list.onClick();
        setSelected(!isSelected);
    }

    /**
     * Get item data
     */
    public NavigationItem getData() {
        return data;
    }

    /**
     * Check is item blank
     */
    private boolean isBlank() {
        return getData() == null;
    }

    /**
     * Check if element selected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Set element selected
     */
    public void setSelected(boolean selected) {
        isSelected = selected;

        //TODO pass average color
        final String averageColor = "#6b80c1";

        if (isSelected) {
            LinearGradient gradient = new LinearGradient(
                    0,
                    0,
                    1,
                    0,
                    true,
                    CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web(averageColor)),
                    new Stop(1, Color.TRANSPARENT)
            );
            this.setBackground(new Background(new BackgroundFill(gradient, CORNER_RADII, this.getPadding())));
        } else {
            this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CORNER_RADII, this.getPadding())));
        }
    }

    /**
     * Compare two classes
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NavigationListElement element = (NavigationListElement) o;
        return id == element.id;
    }
}
