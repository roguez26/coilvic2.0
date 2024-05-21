package mx.fei.coilvicapp.logic.country;

import java.util.ArrayList;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public interface ICountry {

    public ArrayList<Country> getAllCountries() throws DAOException;

    public Country getCountryById(int idCountry) throws DAOException;
    
    public int registerCountry(Country country) throws DAOException;
    
    public int updateCountry(Country country) throws DAOException;
    
    public int deleteCountry(int country) throws DAOException;

}
