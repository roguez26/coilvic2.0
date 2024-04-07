package mx.fei.coilvicapp.logic.academicarea;

import java.sql.SQLException;
import java.util.List;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.academicarea.AcademicArea;

public interface IAcademicArea {
    
    public int insertAcademicArea(AcademicArea academicArea) throws DAOException;
    public int updateAcademicArea(String newAcademicArea, String academicAreaName) throws DAOException;
    public int deleteAcademicArea(String academicAreaName) throws DAOException;
    public List<String> getAcademicAreas() throws DAOException;    
    
}
