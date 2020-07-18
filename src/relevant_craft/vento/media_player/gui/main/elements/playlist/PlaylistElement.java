package relevant_craft.vento.media_player.gui.main.elements.playlist;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
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
import javafx.scene.text.TextAlignment;
import relevant_craft.vento.media_player.manager.font.FontManager;
import relevant_craft.vento.media_player.manager.font.Fonts;
import relevant_craft.vento.media_player.utils.SizeUtils;
import relevant_craft.vento.media_player.utils.TextUtils;
import relevant_craft.vento.media_player.utils.TimeUtils;

import java.text.DecimalFormat;

public class PlaylistElement extends Pane {
    private static final DataFormat ITEM_LIST = new DataFormat("playlist/item");
    private static final CornerRadii CORNER_RADII = new CornerRadii(3.0, 0, 0, 3.0, false);
    private static final FontLoader FONT_LOADER = Toolkit.getToolkit().getFontLoader();
    private static final DecimalFormat FORMAT = new DecimalFormat("00");
    private static final double TEXT_WIDTH = 500;
    private static final double HEIGHT = 38;
    private static int lastID = 0;

    private final int id;
    private final PlaylistList list;
    private final PlaylistItem data;
    private final Text text;
    private final Text subText;
    private final Text time;

    private boolean isSelected;
    private long lastUpdate;
    private int orderNumber;

    /**
     * Init playlist element
     */
    public PlaylistElement(PlaylistList list, PlaylistItem data) {
        this(list, data, false);
    }

    public PlaylistElement(PlaylistList list, PlaylistItem data, boolean isSelected) {
        super();

        this.id = lastID++;
        this.list = list;
        this.data = data;
        this.text = new Text();
        this.subText = new Text();
        this.time = new Text();
        this.isSelected = isSelected;
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
        this.setPrefHeight(HEIGHT);
        this.setCursor(Cursor.HAND);
        this.setSelected(isSelected);

        if (isBlank()) {
            return;
        }

        //render song name
        final double fixY = 11;
        TextUtils.setWidthText(this.text, data.getDisplayName(), TEXT_WIDTH);
        this.text.setFill(Color.WHITE);
        this.text.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 15));
        this.text.setLayoutX(10);
        this.text.setLayoutY(fixY + 7);
        this.text.setWrappingWidth(500);
        this.text.setDisable(true);
        this.getChildren().add(this.text);

        //render song info
        this.subText.setText(String.join(" :: ", data.getAudioFormat(), String.join(", ", data.getSampleRate() / 1000 + " kHz", + data.getBitRate() / 1000 + " kbps", SizeUtils.formatSize(data.getSize()))));
        this.subText.setFill(Color.web(Color.WHITE.toString(), 0.25));
        this.subText.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 11));
        this.subText.setLayoutX(this.text.getLayoutX());
        this.subText.setLayoutY(fixY + 22);
        this.subText.setDisable(true);
        this.getChildren().add(this.subText);

        //render song time
        this.time.setText(TimeUtils.formatPlaylistTime(data.getLength()));
        this.time.setWrappingWidth(60);
        this.time.setTextAlignment(TextAlignment.RIGHT);
        this.time.setFill(Color.WHITE);
        this.time.setFont(FontManager.loadFont(Fonts.SEGOE_UI.getFontName(), 14));
        this.time.setLayoutX(543);
        this.time.setLayoutY(this.text.getLayoutY());
        this.time.setDisable(true);
        this.getChildren().add(this.time);
    }

    /**
     * Calculate current index
     */
    private int getIndex(DragEvent e) {
        int index = 0;

        PlaylistElement target = (PlaylistElement) e.getTarget();
        if (target.isBlank() && e.getY() < this.getPrefHeight()) {
            return list.getLastDragId();
        }

        for (Node node : list.getChildren()) {
            if (node instanceof PlaylistElement) {
                PlaylistElement element = (PlaylistElement) node;
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
            //if add files => add new blank element
            index = list.getSize() - (e.getDragboard().hasFiles() ? 0 : 1);
        }

        return index;
    }

    /**
     * Set blank element at position
     */
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
        Platform.runLater(() -> list.getChildren().add(index, new PlaylistElement(list, null)));

        list.setLastDragId(index);
    }

    /**
     * Remove all blank elements
     */
    private void clearBlank() {
        for (Node node : list.getChildren()) {
            if (node instanceof PlaylistElement) {
                PlaylistElement element = (PlaylistElement) node;
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
        //TODO if element alone

        Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.put(ITEM_LIST, data);
        content.putString(String.valueOf(isSelected));

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        dragboard.setDragView(this.snapshot(params, null));
        dragboard.setContent(content);

        Platform.runLater(() -> list.getChildren().remove(this));
    }

    /**
     * Event on drag over
     */
    private void onDragOver(DragEvent e) {
        Dragboard dragboard = e.getDragboard();
        if (!dragboard.hasContent(ITEM_LIST) && !dragboard.hasFiles()) {
            e.acceptTransferModes(TransferMode.NONE);
            return;
        }

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
            list.setOrder((PlaylistItem) dragboard.getContent(ITEM_LIST), Boolean.parseBoolean(dragboard.getString()));
            isSuccess = true;
        } else if (dragboard.hasFiles()) {
            clearBlank();
            list.onDropFiles(dragboard.getFiles(), getIndex(e));
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
                list.setOrder((PlaylistItem) dragboard.getContent(ITEM_LIST), Boolean.parseBoolean(dragboard.getString()));
            }
        }
    }

    /**
     * Event on mouse click
     */
    private void onClick(MouseEvent e) {
        list.onClick(data);
        setSelected(!isSelected);
    }

    /**
     * Render order numbers
     */
    public void renderOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
        final String number = FORMAT.format(orderNumber);
        TextUtils.setWidthText(this.text, number + ". " + data.getDisplayName(), TEXT_WIDTH);
        this.subText.setLayoutX(this.text.getLayoutX() + FONT_LOADER.computeStringWidth(number, this.text.getFont()) + 7);
    }

    /**
     * Get item data
     */
    public PlaylistItem getData() {
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
     * Get height if element
     */
    public static double getElementHeight() {
        return HEIGHT;
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
                    new Stop(0, Color.web(averageColor, 0.5)),
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
        PlaylistElement element = (PlaylistElement) o;
        return id == element.id;
    }
}
