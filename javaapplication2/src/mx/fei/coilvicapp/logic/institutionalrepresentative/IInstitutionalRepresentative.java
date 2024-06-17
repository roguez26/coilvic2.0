package mx.fei.coilvicapp.logic.institutionalrepresentative;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public interface IInstitutionalRepresentative {

    public int registerInstitutionalRepresentative(InstitutionalRepresentative instutionalRepresentative) throws DAOException;

    public int updateInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative) throws DAOException;

    public int deleteInstitutionalRepresentative(InstitutionalRepresentative instutionalRepresentative) throws DAOException;
    
    public ArrayList<InstitutionalRepresentative> getAllInstitutionalRepresentatives() throws DAOException;
    
    public InstitutionalRepresentative getInstitutionalRepresentativeByEmail(String instutionalRepresentativeEmail) throws DAOException;
}
