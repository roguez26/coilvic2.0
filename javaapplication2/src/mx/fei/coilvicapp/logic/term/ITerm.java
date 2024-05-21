package mx.fei.coilvicapp.logic.term;

import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.ArrayList;

/*
 * @author d0ubl3_d
 */
public interface ITerm {
    
    
    public int registerTerm(Term term) throws DAOException;
    public int updateTerm(Term term) throws DAOException;
    public int deleteTerm(int idTerm) throws DAOException;
    public Term getTermByName(String termName) throws DAOException;
    public Term getTermByIdTerm(int idTerm) throws DAOException;
    public ArrayList<Term> getTerms() throws DAOException;
    
}
