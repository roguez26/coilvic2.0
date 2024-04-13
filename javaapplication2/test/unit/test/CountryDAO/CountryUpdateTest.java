/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit.test.CountryDAO;

import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivanr
 */
public class CountryUpdateTest {

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country COUNTRY_FOR_TESTING = new Country();
    private static final String NAME = "España";

    private static final Country AUX_COUNTRY_FOR_TESTING = new Country();
    private static final String AUX_NAME = "Colombia";

    public CountryUpdateTest() {

    }

    @Before
    public void setUp() {
        int idCountry;
        
        COUNTRY_FOR_TESTING.setName(NAME);
        AUX_COUNTRY_FOR_TESTING.setName(AUX_NAME);
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY_FOR_TESTING);
            AUX_COUNTRY_FOR_TESTING.setIdCountry(idCountry);
            idCountry = COUNTRY_DAO.registerCountry(COUNTRY_FOR_TESTING);
            COUNTRY_FOR_TESTING.setIdCountry(idCountry);
        } catch (DAOException exception) {
            Logger.getLogger(CountryUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    @Test
    public void testUpdateCountrySuccess() {
        int result = 0;
        String newName = "Espana";
        
        COUNTRY_FOR_TESTING.setName(newName);
        try {
            result = COUNTRY_DAO.updateCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(CountryUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }
    
    @Test
    public void testUpdateCountryFailByDuplicatedName() {
        int result = 0;
        String newName = AUX_NAME;
        
        COUNTRY_FOR_TESTING.setName(newName);
        try {
            result = COUNTRY_DAO.updateCountry(COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(CountryUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }

    @After
    public void tearDown() {
        try {
            COUNTRY_DAO.deleteCountry(COUNTRY_FOR_TESTING);
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(CountryRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
