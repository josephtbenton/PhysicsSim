package application;

import static org.junit.Assert.*;
import javafx.scene.layout.Pane;

import org.junit.Test;

public class BallTest {
	
	@Test
	public void moveTest() {
		Ball ball = new Ball(1, 1, 50, 50);
		Pane canvas = new Pane();
		ball.addTo(canvas);
		canvas.setMinSize(100, 100);
		assertEquals(-10, ball.speedY);
		ball.move();
		assertEquals(7, ball.speedY);
		
	}

	@Test
	public void CollisionTest() {
		Pane canvas = new Pane();
		Ball ball = new Ball(1,1,0,0);
		assertTrue(ball.isCollidingX());
		assertTrue(ball.isCollidingY());
	}
	
	
	
	
}
