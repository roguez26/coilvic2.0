package mx.fei.coilvicapp.logic.assignment;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.collaborativeproject.CollaborativeProject;
import mx.fei.coilvicapp.logic.implementations.DAOException;

/* 
 * @author d0ubl3_d
 */

public interface IAssignment {
    
    public int registerAssignment(Assignment assignment, CollaborativeProject collaborativeProject) throws DAOException;
    public ArrayList<Assignment> getAssignmentsByIdProjectColaborative(int idColaborativeProject) throws DAOException;
    public int updateAssignment(Assignment assignment, CollaborativeProject collaborativeProject) throws DAOException;
    public int deleteAssignment(int idAssignment, CollaborativeProject collaborativeProject) throws DAOException;
   
}
