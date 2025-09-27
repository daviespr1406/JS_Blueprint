
package edu.eci.arsw.blueprints.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/**
 *
 * @author hcadavid
 */

public class Blueprint {
    private String author;
    private String name;
    private Point[] points;

    public Blueprint(String author, String name, Point[] points) {
        this.author = author;
        this.name = name;
        this.points = points;
    }

    public String getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public List<Point> getPoints() {
        return List.of(points);
    }

    @Override
    public String toString() {
        return "Blueprint{" +
                "author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", points=" + Arrays.toString(points) +
                '}';
    }
}
