package application;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;


public class Controller {
	private long FRAMES_PER_SEC = 60L;
	private long NANO_INTERVAL = 1000000000L / FRAMES_PER_SEC;
	
	private AnimationTimer timer = new AnimationTimer() {
		long last = 0;
        //QuadTree quad = new QuadTree(0, new Rectangle(0, 0, canvas.getWidth(), canvas.getHeight()));
		
		@Override
		public void handle(long now) {
			if (now - last > NANO_INTERVAL) {
				for (Ball i: actors) {
					i.move(canvas);
					last = now;
				}
			}
		}
	};

	@FXML
	Pane canvas;
	ArrayList<Ball> actors = new ArrayList<Ball>();
	
	@FXML
	public void initialize() {
		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
			       new EventHandler<MouseEvent>() {
			           @Override
			           public void handle(MouseEvent ev) {
			        	   Ball ball = new Ball(20, 5, ev.getX(), ev.getY());
			        	   ball.addTo(canvas);
			        	   actors.add(ball);
			               
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
