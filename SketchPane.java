// import libraries
import javafx.scene.layout.*;
import javafx.scene.shape.Shape;
import javafx.scene.shape.*;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.control.ToggleGroup;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;

// All components organized on a base BorderPane which is extended here
public class SketchPane extends BorderPane {
	
	ArrayList<Shape> shapeList;
	ArrayList<Shape> tempList;
	Button undoButton;
	Button eraseButton;
	Label fillColorLabel;				// declare all instance variables
	Label strokeColorLabel;
	Label strokeWidthLabel;
	ComboBox<String> fillColorCombo;
	ComboBox<String> strokeWidthCombo;
	ComboBox<String> strokeColorCombo;
	RadioButton radioButtonLine;
	RadioButton radioButtonRectangle;
	RadioButton radioButtonCircle;
	Pane sketchCanvas;
	Color[] colors;
	String[] strokeWidth;
	String[] colorLabels;
	Color currentStrokeColor;
	Color currentFillColor;
	int currentStrokeWidth;
	Line line;
	Circle circle;
	Rectangle rectangle;
	double x1;
	double y1;

	// Implement the constructor
	public SketchPane() {
		// Colors, labels, and stroke widths that are available to the user
		colors = new Color[] {Color.BLACK, Color.GREY, Color.YELLOW, Color.GOLD, Color.ORANGE, Color.DARKRED, Color.PURPLE, Color.HOTPINK, Color.TEAL, Color.DEEPSKYBLUE, Color.LIME};
        colorLabels = new String[] {"black", "grey", "yellow", "gold", "orange", "dark red", "purple", "hot pink", "teal", "deep sky blue", "lime"};
        fillColorLabel = new Label("Fill Color:");
        strokeColorLabel = new Label("Stroke Color:");
        strokeWidthLabel = new Label("Stroke Width:");
        strokeWidth = new String[] {"1", "3", "5", "7", "9", "11", "13"};
        
        // Instantiate buttons and register them to button handler
        undoButton = new Button("Undo");
        eraseButton = new Button("Erase");
        undoButton.setOnAction(new ButtonHandler());
        eraseButton.setOnAction(new ButtonHandler());
        
        // Instantiate 3 ComboBoxes for shape fill, stroke width, and stroke color
        fillColorCombo = new ComboBox<String>();
    	strokeWidthCombo = new ComboBox<String>();
    	strokeColorCombo = new ComboBox<String>();
    	
    	// add the available color choices and stroke width to the ComboBoxes
    	fillColorCombo.getItems().addAll(colorLabels);
    	strokeWidthCombo.getItems().addAll(strokeWidth);
    	strokeColorCombo.getItems().addAll(colorLabels);
    	
    	// set the default fill and stroke color to BLACK and the default stroke width to 1
    	fillColorCombo.getSelectionModel().select(0);
    	strokeColorCombo.getSelectionModel().select(0);
    	strokeWidthCombo.getSelectionModel().select(0);
    	
    	// register the ComboBoxes with the corresponding handler
    	fillColorCombo.setOnAction(new ColorHandler());
    	strokeColorCombo.setOnAction(new ColorHandler());
    	strokeWidthCombo.setOnAction(new WidthHandler());
    	
    	// Instantiate 3 RadioButtons to choose the shape to be drawn: Line, Rectangle, or Circle
    	radioButtonLine = new RadioButton("Line");
    	radioButtonRectangle = new RadioButton("Rectangle");
    	radioButtonCircle = new RadioButton("Circle");
    	
    	// add all RadioButtons to a ToggleGroup and select the Line by default
    	ToggleGroup toggle = new ToggleGroup();
    	radioButtonLine.setToggleGroup(toggle);
    	radioButtonRectangle.setToggleGroup(toggle);
    	radioButtonCircle.setToggleGroup(toggle);
    	radioButtonLine.setSelected(true);
    	
    	// Instantiate sketchCanvas and set its background color to WHITE
    	sketchCanvas = new Pane();
    	sketchCanvas.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    	
    	// Instantiate Top HBox for ComboBoxes with size 20 and set the minimum size to (20,40)
    	HBox hbTop = new HBox(20);
    	hbTop.setMinSize(20, 40);	// set MinSize
    	hbTop.setAlignment(Pos.CENTER);		// set alignment to center
    	hbTop.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));	// set background to lightgrey
    	hbTop.getChildren().addAll(fillColorLabel, fillColorCombo, strokeWidthLabel, 
    					strokeWidthCombo, strokeColorLabel, strokeColorCombo);		// add elements to HBox
    	
    	// Instantiate Bottom HBox for Buttons with size 20 and set the minimum size to (20,40)
    	HBox hbBottom = new HBox(20);
    	hbBottom.setMinSize(20, 40);	// set MinSize
    	hbBottom.setAlignment(Pos.CENTER);		// set alignment to center
    	hbBottom.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));	// set background to lightgrey
    	hbBottom.getChildren().addAll(radioButtonLine, radioButtonRectangle, radioButtonCircle,
    							undoButton, eraseButton);		// add elements to HBox
    	
    	// Instantiate arraylists
    	shapeList = new ArrayList<Shape>();
    	tempList = new ArrayList<Shape>();
    	
    	// register mousehandler to the canvas
    	sketchCanvas.addEventHandler(MouseEvent.ANY, new MouseHandler());
    	
    	// instantiate some last variables
    	x1 = 0;
    	y1 = 0;
    	currentStrokeWidth = 1;
    	currentFillColor = Color.BLACK;
    	currentStrokeColor = Color.BLACK;

    	// organize components on the BorderPane
    	this.setCenter(sketchCanvas);
    	this.setTop(hbTop);
    	this.setBottom(hbBottom);
    }

	// mousehandler class responsible for drawing on canvas
	private class MouseHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			// Implement the mouse handler for Circle and Line
			// Rectangle Example given!
			if (radioButtonRectangle.isSelected()) {
				//Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x1 = event.getX();	// get X coordinate
					y1 = event.getY();	// get y coordinate
					rectangle = new Rectangle();	// instantiate new rectangle
					rectangle.setX(x1);
					rectangle.setY(y1);
					shapeList.add(rectangle);	// add shape to shapeList
					rectangle.setFill(Color.WHITE);	// set color to white
					rectangle.setStroke(Color.BLACK);	// set border color to black
					sketchCanvas.getChildren().add(rectangle);	// add shape to canvas
				}
				// Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					
					double changeX = event.getX() - x1;
					double changeY = event.getY() - y1;
					
					if (changeX < 0)	// if negative direction
					{
						rectangle.setX(event.getX());	// set new point as the X-origin
						rectangle.setWidth(Math.abs(changeX));	// set width from there
					}
					else
					{
						rectangle.setWidth(Math.abs(changeX));	// else use original origin
					}
					
					if (changeY < 0)
					{
						rectangle.setY(event.getY());	// set new point as the Y-origin
						rectangle.setHeight(Math.abs(changeY));	// set height from there
					}
					else
					{
						rectangle.setHeight(Math.abs(changeY)); // else use original origin
					}
				}
				//Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					// set user-defined choices to the rectangle
					rectangle.setFill(currentFillColor);
					rectangle.setStroke(currentStrokeColor);
					rectangle.setStrokeWidth(currentStrokeWidth);
				}
			}
			
			// if Line is selected
			else if (radioButtonLine.isSelected())
			{
				// if mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
				{
					x1 = event.getX();	// get X coordinate
					y1 = event.getY();	// get Y coordinate
					line = new Line(x1, y1, x1, y1);	// instantiate new line
					shapeList.add(line);	// add line to shapeList
					sketchCanvas.getChildren().add(line);	// add line to the canvas
				}
				
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED)
				{
					// set end points for the line
					line.setEndX(event.getX());
					line.setEndY(event.getY());
				}
				
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
				{
					// set user-defined choices to the line
					line.setFill(currentFillColor);
					line.setStroke(currentStrokeColor);
					line.setStrokeWidth(currentStrokeWidth);
				}
			}
			
			// if circle is selected
			else if (radioButtonCircle.isSelected())
			{
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
				{
					x1 = event.getX();	// get X coordinate
					y1 = event.getY();	// get Y coordinate
					circle = new Circle();	// instantiate new circle
					circle.setCenterX(x1);	// set circle center
					circle.setCenterY(y1);
					circle.setFill(Color.WHITE);	// set color to white
					circle.setStroke(Color.BLACK);	// set border color to black
					shapeList.add(circle);			// add circle to shapeList
					sketchCanvas.getChildren().add(circle);	// add circle to canvas
				}
				
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED)
				{
					// set circle radius
					circle.setRadius(getDistance(x1, y1, event.getX(), event.getY()));
				}
				
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED)
				{
					// set user-defined choices to the circle
					circle.setFill(currentFillColor);
					circle.setStroke(currentStrokeColor);
					circle.setStrokeWidth(currentStrokeWidth);
				}
			}
		}
	}
	
	// button handler class for user drawing choices to be handled
	private class ButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// Implement the button handler
			if (event.getSource() == undoButton && !shapeList.isEmpty())
			{
				// remove previous shape from shapeList and canvas
				sketchCanvas.getChildren().remove(shapeList.size() - 1);
				shapeList.remove(shapeList.size() - 1);
			} 
			else if (event.getSource() == undoButton && shapeList.isEmpty())
			{
				// copy tempList to shapeList and clear tempList
				shapeList = new ArrayList<Shape>(tempList);
				sketchCanvas.getChildren().addAll(shapeList);
				tempList.clear();
			}
			
			if (event.getSource() == eraseButton && !shapeList.isEmpty())
			{
				tempList.clear();	// clear templist
				tempList = new ArrayList<Shape>(shapeList);	// copy shapelist to templist
				shapeList.clear();	// clear shapelist
				sketchCanvas.getChildren().clear();	// clear canvas
			}
		}
	}

	// color handler class for user drawing choices to be handled
	private class ColorHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// Implement the color handler
			// set current color to what the user chose from the combobox
			currentFillColor = colors[fillColorCombo.getSelectionModel().getSelectedIndex()];
			currentStrokeColor = colors[strokeColorCombo.getSelectionModel().getSelectedIndex()];
		}
	}
	
	// width handler class for user drawing choices to be handled
	private class WidthHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event){
			// Implement the stroke width handler
			// set current stroke width to what the user chose from the combobox
			currentStrokeWidth = Integer.parseInt(strokeWidthCombo.getSelectionModel().getSelectedItem());
		}
	}

	// Get the Euclidean distance between (x1,y1) and (x2,y2)
    private double getDistance(double x1, double y1, double x2, double y2)  {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}