package application;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
	Circle shape;
	int mass;
	int speedX;
	int speedY;
	double size;
	double e;
	private ReadOnlyDoubleProperty height, width;
    Pane canvas;


	public Ball (double size, int mass, double x, double y) {
		shape = new Circle(0, 0, size, new Color(Math.random(), Math.random(), Math.random(), 1));
		shape.setTranslateX(x);
		shape.setTranslateY(y);
		this.size = size;
		this.mass = mass;
		this.speedX = (int)(Math.random()*60 - 30);
		this.speedY = -10;
		this.e = .65;
	}

	public void addTo(Pane canvas) {
		this.canvas = canvas;
        canvas.getChildren().add(shape);
	}

	public void move() {
		if (isCollidingY()){
			bounceY();
		} else if (isCollidingX()) {
			bounceX();
		} else {
			shape.setTranslateY(shape.getTranslateY() + speedY);
			shape.setTranslateX(shape.getTranslateX() + speedX);
			accelY(1);

		}
	}

	private void bounceY() {
		double correct = (shape.getTranslateY() + size - canvas.heightProperty().doubleValue()) / speedY ;
		shape.setTranslateY(height.doubleValue()- size);
		speedY *= -e;
        speedY += 1;
        speedX += (speedX < 0 ? 1 : speedX == 0 ? 0 : -1);
        shape.setTranslateY(shape.getTranslateY() + speedY * correct);
        shape.setTranslateX(shape.getTranslateX() + speedX);
	}
	
	private void bounceX() {
        shape.setTranslateX(shape.getTranslateX() > 0 + size ? width.doubleValue() - 1 - size: 1 + size);
		speedX *= -e;
	}

	private void accelY(int acc) {
		speedY += acc;
	}

	public boolean isCollidingY() {
		width = canvas.widthProperty();
		height = canvas.heightProperty();
		if (shape.getTranslateY() + size > height.doubleValue()){
			return true;
		}
		return false;
	}
	public boolean isCollidingX() {
		width = canvas.widthProperty();
		height = canvas.heightProperty();
		if (shape.getTranslateX() + size >= width.doubleValue() || shape.getTranslateX() - size <= 0){
			return true;
		}
		return false;
	}

	public boolean isColliding(Ball other) {
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        double rad = this.getRadius() + other.getRadius();
        return other == this ? false : dx * dx + dy * dy < rad * rad;
	}

	public void setSpeed(int speedX, int speedY) {
		this.speedX = speedX;
		this.speedY = speedY;
	}

    public double getY() {
        return shape.getTranslateY();
    }

    public double getX() {
        return shape.getTranslateX();
    }

    public double getRadius() {
        return shape.getRadius();
    }
}
