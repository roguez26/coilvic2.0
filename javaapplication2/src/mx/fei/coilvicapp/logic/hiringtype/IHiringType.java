package mx.fei.coilvicapp.logic.hiringtype;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.List;

public interface IHiringType {
    
    public int insertHiringType(HiringType hiringType) throws DAOException;
    public int updateHiringType(String newHiringType, String hiringTypeName) throws DAOException;
    public int deleteHiringType(String hiringTypeName) throws DAOException;
    public List<String> getHiringTypes() throws DAOException;
    
}
