package Model;

public class Pokemon extends Obstacle{
    private int vitesse;

    public Pokemon (int x, int y, int vitesse){
        super(x, y);
        this.vitesse= vitesse;
    }

    public void accept (Visitor visitor){
        visitor.visit(this);
    }


}
