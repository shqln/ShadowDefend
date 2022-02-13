import bagel.DrawOptions;
import bagel.Drawing;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Represents a game entity
 * <p>
 * Code adapted from the sample solution of project 1,
 * originally made by Rohyl Joshi
 * https://gitlab.eng.unimelb.edu.au/swen20003-S1-2020/rohylj/rohylj-project-1/blob/master/src/lists/Sprite.java
 * implemented by Daniel Lin (Shiqi)
 *
 */
public abstract class Sprite {

    private final Image image;
    private Point position;
    private Orientation orientation;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point    The starting point for the entity
     * @param imageSrc The image which will be rendered at the entity's point
     */
    public Sprite(Point point, String imageSrc) {
        this.image = new Image(imageSrc);
        this.position = point;
        this.orientation = new Orientation();
    }

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point The starting point for the entity
     * @param image The image which will be rendered at the entity's point
     */
    public Sprite(Point point, Image image) {
        this.image = image;
        this.position = point;
        this.orientation = new Orientation();
    }


    /**
     * Gets the bounding box of the image of the sprite
     *
     * @return the bounding box
     */
    public Rectangle getRect() {
        return this.image.getBoundingBoxAt(getPosition());
    }


    public Point getPosition() {
        return position;
    }


    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Moves the Sprite by a specified delta
     *
     * @param dx The move delta vector
     */
    public void move(Vector2 dx) {
        setPosition(getPosition().asVector().add(dx).asPoint());
    }


    public Orientation getOrientation() {
        return orientation;
    }


    /**
     * Renders the hit box of the sprite as a red rectangle, defaults to the bounding box of the image.
     */
    // for debugging purposes
    // draws a rectangle, default rectangle is the hitBox
    public void drawRectangle(){
        Rectangle rect = this.getHitBox();
        Drawing.drawLine(rect.topLeft(), rect.topRight(),3,Colour.RED);
        Drawing.drawLine(rect.topLeft(), rect.bottomLeft(),3,Colour.RED);
        Drawing.drawLine(rect.topRight(), rect.bottomRight(),3,Colour.RED);
        Drawing.drawLine(rect.bottomLeft(), rect.bottomRight(),3,Colour.RED);
    }

    /**
     * Renders the hit box of the sprite as a rectangle of a given colour,
     * defaults to the bounding box of the image.
     *
     * @param colour the colour of the rectangle
     */
    // for debugging purposes
    // draws a rectangle, default rectangle is the hitBox
    public void drawRectangle(Colour colour){
        Rectangle rect = this.getHitBox();
        Drawing.drawLine(rect.topLeft(), rect.topRight(),3,colour);
        Drawing.drawLine(rect.topLeft(), rect.bottomLeft(),3,colour);
        Drawing.drawLine(rect.topRight(), rect.bottomRight(),3,colour);
        Drawing.drawLine(rect.bottomLeft(), rect.bottomRight(),3,colour);
    }

    /**
     * Renders the Sprite at its current position.
     */
    public void render(){
        this.image.draw(getPosition().x, getPosition().y, new DrawOptions().setRotation(orientation.getAngle()));
    }

    /**
     * Renders the Sprite at its current position with an orientation specified.
     *
     * @param orientation the {@link Orientation} of the sprite
     */
    public void render(@NotNull Orientation orientation){
        this.image.draw(getPosition().x, getPosition().y, new DrawOptions().setRotation(orientation.getAngle()));
    }

    /**
     * Renders the Sprite at its current position with properties specified by options.
     *
     * @param options customisations of the rendering of the image
     */
    public void render(DrawOptions options){
        this.image.draw(getPosition().x, getPosition().y, options);
    }

    /**
     * Gets the hit box of the sprite, defaults to the bounding box of the image of the sprite.
     *
     * @return the hit box.
     */
    // returns the hitBox of the sprite, default hitBox is the size of the image
    public Rectangle getHitBox(){
        return getRect();
    }

    /**
     * Generate the hit box rectangle as a square of a given size,
     *
     * @param center the center of the entity.
     * @param size   the length of the sides of the square.
     * @return the hit box rectangle.
     */
    // generates a squre hitbox based on the center location and size specified
    @NotNull
    @Contract("_, _ -> new")
    public static Rectangle generateHitBox(@NotNull Point center, int size){
        return new Rectangle(new Point(center.x - (size / 2), center.y - (size / 2)), size, size);
    }
}



