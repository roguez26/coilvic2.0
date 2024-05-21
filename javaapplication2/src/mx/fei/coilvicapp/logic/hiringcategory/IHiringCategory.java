package mx.fei.coilvicapp.logic.hiringcategory;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public interface IHiringCategory {
    
    public int registerHiringCategory(HiringCategory hiringCategory) throws DAOException;
    public int updateHiringCategory(HiringCategory newHiringCategory) throws DAOException;
    public int deleteHiringCategory(int idHiringCategory) throws DAOException;
    public HiringCategory getHiringCategoryByName(String academicAreaName) throws DAOException;
    public ArrayList<HiringCategory> getHiringCategories() throws DAOException;  
    
}
