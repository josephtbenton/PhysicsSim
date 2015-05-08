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
import javafx.scene.shape.Rectangle;


public class Controller {
	private long FRAMES_PER_SEC = 60L;
	private long NANO_INTERVAL = 1000000000L / FRAMES_PER_SEC;
    private boolean optimize;
    double startX = 0;
    double startY = 0;
    Circle crosshair = new Circle(2, Color.GREEN);
    @FXML
    Pane canvas;
    @FXML
    Label text;
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
            HashSet<Ball[]> checked = new HashSet<>();



			if (now - last > NANO_INTERVAL) {
                fps = (int)(1 / ((now - last) / 1000000000.0) * .5 + fps * .5);


                optimize = toggle.isSelected();
                text.setText("Balls: " + actors.size() + " FPS: " + fps);

				for (Ball i: actors) {
					i.move();
                    quad.insert(i);
				}

                for (Ball i: actors) {
                    boolean colliding = false;
                    neighbors.clear();
                    quad.retrieve(neighbors, i);
                    for (Ball other: optimize ? neighbors : actors) {
                        Ball[] pair = new Ball[]{i, other};
                        Ball[] pairInv = new Ball[]{other, i};
                        if (i.isColliding(other)) {
                            i.resolveCollision(other);
                            i.shape.setFill(Color.RED);
                            colliding = true;
                        }
                    }
                    if (!colliding) i.shape.setFill(Color.BLUE);
                }
                last = now;
			}
		}
	};

	@FXML
	public void initialize() {
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                ev -> {
                    if (ev.isMiddleButtonDown() || ev.isShiftDown()) {
                        Ball ball = new Ball(15, 5, ev.getX(), ev.getY());
                        ball.addTo(canvas);
                        actors.add(ball);
                    }
                });

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                ev -> {
                    canvas.getChildren().add(crosshair);
                    startX = ev.getX();
                    startY = ev.getY();
                    crosshair.setCenterX(startX);
                    crosshair.setCenterY(startY);

                });

		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                ev -> {
                    if (!ev.isShiftDown()) {
                        canvas.getChildren().remove(crosshair);
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
		for (Node i: canvas.getChildren()) {
			i.setVisible(false);
			actors.clear();
		}
	}
}
