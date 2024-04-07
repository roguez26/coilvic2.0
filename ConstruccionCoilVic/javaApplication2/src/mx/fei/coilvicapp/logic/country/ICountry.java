package mx.fei.coilvicapp.logic.country;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;
public interface ICountry {
    
    public ArrayList<Country> getAllCountries() throws DAOException;
    public Country getCountryById(int idCountry) throws DAOException;
    
}
