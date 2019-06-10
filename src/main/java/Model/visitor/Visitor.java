package Model.visitor;

import Model.element.Hole;
import Model.element.Wizard;
import Model.element.Pokemon;

public interface Visitor{
    /**
     * Méthode pour réaliser la visite sur l'ếlément wizard
     * @param wizard
     * */
    void visit(Wizard wizard);

    /**
     * Méthode pour réalisé la visite sur l'élément hole
     * @param hole
     * */
    void visit(Hole hole);

    /**
     * Méthode pour réalisé la visite sur l'élément pokemon
     * @param pokemon
     * */
    void visit(Pokemon pokemon);

}
