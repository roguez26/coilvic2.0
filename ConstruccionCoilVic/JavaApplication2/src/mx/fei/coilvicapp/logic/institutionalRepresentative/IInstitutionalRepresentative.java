package mx.fei.coilvicapp.logic.institutionalRepresentative;
import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
/**
 *
 * @author ivanr
 */
public interface IInstitutionalRepresentative {
    
    public int registerInstitutionalRepresentative(InstitutionalRepresentative instutionalRepresentative) throws DAOException;
    public int deleteInstitutionalRepresentative(InstitutionalRepresentative instutionalRepresentative) throws DAOException;
    public int updateInstitutionalRepresentative (InstitutionalRepresentative institutionalRepresentative) throws DAOException;
}
