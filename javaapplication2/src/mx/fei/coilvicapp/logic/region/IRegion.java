package mx.fei.coilvicapp.logic.region;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.List;

public interface IRegion {
    
    public int insertRegion(Region region) throws DAOException;
    public int updateRegion(String newRegion, String regionName) throws DAOException;
    public int deleteRegion(String regionName) throws DAOException;
    public List<String> getRegions() throws DAOException;
            
}
