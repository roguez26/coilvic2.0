package mx.fei.coilvicapp.logic.academicarea;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public interface IAcademicArea {
    
    public int registerAcademicArea(AcademicArea academicArea) throws DAOException;
    public int updateAcademicArea(AcademicArea newAcademicAreaInformation) throws DAOException;
    public int deleteAcademicArea(int idAcademicArea) throws DAOException;
    public AcademicArea getAcademicAreaByName(String academicAreaName) throws DAOException;
    public AcademicArea getAcademicAreaById(int idAcademicArea) throws DAOException;
    public ArrayList<AcademicArea> getAcademicAreas() throws DAOException;  
    
}
