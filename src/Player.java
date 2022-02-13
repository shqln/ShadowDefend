import org.jetbrains.annotations.NotNull;

/**
 * The player, keeps track of the player's money and health
 */
// singleton
// keeps track of money and health
public class Player {
    private static Player player;

    private static final int DEFAULT_HP = 25;
    private static final int DEFAULT_MONEY = 1000;

    private int hp;
    private int money;

    private Player(){
        this.hp = DEFAULT_HP;
        this.money = DEFAULT_MONEY;
    }

    /**
     * Get the Player instance.
     *
     * @return the instance of Player
     */
    public static Player getPlayer(){
        if(player == null){
            player = new Player();
        }
        return player;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Adds or removes an amount of money specified, from the player.
     *
     * @param amount the amount to be added, can be negative.
     */
    public void transaction(int amount) {
        int currentMoney = getMoney();
        int newMoney = currentMoney + amount;
        if(newMoney > 0){
            setMoney(newMoney);
        }
        // new amount cannot be less than 0
        else{
            setMoney(0);
        }
    }

    /**
     * Checks if the player can afford a given tower
     *
     * @param tower the tower to be checked
     * @return true if the player has enough money, false if otherwise.
     */
    // checks if the player can afford a given tower
    public boolean canAfford(@NotNull Tower tower){
        return getMoney() >= tower.getPrice();
    }

    public int getHP() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Deducts a certain amount of HP from the player.
     *
     * @param damage the amount of HP to be deducted
     */
    // decreases the player's hp by a given amount
    public void damageSelf(int damage){
        int hp = getHP();
        if(hp >= damage){
            setHp(hp - damage);
        }
        // overkill
        else{
            setHp(0);
        }
    }

    /**
     * Resets the player's HP and money to the initial state.
     */
    // reverts hp and money to starting amount
    public void resetPlayer(){
        setHp(DEFAULT_HP);
        setMoney(DEFAULT_MONEY);
    }

    /**
     * Checks if the player is dead, determined by the HP.
     *
     * @return true of HP is positive,  false otherwise
     */
    public boolean isDead(){
        return getHP() <= 0;
    }
}
