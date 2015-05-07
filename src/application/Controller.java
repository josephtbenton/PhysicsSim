package application;


import java.util.ArrayList;

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
import javafx.scene.shape.Rectangle;


public class Controller {
	private long FRAMES_PER_SEC = 60L;
	private long NANO_INTERVAL = 1000000000L / FRAMES_PER_SEC;
    private boolean optimize;
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


                optimize = toggle.isSelected();
                text.setText("Balls: " + actors.size() + " FPS: " + fps);

				for (Ball i: actors) {
					i.move();
                    quad.insert(i);
				}

                for (Ball i: actors) {
                    boolean colliding = false;
                    neighbors.clear();
                    ArrayList<Ball> others = quad.retrieve(neighbors, i);
                    for (Ball other: optimize ? others : actors) {

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
	Pane canvas;
    @FXML
    Label text;
    @FXML
    CheckBox toggle;
	ArrayList<Ball> actors = new ArrayList<Ball>();

	@FXML
	public void initialize() {
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                ev -> {
                    if (ev.isMiddleButtonDown() || ev.isShiftDown()) {
                        Ball ball = new Ball(20, 5, ev.getX(), ev.getY());
                        ball.addTo(canvas);
                        actors.add(ball);
                    }
                });

		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                ev -> {
                    Ball ball = new Ball(20, 5, ev.getX(), ev.getY());
                    ball.addTo(canvas);
                    actors.add(ball);

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
