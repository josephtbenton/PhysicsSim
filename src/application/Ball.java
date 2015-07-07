package application;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
	Circle shape;
	int mass;
	double pressure;
	double speedX;
	double speedY;
	double size;
	double e;
	double presCoef;
	private ReadOnlyDoubleProperty height, width;
    Pane canvas;


	public Ball (double size, int mass, double x, double y) {
		shape = new Circle(0, 0, size, new Color(1 - .01, 0, 0, 1));
		shape.setTranslateX(x);
		shape.setTranslateY(y);
		this.size = size;
		this.mass = mass;
		this.speedX = (int)(Math.random()*30 - 15);
		this.speedY = 0;
		this.e = .65;
		presCoef = .15;
		pressure = 1;
	}

	public void addTo(Pane canvas) {
        shape.setStroke(Color.BLACK);
		this.canvas = canvas;
        canvas.getChildren().add(shape);
	}


    public void resolveCollision(Ball other) {
        double damp = .1;
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        double dvx = other.speedX - this.speedX;
        double dvy = other.speedY - this.speedY;
        double dotProduct = dx * dvx + dy * dvy;
        if (dotProduct > 0) {
            double angle = Math.atan2(dy, dx);
            double minDist = this.getRadius() + other.getRadius();
            double targetX = this.getX() + Math.cos(angle) * minDist;
            double targetY = this.getY() + Math.sin(angle) * minDist;
            double ax = (targetX - other.getX()) * e * damp;
            double ay = (targetY - other.getY()) * e * damp;
            this.speedX += ax;
            this.speedY += ay;
            other.speedX -= ax;
            other.speedY -= ay;
        }
    }

	private void dispPressure() {
        shape.setFill(new Color((1 - .01) / (pressure * pressure) + .01, 0, 0, 1));
	}

	public void move() {
        dispPressure();
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
        speedY += 1 ;
        speedX += (speedX < 0 ? 1 : speedX == 0 ? 0 : -1);
        shape.setTranslateY(shape.getTranslateY() + speedY * correct);
        shape.setTranslateX(shape.getTranslateX() + speedX);
	}
	
	private void bounceX() {
        shape.setTranslateY(shape.getTranslateY() + speedY);
        shape.setTranslateX(shape.getTranslateX() > 0 + size ? width.doubleValue() - 1 - size : 1 + size);
		speedX *= -e;
	}

	private void accelY(int acc) {
		speedY += acc;
	}

	public boolean isCollidingY() {
		width = canvas.widthProperty();
		height = canvas.heightProperty();
		if (shape.getTranslateY() + size > height.doubleValue()){
			pressure += presCoef;
			return true;
		}
		return false;
	}
	public boolean isCollidingX() {
		width = canvas.widthProperty();
		height = canvas.heightProperty();
		if (shape.getTranslateX() + size >= width.doubleValue() || shape.getTranslateX() - size <= 0){
			pressure += presCoef;
			return true;
		}
		return false;
	}

	public boolean isColliding(Ball other) {
        double dx = this.getX() - other.getX();
        double dy = this.getY() - other.getY();
        double rad = this.getRadius() + other.getRadius();
        if((other != this) && (((dx * dx) + (dy * dy)) < (rad * rad))) {

			pressure += presCoef;
			return true;
		}
		return false;
	}

	public void setSpeed(double speedX, double speedY) {
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

    public void resetPressure() {
        pressure = 1;
    }
}
