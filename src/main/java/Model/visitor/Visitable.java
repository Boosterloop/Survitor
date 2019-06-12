package Model.visitor;

/**
 * Visitable interface to define the "accept" method
 */
public interface Visitable {
    /**
     * Accept a visitor
     * @param visitor to accept
     */
    void accept(Visitor visitor);
}
