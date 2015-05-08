package application;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class QuadTree {

    private int MAX_OBJECTS = 30;
    private int MAX_LEVELS = 6;

    private int level;
    private ArrayList<Ball> objects;
    private Rectangle bounds;
    private QuadTree[] nodes;

    public QuadTree(int level, Rectangle bounds) {
        this.level = level;
        objects = new ArrayList<>();
        this.bounds = bounds;
        nodes = new QuadTree[4];
    }

    public void clear() {
        objects.clear();
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    private void split() {
        int subWidth = (int)(bounds.getWidth() / 2);
        int subHeight = (int)(bounds.getHeight() / 2);
        int x = (int)bounds.getX();
        int y = (int)bounds.getY();

        nodes[0] = new QuadTree(level+1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new QuadTree(level+1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new QuadTree(level+1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new QuadTree(level+1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    private int getIndex(Ball ball) {
        int index = -1;
        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        boolean topQuadrant = (ball.getY() < horizontalMidpoint && ball.getY() + 2 * ball.getRadius() < horizontalMidpoint);
        boolean bottomQuadrant = (ball.getY() > horizontalMidpoint);

        if (ball.getX() < verticalMidpoint && ball.getX() + 2 * ball.getRadius() < verticalMidpoint) {
            if (topQuadrant) {
                index = 1;
            }
            else if (bottomQuadrant) {
                index = 2;
            }
        }
        else if (ball.getX() > verticalMidpoint) {
            if (topQuadrant) {
                index = 0;
            }
            else if (bottomQuadrant) {
                index = 3;
            }
        }

        return index;
    }

    public void insert(Ball ball) {
        if (nodes[0] != null) {
            int index = getIndex(ball);

            if (index != -1) {
                nodes[index].insert(ball);

                return;
            }
        }

        objects.add(ball);

        if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size()) {
                int index = getIndex(objects.get(i));
                if (index != -1) {
                    nodes[index].insert(objects.remove(i));
                }
                else {
                    i++;
                }
            }
        }
    }

    public ArrayList<Ball> retrieve(ArrayList<Ball> returnObjects, Ball ball) {
        int index = getIndex(ball);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(returnObjects, ball);
        }

        returnObjects.addAll(objects);

        return returnObjects;
    }

    public boolean isEmpty() {
        return level == 0 && nodes[0] == null && objects.isEmpty();
    }

    public int depth() {
        QuadTree quad = this;
        if (nodes[0] == null) {
            return level;
        }
        return Math.max(Math.max(Math.max(nodes[0].depth(), nodes[1].depth()), nodes[2].depth()), nodes[3].depth());
    }
}
