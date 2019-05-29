package Model.visitor;

import Model.element.Laser;
import Model.element.Pokemon;
import Model.element.Trou;

public interface Visitor{
    /**
     * Méthode pour réaliser la visite sur l'ếlément laser
     * @param laser
     * */
    void visit(Laser laser);

    /**
     * Méthode pour réalisé la visite sur l'élément trou
     * @param trou
     * */
    void visit(Trou trou);

    /**
     * Méthode pour réalisé la visite sur l'élément pokemon
     * @param pokemon
     * */
    void visit(Pokemon pokemon);

}
