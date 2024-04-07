package mx.fei.coilvicapp.logic.hiringcategory;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.sql.SQLException;
import java.util.List;

public interface IHiringCategory {
    
    public int insertHiringCategory(HiringCategory hiringCategory) throws DAOException;
    public int updateHiringCategory(String newHiringCategory, String hiringCategoryName) throws DAOException;
    public int deleteHiringCategory(String hiringCategoryName) throws DAOException;
    public List<String> getHiringCategories() throws DAOException;
    
}
