import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;


/**
 * A panel that renders at the top of the screen, showing the shop, information of key binds, and the player's money.
 *
 * Is singleton.
 */
// singleton
public class BuyPanel extends Sprite {
    private static BuyPanel panel;
    private static final String BACKGROUND_IMAGE_SRC = "res/images/buypanel.png";
    // for rendering towers
    private static final int NUMBER_OF_TOWERS = 3;
    private static final int LEFT_OFFSET = 64; //px
    private static final int CENTER_TOP_OFFSET = 10; // px
    private static final int HORIZONTAL_GAP = 120; //px
    // for rendering prices
    private static final int VERTICAL_GAP = 50; // px
    private static final int PRICE_RIGHT_OFFSET = 25; // px
    private static final Colour PRICE_COLOUR_YES = Colour.GREEN;
    private static final Colour PRICE_COLOUR_NO = Colour.RED;
    // for rendering key bindings
    private static final int KEY_TOP_OFFSET = 20; // px
    private static final int KEY_VERTICAL_GAP = 20; // px
    private static final int KEY_CENTER_LEFT_OFFSET = 50; // px

    // for rendering money
    private static final int MONEY_RIGHT_OFFSET = 200; //px
    private static final int MONEY_TOP_OFFSET = 65; //px

    // purchasable towers           : Tank
    private Tower[] towers;

    // initialising the buy panel
    private BuyPanel() {
        super(new Point(new Image(BACKGROUND_IMAGE_SRC).getWidth() / 2,
                new Image(BACKGROUND_IMAGE_SRC).getHeight() / 2) ,
                new Image(BACKGROUND_IMAGE_SRC));

        Image img = new Image(BACKGROUND_IMAGE_SRC);
        Point center = new Point(img.getWidth() / 2, img.getHeight() / 2);

        this.towers = new Tower[NUMBER_OF_TOWERS];
        towers[0] = new Tank(new Point(LEFT_OFFSET, center.y - CENTER_TOP_OFFSET));
        towers[1] = new SuperTank(new Point(LEFT_OFFSET + HORIZONTAL_GAP, center.y - CENTER_TOP_OFFSET));
        towers[2] = new Airplane(new Point(LEFT_OFFSET + HORIZONTAL_GAP * 2, center.y - CENTER_TOP_OFFSET));
    }

    /**
     * Get the instance of the buy panel.
     *
     * @return the instance of the buy panel
     */
    public static BuyPanel getPanel(){
        if(panel == null){
            panel = new BuyPanel();
        }
        return panel;
    }

    /**
     * Renders all the images and texts, checks the player's interaction with the shop.
     *
     * @param input the input
     * @return returns the tower the player is buying, or null if the player did not interact with the shop.
     */
    public Tower update(Input input){
        // render background
        super.render();
        // render content
        for (int i = 0; i < NUMBER_OF_TOWERS; i++) {
            towers[i].render();
        }
        // render price
        renderPrice();
        // render key bindings
        renderKeyBinds();
        // render money
        renderMoney();
        // if mouse cursor is in the panel
        Point cursor = new Point(input.getMouseX(), input.getMouseY());
        if(super.getRect().intersects(cursor)){
            Player player = Player.getPlayer();
            // checking which tower the cursor is on
            for (int i = 0; i < towers.length; i++) {
                Tower tower = towers[i];
                if (tower.getRect().intersects(cursor)) {
                    // if left click on tower and player can afford the tower
                    if (input.wasPressed(MouseButtons.LEFT) && player.canAfford(tower)) {
                        // buying tower
                        if(i == 0){
                            System.out.println("tank bought");
                            return new Tank(cursor);
                        }
                        if(i == 1){
                            System.out.println("super tank bought");
                            return new SuperTank(cursor);
                        }
                        if(i == 2){
                            System.out.println("airplane bought");
                            return new Airplane(cursor);
                        }
                    }
                }
            }
        }
        return null;
    }

    // shows the price of towers
    private void renderPrice(){
        Font priceFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 20);
        for (int i = 0; i < NUMBER_OF_TOWERS; i++) {
            Tower tower = towers[i];
            Point position = tower.getPosition();
            int price = tower.getPrice();
            DrawOptions colour = new DrawOptions().setBlendColour(PRICE_COLOUR_YES);
            // text is red if tower cannot be afforded
            if (price > Player.getPlayer().getMoney()){
                colour.setBlendColour(PRICE_COLOUR_NO);
            }
            priceFont.drawString("$" + price, position.x - PRICE_RIGHT_OFFSET,
                    position.y + VERTICAL_GAP, colour);
        }
    }

    // shows the key binds
    private void renderKeyBinds(){
        Font keyFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 15);
        double x = getRect().centre().x - KEY_CENTER_LEFT_OFFSET;
        double y = KEY_TOP_OFFSET;
        // number of keys = 3
        keyFont.drawString(ShadowDefend.KEY_TITLE, x, y);
        y += KEY_VERTICAL_GAP;
        keyFont.drawString(ShadowDefend.WAVE_START.toString() + " - " + ShadowDefend.KEY_DESCRIPTION_1,
                x, y);
        y += KEY_VERTICAL_GAP;
        keyFont.drawString(ShadowDefend.INCREASE_TIMESCALE.toString() + " - " + ShadowDefend.KEY_DESCRIPTION_2,
                x, y);
        y += KEY_VERTICAL_GAP;
        keyFont.drawString(ShadowDefend.DECREASE_TIMESCALE.toString() + " - " + ShadowDefend.KEY_DESCRIPTION_3,
                x, y);
    }

    // shows the amount of money the player has
    private void renderMoney(){
        Font moneyFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 40);
        moneyFont.drawString("$" + Player.getPlayer().getMoney(), Window.getWidth() - MONEY_RIGHT_OFFSET, MONEY_TOP_OFFSET);
    }
}
