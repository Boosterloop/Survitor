package Model.visitor;

/**
 * Created on 06.06.19.
 *
 * @author Max
 */
public interface Visitable {

    void accept (Visitor visitor);
}
