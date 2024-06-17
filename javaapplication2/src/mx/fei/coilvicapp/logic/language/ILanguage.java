package mx.fei.coilvicapp.logic.language;

import mx.fei.coilvicapp.logic.implementations.DAOException;
import java.util.ArrayList;

public interface ILanguage {
    
    public int registerLanguage(Language language) throws DAOException;
    public int updateLanguage(Language language) throws DAOException;
    public int deleteLanguage(int idLanguage) throws DAOException;
    public Language getLanguageByName(String languageName) throws DAOException;
    public Language getLanguageByIdLanguage(int idLanguage) throws DAOException;
    public ArrayList<Language> getLanguages() throws DAOException; 
    
}
