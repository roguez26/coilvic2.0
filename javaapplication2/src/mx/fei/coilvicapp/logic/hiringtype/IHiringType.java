package mx.fei.coilvicapp.logic.hiringtype;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public interface IHiringType {
    
    public boolean isThereAtLeastOneHiringType() throws DAOException;
    public int registerHiringType(HiringType hiringType) throws DAOException;
    public int updateHiringType(HiringType newHiringTypeInformation) throws DAOException;
    public int deleteHiringType(int idHiringType) throws DAOException;
    public HiringType getHiringTypeByName(String hiringTypeName) throws DAOException;
    public HiringType getHiringTypeById(int IdHiringType) throws DAOException;
    public ArrayList<HiringType> getHiringTypes() throws DAOException;
    
}
