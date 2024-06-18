package mx.fei.coilvicapp.logic.modality;

import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.ArrayList;

public interface IModality {
    
    public int registerModality(Modality modality) throws DAOException;
    public int updateModality(Modality modality) throws DAOException;
    public int deleteModality(int idModality) throws DAOException;
    public Modality getModalityByName(String modalityName) throws DAOException;
    public Modality getModalityByIdModality(int idModality) throws DAOException;
    public ArrayList<Modality> getModalities() throws DAOException; 
    
}
