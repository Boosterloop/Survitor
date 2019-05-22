package Model;

public interface Visitor {
    void visit(Laser laser);
    void visit(Trou trou);
    void visit(Pokemon pokemon);

}
