import bagel.util.Point;
import bagel.util.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A route for an entity to traverse in, in the form of a series of Points.
 */
// routes for enemies to go through
// each enemy will store one route to track progress
public class Route implements Cloneable{
    private ArrayList<Point> points;
    private Iterator<Point> pointsIterator;
    private Point currentTarget;

    /**
     * Instantiates a new Route.
     *
     * @param points the series of points that makes up the route.
     */
    public Route(ArrayList<Point> points) {
        this.points = points;
        pointsIterator = this.points.iterator();
        currentTarget = pointsIterator.next();
    }

    /**
     * Instantiates a new Route.
     *
     * @param points the series of points that makes up the route.
     */
    public Route(Point[] points) {
        this.points = new ArrayList<>(Arrays.asList(points));
        pointsIterator = this.points.iterator();
        currentTarget = pointsIterator.next();
    }

    /**
     * Instantiates a new Route that is already traversed up to a certain point.
     *
     * @param points        the series of points.
     * @param currentTarget the current target point in the route.
     */
    public Route(@NotNull ArrayList<Point> points, Point currentTarget) {
        this.points = points;
        pointsIterator = this.points.iterator();
        // iterate the iterator until the currentTarget
        for (Point point : points){
            if(point.equals(currentTarget)){
               break;
            }
            pointsIterator.next();
        }
        this.currentTarget = currentTarget;
    }

    /**
     * Copy route.
     *
     * @return the route
     */
// returns a duplicate of this route
    public Route copy(){
        return new Route(this.points, this.currentTarget);
    }

    /**
     * Gets the current target point
     *
     * @return the current target point in the series of points
     */
    public Point getCurrentTarget() {
        return currentTarget;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    /**
     * Calculates and returns the next target point in the route.
     *
     * @param current the current coordinates of the entity traversing the route.
     * @param spd     the speed of the entity traversing the route.
     * @return the target point.
     */
// returns a point that indicates where the enemy will be at the next frame
    // spd = speed of enemy, in px/frame
    public Point next(Point current, double spd){
        double distanceLeft = spd;
        Vector2 currentVector;
        Vector2 targetVector;
        Vector2 newVector;
        while (distanceLeft > 0){
            currentVector = current.asVector();
            targetVector = currentTarget.asVector();

            if(current.distanceTo(currentTarget) >= distanceLeft){
                newVector = targetVector.sub(currentVector);
                newVector = newVector.normalised();
                newVector = newVector.mul(distanceLeft);
                newVector = currentVector.add(newVector);
                return newVector.asPoint();
            }
            else{ // need to get a new target
                // cannot normalise (0,0) so need to manually skip this case
                if(!currentVector.equals(targetVector)){
                    double distanceCanCover = current.distanceTo(currentTarget);
                    double remainingDistance = distanceLeft - distanceCanCover;
                    newVector = targetVector.sub(currentVector);
                    newVector = newVector.normalised();
                    newVector = newVector.mul(distanceCanCover);
                    newVector = currentVector.add(newVector);
                    current = newVector.asPoint();
                    distanceLeft = remainingDistance;
                }
                if (!pointsIterator.hasNext()){return null;}
                currentTarget = pointsIterator.next();
            }
        }
        return null;
    }
}
