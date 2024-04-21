package mx.fei.coilvicapp.logic.assignment;

import java.sql.SQLException;
import java.util.List;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/* 
 * @author d0ubl3_d
 */

public interface IAssignment {
    
    public int insertAssignment(Assignment assignment) throws DAOException;    
    public List<Assignment> getAssignmentsByIdProjectColaborative(int idColaborativeProject) throws DAOException;
    public int updateAssignment(Assignment assignment) throws DAOException;
    public int deleteAssignmentByIdAssignment(int idAssignment) throws DAOException;
}
