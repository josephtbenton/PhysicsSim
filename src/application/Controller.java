package application;


import java.util.ArrayList;
import java.util.HashSet;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;


public class Controller {
	private long FRAMES_PER_SEC = 60L;
	private long NANO_INTERVAL = 1000000000L / FRAMES_PER_SEC;
    private boolean optimize;
    private boolean spray;
    double startX = 0;
    double startY = 0;
    Circle crosshair = new Circle(2, Color.GREEN);
    Line pointer = new Line(0, 0, 0, 0);
    @FXML
    Pane canvas;
    @FXML
    Label text;
    @FXML
    CheckBox cullToggle;
    @FXML
    CheckBox toggle;
    ArrayList<Ball> actors = new ArrayList<Ball>();

	private AnimationTimer timer = new AnimationTimer() {
		long last = 0;
        int fps = 0;


		@Override
		public void handle(long now) {
            QuadTree quad = new QuadTree(0, new Rectangle(0, 0, canvas.widthProperty().doubleValue(), canvas.heightProperty().doubleValue()));
            quad.clear();
            ArrayList<Ball> neighbors = new ArrayList<>();
			if (now - last > NANO_INTERVAL) {
                fps = (int)(1 / ((now - last) / 1000000000.0) * .5 + fps * .5);
                if (fps < 30 && cullToggle.isSelected()) {
                    for (int i = 0; i < 10 ; i++) {
                        Ball victim = actors.remove(i);
                        victim.shape.setFill(Color.WHITESMOKE);
                        victim.shape.setStroke(Color.WHITESMOKE);
                    }
                }
                optimize = toggle.isSelected();
                text.setText("Balls: " + actors.size() + " FPS: " + fps);
				for (Ball i: actors) {
					i.move();
                    quad.insert(i);
				}
                for (Ball i: actors) {
                    i.resetPressure();
                    neighbors.clear();
                    quad.retrieve(neighbors, i);
                    for (Ball other: optimize ? neighbors : actors) {
                        Ball[] pair = new Ball[]{i, other};
                        Ball[] pairInv = new Ball[]{other, i};
                        if (i.isColliding(other)) {
                            i.resolveCollision(other);
                        }
                    }
                }
                last = now;
			}
		}
	};

	@FXML
	public void initialize() {
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                ev -> {
                    pointer.setStartX(startX);
                    pointer.setStartY(startY);
                    pointer.setEndX(startX - (ev.getX() - startX) / 2);
                    pointer.setEndY(startY - (ev.getY() - startY) / 2);
                    if (ev.isMiddleButtonDown() || ev.isShiftDown()) {
                        spray = true;
                        Ball ball = new Ball(20, 5, startX, startY);
                        ball.addTo(canvas);
                        actors.add(ball);
                        ball.setSpeed(-(ev.getX() - startX) / 10, -(ev.getY() - startY) / 10);
                    }
                });

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                ev -> {
                    crosshair = new Circle(2, Color.GREEN);
                    pointer = new Line(0, 0, 0, 0);

                    spray = false;
                    pointer.setStartX(0);
                    pointer.setStartY(0);
                    pointer.setEndX(0);
                    pointer.setEndY(0);
                    canvas.getChildren().add(pointer);
                    canvas.getChildren().add(crosshair);
                    startX = ev.getX();
                    startY = ev.getY();
                    crosshair.setCenterX(startX);
                    crosshair.setCenterY(startY);

                });

		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                ev -> {
                    canvas.getChildren().remove(pointer);
                    canvas.getChildren().remove(crosshair);
                    if (!spray) {
                        Ball ball = new Ball(30, 5, startX, startY);
                        ball.addTo(canvas);
                        actors.add(ball);
                        ball.setSpeed(-(ev.getX() - startX) / 10, -(ev.getY() - startY) / 10);
                    }
                });
	}
	@FXML
	void start() {
		timer.start();
	}
	@FXML
	void stop() {
		timer.stop();
	}
	@FXML
	void clear() {
        boolean re = cullToggle.isSelected();
        cullToggle.setSelected(false);
		for (Node i: canvas.getChildren()) {
			i.setVisible(false);
			actors.clear();
		}
        cullToggle.setSelected(re);
	}
}
