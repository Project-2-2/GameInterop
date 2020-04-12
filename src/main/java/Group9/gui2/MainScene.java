package Group9.gui2;

import Group9.agent.container.AgentContainer;
import Group9.agent.container.GuardContainer;
import Group9.agent.container.IntruderContainer;
import Group9.map.GameMap;
import Group9.map.dynamic.DynamicObject;
import Group9.map.dynamic.Pheromone;
import Group9.map.dynamic.Sound;
import Group9.map.objects.*;
import Group9.math.Vector2;
import Interop.Geometry.Angle;
import Interop.Geometry.Point;
import Interop.Percept.Vision.FieldOfView;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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

import java.io.File;
import java.util.List;
import java.util.Optional;

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
    private Label stop = new Label();
    private HBox animationSettings = new HBox();
    private HBox maxSpeedSetting = new HBox();
    private Label maxSpeedLabel = new Label("Maximum Speed");
    private Label animationLabel = new Label("Speed");
    private Label animationSliderInfo = new Label("15");
    private final GameMap map;
    private Gui gui;
    private FileChooser fileChooser = new FileChooser();
    private boolean hasHistory = false;
    private CheckBox maxSpeed = new CheckBox();

    //Buttons
    private Label reloadMapButton = new Label("Reload Map");
    private Label button1 = new Label("Toggle Description");
    private Label button2 = new Label("Toggle Agent-Zoom");
    private Label button3 = new Label("Load Map");
    private Label button4 = new Label("Button 4");

    ///Agent
    private List<MapObject> elements;
    public MainScene(StackPane mainStack, GameMap map,Gui gui) {
        super(mainStack);
        this.gui = gui;
        this.mainStack = mainStack;
        this.map = map;
        elements = map.getObjects();
        build();
        scale();
        style();
        listener();
    }
    private void build(){
        menu.getChildren().addAll(reloadMapButton,button1,button2,button3,button4,animationSettings,maxSpeedSetting);
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
    private void scale(){
        double height = this.getHeight();
        double width = this.getWidth();
        if(height<=0){
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
        button1.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        button1.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        button2.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        button2.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        button3.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        button3.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        button4.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        button4.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        animationSettings.setMaxSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
        animationSettings.setMinSize(GuiSettings.widthMenuFocus,GuiSettings.buttonHeight);
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
        button1.getStyleClass().add("nav-button");
        button2.getStyleClass().add("nav-button");
        button3.getStyleClass().add("nav-button");
        button4.getStyleClass().add("nav-button");
        play.getStyleClass().add("play-button");
        stop.getStyleClass().add("stop-button");
        slider.getStyleClass().add("sDark");
        animationSpeedSlider.getStyleClass().add("sLight");
        animationSettings.getStyleClass().add("animation-slider-pane");
        playContainer.getStyleClass().add("button-container");
        stopContainer.getStyleClass().add("button-container");
        animationLabel.getStyleClass().add("animation-label");
        animationSliderInfo.getStyleClass().add("animation-label");
        maxSpeedSetting.getStyleClass().add("animation-slider-pane");
        maxSpeedLabel.getStyleClass().add("animation-label");
        quickSettings.setAlignment(Pos.BOTTOM_CENTER);
        quickSettingsBar.setPadding(new Insets(10));
        quickSettingsBar.setSpacing(5);
        quickSettingsBar.setAlignment(Pos.CENTER);

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
            gui.restartGame();
            gui.getMainController().updateGameSpeed((int) animationSpeedSlider.getValue());
        } );
        button1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            settings.toggleText();
            draw();
        } );
        button2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            settings.toggleAgentScale();
            draw();
        } );
        button3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            File file = fileChooser.showOpenDialog(gui.getPrimary());
            if(file != null){
                gui.setMapFile(file);
                gui.restartGame();
            }
        } );
        reloadMapButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            rescaleMap();
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
    }
    public void activateHistory(){
        hasHistory =true;
        gui.getMainController().getHistoryViewIndex().get();
        int age = gui.getMainController().getHistoryIndex();
        slider.setMax(age);
        slider.setValue(age);
        slider.setMin(0);
    }
    public void rescale(){
        scale();
        draw();
    }
    public void rescaleMap(){
        calcScale();
        scale();
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
    public void drawMovables(List<GuardContainer> guards, List<IntruderContainer> intruders, List<DynamicObject<?>> objects){
        GraphicsContext g = canvasAgents.getGraphicsContext2D();
        g.clearRect(0,0,canvasAgents.getWidth(),canvasAgents.getHeight());

        g.setFill(GuiSettings.guardColor);
        for(GuardContainer movables : guards){
            drawAgent(g, movables);
        }

        g.setFill(GuiSettings.intruderColor);
        for(IntruderContainer movables : intruders){
            drawAgent(g, movables);
        }

        for(DynamicObject<?> dynamicObject : objects)
        {
            if(dynamicObject instanceof Pheromone)
            {
                g.setFill(Color.ORANGERED);
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

    }
    private void drawPheromone(GraphicsContext g, Pheromone pheromone){
        Vector2 z = pheromone.getCenter();
        double radius = 1*mapScale*settings.agentScale;
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
