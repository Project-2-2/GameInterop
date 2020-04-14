package Group9.gui2;

import Group9.agent.container.AgentContainer;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.GameMap;
import Group9.map.dynamic.DynamicObject;
import Group9.map.dynamic.Pheromone;
import Group9.map.dynamic.Sound;
import Group9.map.objects.Window;
import Group9.map.objects.*;
import Group9.math.Vector2;
import Interop.Percept.Vision.FieldOfView;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class MainScene extends Scene {
    class Settings{
        public boolean showText=false;
        public double agentScale = 5;
        public void toggleText(){
            showText = !showText;
        }
        public void toggleAgentScale(){
            if(agentScale==5){
                agentScale=1;
            }else{
                agentScale=5;
            }
        }
    }
    private StackPane mainStack;
    private HBox mainHBox = new HBox();
    private StackPane menuBackground = new StackPane();
    private Label menuBackgroundLabel = new Label(">");
    private VBox menu = new VBox();
    private StackPane menuPane = new StackPane();
    private StackPane canvasPane = new StackPane();
    private Canvas canvas = new Canvas(200,200);
    private Canvas canvasAgents = new Canvas(200,200);
    private double mapScale = 1;
    private Settings settings = new Settings();
    private HBox quickSettings = new HBox();
    private HBox quickSettingsBar = new HBox();
    private Slider animationSpeedSlider = new Slider(0,120,15);
    private Slider slider = new Slider(0.0,1,1);
    private Label sliderInfo = new Label("0.5");
    private StackPane playContainer = new StackPane();
    private StackPane stopContainer = new StackPane();
    private Label play = new Label();
    private AnimationTimer playbackAnimationTimer = null;
    private Label stop = new Label();
    private HBox animationSettings = new HBox();
    private HBox maxSpeedSetting = new HBox();
    private Label maxSpeedLabel = new Label("Maximum Speed");
    private Label animationLabel = new Label("Speed");
    private Label animationSliderInfo = new Label("15");
    private HBox historyPane = new HBox();
    private Label historyLabel = new Label("Safe History");
    private CheckBox history = new CheckBox();
    private final GameMap map;
    private Gui gui;
    private FileChooser fileChooser = new FileChooser();
    private boolean hasHistory = false;
    private CheckBox maxSpeed = new CheckBox();

    private boolean ffmpegWindows;
    private boolean ffmpegLinux;
    private final boolean ffmpegInstalled = isFFMPEGInstalled();

    //Buttons
    private Label reloadMapButton = new Label("Scale Map");
    private Label descriptionButton = new Label("Toggle Description");
    private Label toggleZoomButton = new Label("Toggle Agent-Zoom");
    private Label loadMapButton = new Label("Load Map");
    private Label renderButton = new Label(String.format("Render Video%s", (ffmpegInstalled ? "" : " (ffmpeg unavailable)")));
    private Label reloadButton = new Label("Reload Game");
    private Label helpButton = new Label("Help");

    ///Agent
    private List<MapObject> elements;
    public MainScene(StackPane mainStack, GameMap map,Gui gui) {
        super(mainStack);
        this.gui = gui;
        this.mainStack = mainStack;
        this.map = map;
        elements = map.getObjects();
        build();
        scale(true);
        style();
        listener();
    }
    private void build(){
        menu.getChildren().addAll(loadMapButton, reloadButton, reloadMapButton, descriptionButton, toggleZoomButton,
                renderButton,animationSettings, historyPane,maxSpeedSetting,helpButton);
        historyPane.getChildren().addAll(historyLabel,history);
        menuPane.getChildren().add(menu);
        canvasPane.getChildren().add(canvas);
        canvasPane.getChildren().add(canvasAgents);
        canvasPane.getChildren().add(quickSettings);
        mainHBox.getChildren().add(menuBackground);
        mainHBox.getChildren().add(canvasPane);
        mainStack.getChildren().add(mainHBox);
        mainStack.getChildren().add(menuPane);
        menuBackground.getChildren().add(menuBackgroundLabel);
        playContainer.getChildren().add(play);
        stopContainer.getChildren().add(stop);
        quickSettingsBar.getChildren().addAll(playContainer,stopContainer,slider,sliderInfo);
        quickSettings.getChildren().add(quickSettingsBar);
        maxSpeedSetting.getChildren().addAll(maxSpeedLabel,maxSpeed);
        animationSettings.getChildren().addAll(animationLabel,animationSpeedSlider,animationSliderInfo);
    }
    private void scale(boolean first){
        double height = this.getHeight();
        double width = this.getWidth();
        if(first){
            height = GuiSettings.defaultHeight;
            width = GuiSettings.defaultWidth;
        }
        canvasPane.setMaxSize(width-GuiSettings.widthMenu,height);
        canvasPane.setMinSize(width-GuiSettings.widthMenu,height);
        menu.setMaxSize(GuiSettings.widthMenuFocus, height);
        menu.setMinSize(GuiSettings.widthMenuFocus, height);
        menuBackground.setPrefSize(GuiSettings.widthMenu, height);
        menuPane.setPrefSize(width,height);
        calcScale();
        canvas.setWidth(map.getGameSettings().getWidth()*mapScale);
        canvas.setHeight(map.getGameSettings().getHeight()*mapScale);
        canvasAgents.setWidth(map.getGameSettings().getWidth()*mapScale);
        canvasAgents.setHeight(map.getGameSettings().getHeight()*mapScale);
        quickSettingsBar.setMaxHeight(GuiSettings.quickSettingsBarHeight);
        quickSettingsBar.setMinHeight(GuiSettings.quickSettingsBarHeight);
        //Buttons
        reloadMapButton.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        reloadMapButton.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        descriptionButton.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        descriptionButton.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        toggleZoomButton.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        toggleZoomButton.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        loadMapButton.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        loadMapButton.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        renderButton.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        renderButton.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        reloadButton.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        reloadButton.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        helpButton.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        helpButton.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        renderButton.setDisable(!ffmpegInstalled);
        animationSettings.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        animationSettings.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        historyPane.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        historyPane.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        maxSpeedSetting.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        maxSpeedSetting.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        slider.setPrefWidth(800);
        quickSettingsBar.setMaxWidth(800);

    }
    private void style(){
        File file = new File("./src/main/java/Group9/gui2/style.css");
        this.getStylesheets().add(file.toURI().toString());
        menu.getStyleClass().add("bg-nav");
        menuBackground.getStyleClass().add("bg-nav");
        canvasPane.getStyleClass().add("bg-light");
        menuPane.setAlignment(Pos.CENTER_LEFT);
        canvasPane.setAlignment(Pos.CENTER);
        reloadMapButton.getStyleClass().add("nav-button");
        menu.setVisible(false);
        menuPane.setMouseTransparent(true);
        menuBackgroundLabel.getStyleClass().add("menu-background-label");
        descriptionButton.getStyleClass().add("nav-button");
        toggleZoomButton.getStyleClass().add("nav-button");
        loadMapButton.getStyleClass().add("nav-button");
        renderButton.getStyleClass().add("nav-button");
        reloadButton.getStyleClass().add("nav-button");
        helpButton.getStyleClass().add("nav-button");
        play.getStyleClass().add("play-button");
        stop.getStyleClass().add("stop-button");
        slider.getStyleClass().add("sDark");
        animationSpeedSlider.getStyleClass().add("sLight");
        animationSettings.getStyleClass().add("animation-slider-pane");
        historyPane.getStyleClass().add("animation-slider-pane");
        playContainer.getStyleClass().add("button-container");
        stopContainer.getStyleClass().add("button-container");
        animationLabel.getStyleClass().add("animation-label");
        historyLabel.getStyleClass().add("animation-label");
        animationSliderInfo.getStyleClass().add("animation-label");
        maxSpeedSetting.getStyleClass().add("animation-slider-pane");
        maxSpeedLabel.getStyleClass().add("animation-label");
        quickSettings.setAlignment(Pos.BOTTOM_CENTER);
        quickSettingsBar.setPadding(new Insets(10));
        quickSettingsBar.setSpacing(5);
        quickSettingsBar.setAlignment(Pos.CENTER);
        history.setSelected(true);
        play.setDisable(true);
        stop.setDisable(true);
    }
    private void listener(){
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> rescale();
        this.widthProperty().addListener(stageSizeListener);
        this.heightProperty().addListener(stageSizeListener);
        menuBackground.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            menu.setVisible(true);
            menuPane.setMouseTransparent(false);
        });
        menu.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            menu.setVisible(false);
            menuPane.setMouseTransparent(true);
        });
        this.setOnKeyPressed(event -> {
            if(event.getText().equalsIgnoreCase("+")){
                mapScale = mapScale*1.1;
                canvas.setWidth(map.getGameSettings().getWidth()*mapScale);
                canvas.setHeight(map.getGameSettings().getHeight()*mapScale);
                canvasAgents.setWidth(map.getGameSettings().getWidth()*mapScale);
                canvasAgents.setHeight(map.getGameSettings().getHeight()*mapScale);
                draw();
            }
            if(event.getText().equalsIgnoreCase("-")){
                mapScale = mapScale*0.9;
                canvas.setWidth(map.getGameSettings().getWidth()*mapScale);
                canvas.setHeight(map.getGameSettings().getHeight()*mapScale);
                canvasAgents.setWidth(map.getGameSettings().getWidth()*mapScale);
                canvasAgents.setHeight(map.getGameSettings().getHeight()*mapScale);
                draw();
            }
        });
        reloadMapButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            rescaleMap();
        } );
        descriptionButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            settings.toggleText();
            draw();
        } );
        toggleZoomButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            settings.toggleAgentScale();
            draw();
        } );
        loadMapButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            File file = fileChooser.showOpenDialog(gui.getPrimary());
            if(file != null){
                gui.setMapFile(file);
                gui.restartGame(history.isSelected());
                updateButtons();
            }
        } );
        renderButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            generateVideo();
        });
        reloadButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            play.setDisable(true);
            stop.setDisable(true);
            if(playbackAnimationTimer != null)
            {
                playbackAnimationTimer.stop();
                playbackAnimationTimer = null;
            }
            hasHistory = false;
            gui.restartGame(history.isSelected());
            updateButtons();
            if(maxSpeed.isSelected()){
                gui.getMainController().updateGameSpeed(-1);
            }else{
                gui.getMainController().updateGameSpeed((int) animationSpeedSlider.getValue());
            }
        });
        helpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            openHelp();
        });

        slider.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            event.consume();
            int shiftModifier = event.isShiftDown() ? 10 : 0;
            if(event.getCode() == KeyCode.RIGHT)
            {
                slider.setValue(Math.min((int) (slider.getValue() + 1 + shiftModifier), gui.getMainController().getHistoryIndex()));
            }
            else if(event.getCode() == KeyCode.LEFT)
            {
                slider.setValue(Math.max((int) (slider.getValue() - (1 + shiftModifier)), 0));
            }
        });
        slider.valueProperty().addListener((observableValue, number, t1) -> {
           if(hasHistory){
               int newVal = t1.intValue();
               gui.getMainController().getHistoryViewIndex().set(newVal);
           }
        });
        animationSpeedSlider.valueProperty().addListener((observableValue, number, t1) -> {
            int newVal = t1.intValue();
            animationSliderInfo.setText(String.valueOf(newVal));
            gui.getMainController().updateGameSpeed(newVal);
        });
        maxSpeed.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(t1){
                gui.getMainController().updateGameSpeed(-1);
            }else{
                gui.getMainController().updateGameSpeed((int) animationSpeedSlider.getValue());
            }
        });

        this.canvasPane.setOnMouseClicked(event -> {
            Vector2 scene = new Vector2(event.getSceneX(), event.getSceneY())
                    .sub(canvasAgents.getBoundsInParent().getMinX(), canvasAgents.getBoundsInParent().getMinY())
                    .mul(1D / mapScale);

            MainController.History entry = gui.getMainController().getCurrentHistory();
            Optional<IntruderContainer> intruder = entry.intruderContainers.stream()
                    .filter(e -> e.getPosition().distance(scene) < 15).findAny();
            Optional<GuardContainer> guard = entry.guardContainers.stream()
                    .filter(e -> e.getPosition().distance(scene) < 15).findAny();

            if(intruder.isPresent())
            {
                System.out.println(intruder);
            }
            else if(guard.isPresent())
            {
                System.out.println(guard);
            }
        });

        play.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(!play.isDisabled())
            {
                stop.setDisable(false);
                play.setDisable(true);
                this.playbackAnimationTimer = new AnimationTimer() {

                    private long lastFrame = System.nanoTime();
                    private double drawFrames = 0;

                    @Override
                    public void handle(long now) {

                        double delta = (now - lastFrame);

                        final double frameTime = 1E9 / animationSpeedSlider.getValue();

                        if(delta >= frameTime)
                        {
                            if(gui.getMainController().getHistoryViewIndex().get() < gui.getMainController().getHistoryIndex())
                            {
                                this.lastFrame = now;
                                drawFrames += (delta / frameTime);

                                final int frames = (int) drawFrames;
                                drawFrames -= frames;
                                slider.setValue(gui.getMainController().getHistoryViewIndex().get() + frames);
                            }
                            else
                            {
                                stop.setDisable(true);
                                play.setDisable(false);
                                this.stop();
                            }
                        }

                    }
                };
                this.playbackAnimationTimer.start();

            }
        });

        stop.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(play.isDisabled())
            {
                play.setDisable(false);
                stop.setDisable(true);
                this.playbackAnimationTimer.stop();
                this.playbackAnimationTimer = null;
            }
        });

    }
    public void updateButtons()
    {
        this.play.setDisable(!history.isSelected());
        this.stop.setDisable(!history.isSelected());
        this.slider.setDisable(!history.isSelected());
        this.renderButton.setDisable(!history.isSelected());
    }
    public void activateHistory(){
        hasHistory =true;
        int age = gui.getMainController().getHistoryIndex();
        slider.setMax(age);
        slider.setValue(age);
        slider.setMin(0);
        play.setDisable(false);
    }
    public void rescale(){
        scale(false);
        draw();
    }
    public void rescaleMap(){
        calcScale();
        scale(false);
        draw();
    }
    private void draw(){
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        g.setFill(GuiSettings.backgroundColor);
        g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        g.setFont(new Font("TimesRoman", 3*mapScale));
        for(MapObject e : elements){
            GraphicElement graphicElement = calculateGraphicElement(e);

            Vector2[] points = e.getArea().getAsPolygon().getPoints();

            final double[] xPoints = new double[points.length];
            final double[] yPoints = new double[points.length];

            for (int i = 0; i < points.length; i++) {
                Vector2 point = points[i];
                xPoints[i] = point.getX();
                yPoints[i] = point.getY();
            }

            if(graphicElement.fill){
                g.setFill(graphicElement.color);
                g.fillPolygon(scalePoints(xPoints,mapScale),scalePoints(yPoints,mapScale),4);
            }else {
                g.setStroke(graphicElement.color);
                g.setLineWidth(2);
                g.strokePolygon(scalePoints(xPoints,mapScale),scalePoints(yPoints,mapScale),4);
            }

            Vector2 center = e.getArea().getCenter();
            g.setTextAlign(TextAlignment.CENTER);
            if(settings.showText){
                g.setFill(Color.WHITE);
                g.fillText(graphicElement.text,center.getX()*mapScale,center.getY()*mapScale+1.5*mapScale);
            }
        }
    }

    private void generateScreenshot(AtomicBoolean rendering, MainController.History history, File file)
    {

        Function<WritableImage, BufferedImage> convert = (input) -> {
            BufferedImage bufferedImage = new BufferedImage((int) Math.rint(input.getWidth()), (int) Math.rint(input.getHeight()),
                    BufferedImage.TYPE_INT_ARGB);
            IntBuffer buffer = IntBuffer.allocate(bufferedImage.getWidth() * bufferedImage.getHeight());
            // copy...
            input.getPixelReader().getPixels(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), WritablePixelFormat.getIntArgbInstance(),
                    buffer, bufferedImage.getWidth());
            // ...paste
            bufferedImage.setRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), buffer.array(),
                    0, bufferedImage.getWidth());

            return bufferedImage;
        };

        BufferedImage screenshot = new BufferedImage((int) canvas.getWidth(), (int) canvas.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = screenshot.getGraphics();

        AtomicInteger i = new AtomicInteger();
        {
            WritableImage writableImage = new WritableImage((int) Math.rint(canvas.getWidth()),
                    (int) Math.rint(canvas.getHeight()));
            i.incrementAndGet();
            Platform.runLater(() -> {

                canvas.snapshot(null, writableImage);
                i.decrementAndGet();
            });
            while (i.get() > 0);
            graphics.drawImage(convert.apply(writableImage), 0, 0, null);
        }

        {
            WritableImage writableImage = new WritableImage((int) Math.rint(canvas.getWidth()),
                    (int) Math.rint(canvas.getHeight()));
            i.incrementAndGet();
            Platform.runLater(() -> {
                drawMovables(history.guardContainers, history.intruderContainers, history.dynamicObjects);
                SnapshotParameters snapshotParameters = new SnapshotParameters();
                snapshotParameters.setFill(Color.rgb(0, 0, 0, 0));
                canvasAgents.snapshot(snapshotParameters, writableImage);
                i.decrementAndGet();
            });
            while (i.get() > 0);
            graphics.drawImage(convert.apply(writableImage), 0, 0, null);
        }


        graphics.dispose();

        try {
            if(rendering.get())
            {
                ImageIO.write(screenshot, "png", file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateVideo()
    {
        if(hasHistory)
        {
            AtomicBoolean hasRenderedFrames = new AtomicBoolean(false);
            VBox root = new VBox();
            Stage stage = new Stage();
            Scene scene = new Scene(root, 720, 360);
            StackPane progressBarHolder = new StackPane();
            ProgressBar progressBar = new ProgressBar(0);
            File style = new File("./src/main/java/Group9/gui2/style.css");
            scene.getStylesheets().add(style.toURI().toString());
            stage.setScene(scene);

            TextArea console = new TextArea();
            console.setEditable(false);

            ComboBox<String> resolution = new ComboBox<>(FXCollections.observableArrayList(
                    "1280x720", "1920x1080", "3840x2160"
            ));
            resolution.getSelectionModel().select(1);

            Button selectFileLocation = new Button("Select location");
            Button renderButton = new Button("Render");
            renderButton.setDisable(true);

            Slider fpsSlider = new Slider(1, 120, 30);
            fpsSlider.valueProperty().addListener((obs, oldval, newVal) -> fpsSlider.setValue(newVal.intValue()));
            fpsSlider.setSnapToTicks(true);
            fpsSlider.setBlockIncrement(1);
            fpsSlider.setMinorTickCount(5);
            fpsSlider.setMajorTickUnit(10);
            fpsSlider.setShowTickLabels(true);
            fpsSlider.setShowTickMarks(true);
            progressBarHolder.getChildren().add(progressBar);
            root.getChildren().addAll(selectFileLocation, resolution, fpsSlider, renderButton,progressBarHolder, console);
            AtomicReference<File> output = new AtomicReference<>();
            //Styling
            progressBarHolder.setMinWidth(360);
            progressBarHolder.setAlignment(Pos.CENTER);
            fpsSlider.getStyleClass().add("sDark");
            progressBar.getStyleClass().add("progress-bar-style");
            root.getStyleClass().add("video-interface");
            resolution.getStyleClass().add("drop-box");
            selectFileLocation.getStyleClass().add("safe-button");
            renderButton.getStyleClass().add("safe-button-2");

            {

                selectFileLocation.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showSaveDialog(stage);
                    if(file != null)
                    {
                        String path = file.getAbsolutePath();
                        int index = path.lastIndexOf(".");
                        if(index == -1 || !path.substring(index).equalsIgnoreCase(".mp4"))
                        {
                            path += ".mp4";
                        }
                        output.set(new File(path));
                        renderButton.setDisable(false);
                    }
                });

            }

            AtomicBoolean rendering = new AtomicBoolean(true);
            {
                renderButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

                    File tempDirectory = new File(String.format("%s/gameinterop", System.getProperty("java.io.tmpdir")));

                    Thread renderVideoThread = new Thread(() -> {
                        try {
                            renderButton.setDisable(true);
                            progressBar.setProgress(0.92);

                            final String frameWidth = resolution.getSelectionModel().getSelectedItem().split("x")[0];

                            Process pr = null;

                            if(ffmpegWindows)
                            {
                                pr = new ProcessBuilder(String.format(
                                        "ffmpeg -y -framerate %.2f -start_number 0 -i %s\\%%d.png -vf \"scale=%s:trunc(ow/a/2)*2:flags=lanczos\" -c:v libx264 -preset slow -crf 21 %s",
                                        fpsSlider.getValue(), tempDirectory.getAbsolutePath(), frameWidth, output.get().getAbsolutePath()).split(" "))
                                        .redirectErrorStream(true).start();
                            }
                            else if(ffmpegLinux)
                            {
                                pr = Runtime.getRuntime().exec(String.format(
                                        "ffmpeg -y -framerate %.2f -start_number 0 -i %s/%%d.png -vf scale=%s:trunc(ow/a/2)*2:flags=lanczos -c:v libx264 -preset slow -crf 21 %s",
                                            fpsSlider.getValue(), tempDirectory.getAbsolutePath(), frameWidth, output.get().getAbsolutePath()));
                            }

                            BufferedReader logInfo = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                            BufferedReader logError = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
                            logInfo.lines().map(line -> line + "\n").forEach(console::appendText);
                            logError.lines().map(line -> line + "\n").forEach(console::appendText);

                            pr.waitFor();
                            pr.destroy();
                            logInfo.close();
                            logError.close();

                            progressBar.setProgress(1);

                            renderButton.setDisable(false);

                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

                    if(!hasRenderedFrames.get())
                    {
                        hasRenderedFrames.set(true);

                        if(tempDirectory.exists()) {
                            try {
                                deleteDirectory(tempDirectory);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                        }

                        if(!tempDirectory.mkdir())
                        {
                            return;
                        }


                        Thread generateFramesThread = new Thread(() -> {
                            renderButton.setDisable(true);
                            for(int i = 0; i <= gui.getMainController().getHistoryIndex() && rendering.get(); i++)
                            {
                                gui.getMainController().getHistoryViewIndex().set(i);
                                MainController.History entry = gui.getMainController().getCurrentHistory();

                                generateScreenshot(rendering, entry, new File(String.format("%s%s%d.png", tempDirectory.getAbsolutePath(),
                                        File.separator, i)));
                                progressBar.setProgress((i / (double) gui.getMainController().getHistoryIndex()) * 0.9D);
                            };
                            renderVideoThread.start();

                        });
                        generateFramesThread.start();

                    } else {
                        renderVideoThread.start();
                    }

                    stage.setOnCloseRequest((closeRequest) -> {
                        try {
                            rendering.set(false);
                            deleteDirectory(tempDirectory);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                });
            }

            stage.initOwner(gui.getPrimary());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        }
    }
    private void openHelp(){
        String text = "History - History is storing the state of the game after each turn. History generation can be" +
                " disabled in the menu. If it is disabled, playback, step-by-step inspection, and video rendering will not" +
                " be available.\n\n" +
                "Playback - Once the game has ended, you can use the left and right keys to go a frame forward and backward," +
                " Shift + Left/Right moves 10 frames forwards or backwards.\n\n" +
                "Reload Game - This button starts a new instance of the simulation.\n\n" +
                "Render Video - This renders the simulation to a MP4 file. This feature requires history to be enabled." +
                " ffmpeg also needs to be installed.";



        VBox root = new VBox();
        Stage stage = new Stage();
        stage.setTitle("Help");
        Scene scene = new Scene(root, 720, 360);
        File style = new File("./src/main/java/Group9/gui2/style.css");
        scene.getStylesheets().add(style.toURI().toString());
        root.getStyleClass().add("video-interface");
        Label helpText = new Label(text);
        helpText.setWrapText(true);
        helpText.getStyleClass().add("help-Text");
        root.getChildren().add(helpText);
        stage.setScene(scene);
        stage.initOwner(gui.getPrimary());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private boolean isFFMPEGInstalled()
    {
        try {
            Runtime.getRuntime().exec("ffmpeg");
            this.ffmpegLinux = true;
        } catch (Exception e) {
            this.ffmpegLinux = false;
        }

        try {
            new ProcessBuilder("cmd.exe /c ffmpeg").start().waitFor();
            this.ffmpegWindows = true;
        } catch (Exception e) {
            this.ffmpegWindows = false;
        }

        return (ffmpegLinux || ffmpegWindows);
    }

    private void deleteDirectory(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
            {
                deleteDirectory(c);
            }
        }
        f.delete();
    }

    public void drawMovables(List<GuardContainer> guards, List<IntruderContainer> intruders, List<DynamicObject<?>> objects){
        GraphicsContext g = canvasAgents.getGraphicsContext2D();
        g.clearRect(0,0,canvasAgents.getWidth(),canvasAgents.getHeight());
        for(DynamicObject<?> dynamicObject : objects)
        {
            if(dynamicObject instanceof Pheromone)
            {
                g.setFill(GuiSettings.pheromoneColor);
                drawPheromone(g, (Pheromone) dynamicObject);

            }
            else if(dynamicObject instanceof Sound)
            {
                g.setFill(Color.ORCHID);
                //TODO draw sounds
            }
            else
            {
                throw new IllegalArgumentException();
            }
        }
        g.setFill(GuiSettings.guardColor);
        for(GuardContainer movables : guards){
            drawAgent(g, movables);
        }

        g.setFill(GuiSettings.intruderColor);
        for(IntruderContainer movables : intruders){
            drawAgent(g, movables);
        }
    }
    private void drawPheromone(GraphicsContext g, Pheromone pheromone){
        Vector2 z = pheromone.getCenter();
        double radius = mapScale * pheromone.getRadius();
        double x = z.getX()*mapScale;
        double y = z.getY()*mapScale;
        g.fillOval(x-radius/2,y-radius/2,radius,radius);
    }
    private void drawAgent(GraphicsContext g, AgentContainer<?> agent) {

        Vector2 center = agent.getPosition().mul(mapScale);

        {
            double radius = mapScale*settings.agentScale * AgentContainer._RADIUS;
            double x = center.getX();
            double y = center.getY();
            g.fillOval(x-radius/2,y-radius/2,radius,radius);
        }

        {

            FieldOfView fov = agent.getFOV(map.getEffectAreas(agent));

            final double r = fov.getRange().getValue() * mapScale;
            final double alpha = fov.getViewAngle().getRadians();

            final double angle = agent.getDirection().rotated(alpha / 2).getClockDirection() - Math.PI / 2;
            g.setStroke(g.getFill());

            g.strokeArc(center.getX() - r, center.getY() - r, r*2, r*2,
                    (angle / (2 * Math.PI)) * 360,
                    fov.getViewAngle().getDegrees(), ArcType.ROUND);

        }

    }
    private void calcScale(){
        double height = canvasPane.getHeight();
        double width = canvasPane.getWidth();
        double scale = height/map.getGameSettings().getHeight();
        if(scale*map.getGameSettings().getWidth()< width){
            this.mapScale = scale*0.9;
        }else{
            this.mapScale = width/map.getGameSettings().getWidth()*0.9;
        }
    }

    protected GraphicElement calculateGraphicElement(MapObject element){
        if(element instanceof Wall){
            return new GraphicElement(GuiSettings.wallColor,"",true);
        }
        if(element instanceof TargetArea){
            return new GraphicElement(GuiSettings.targetAreaColor,"T",false);
        }
        if(element instanceof Spawn.Intruder){
            return new GraphicElement(GuiSettings.spawnIntrudersColor,"SI",false);
        }
        if(element instanceof Spawn.Guard){
            return new GraphicElement(GuiSettings.spawnGuardsColor,"SG",false);
        }
        if(element instanceof ShadedArea){
            return new GraphicElement(GuiSettings.shadedColor,"",true);
        }
        if(element instanceof Door){
            return new GraphicElement(GuiSettings.doorColor,"D",true);
        }
        if(element instanceof Window){
            return new GraphicElement(GuiSettings.windowColor,"",true);
        }
        if(element instanceof SentryTower){
            return new GraphicElement(GuiSettings.sentryColor,"",true);
        }
        if(element instanceof TeleportArea){
            return new GraphicElement(GuiSettings.teleportColor,"Tp",true);
        }
        System.out.println("Unknown");
        return new GraphicElement(Color.RED,"",false);
    }
    protected double[]  scalePoints(double[] points,double scale){
        double[] newPoints = new double[points.length];
        for(int i=0;i<points.length;i++){
            newPoints[i] = points[i]*scale;
        }
        return newPoints;
    }
    public boolean isHasHistory() {
        return hasHistory;
    }
}
