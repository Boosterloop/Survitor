package View;

import Model.Hole;
import Model.Wizard;
import Model.Pokemon;

/**
 * Visitor interface to define the "visit" methods
 */
public interface Visitor{
    /**
     * "Visit" method for a Wizard
     * @param wizard to visit
     */
    void visit(Wizard wizard);

    /**
     * "Visit" method for a Hole
     * @param hole to visit
     */
    void visit(Hole hole);

    /**
     * "Visit" method for a Pokemon
     * @param pokemon to visit
     */
    void visit(Pokemon pokemon);
}
