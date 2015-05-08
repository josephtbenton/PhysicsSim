package application;

import javafx.scene.shape.Rectangle;
import junit.framework.TestCase;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Joseph on 5/8/2015.
 */
public class QuadTreeTest extends TestCase{
    QuadTree quad = new QuadTree(0, new Rectangle(0, 0, 100, 100));

    @Test
    public void testDepth() {
        quad.clear();
        assertTrue(quad.isEmpty());
        for (int i = 0 ; i < 180 ; i++) {
            quad.insert(new Ball(1, 5, 1, 1));
            System.out.println(quad.depth() / 5 + " " + i / 30);
            assertEquals(quad.depth() / 5, i / 30);
        }

    }

}