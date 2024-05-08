package mx.fei.coilvicapp.logic.region;

import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.ArrayList;

public interface IRegion {
    
    public int registerRegion(Region region) throws DAOException;
    public int updateRegion(Region newRegionInformation) throws DAOException;
    public int deleteRegion(int idRegion) throws DAOException;
    public Region getRegionByName(String regionName) throws DAOException;
    public Region getRegionById(int idRegion) throws DAOException;
    public ArrayList<Region> getRegions() throws DAOException; 
            
}
