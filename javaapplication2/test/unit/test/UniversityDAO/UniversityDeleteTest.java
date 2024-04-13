/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit.test.UniversityDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author ivanr
 */
public class UniversityDeleteTest {
    
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University UNIVERSITY_FOR_TESTING = new University();
    private static final String NAME = "Universidad Veracruzana";
    private static final String ACRONYM = "UV";
    private static final String JURISDICTION = "Veracruz";
    private static final String CITY = "Xalapa";
    private static final int ID_COUNTRY = 1;
    
    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final InstitutionalRepresentative AUX_REPRESENTATIVE = new InstitutionalRepresentative();
    private static final String AUX_REPRESENTATIVE_NAME = "Carlos";
    private static final String AUX_PATERNAL_SURNAME = "Oliva";
    private static final String AUX_MATERNAL_SURNAME = "Ramirez";
    private static final String AUX_PHONE_NUMBER = "2297253222";
    private static final String AUX_EMAIL = "Carlos@gmail.com";
    
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();
    private static final String AUX_COUNTRY_NAME = "Mexico";
    
    
    @Before
    public void setUp() {
        int idUniversity = 0;
        int idInstitutionalRepresentative = 0;
        int idCountry = 0;
        
        intitliazeUniversity();
        initializeInstitutionalRepresentative();
        AUX_COUNTRY.setName(AUX_COUNTRY_NAME);
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            UNIVERSITY_FOR_TESTING.setIdCountry(idCountry);
            idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
            AUX_REPRESENTATIVE.setIdUniversity(idUniversity);
            idInstitutionalRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(AUX_REPRESENTATIVE);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        AUX_REPRESENTATIVE.setIdInstitutionalRepresentative(idInstitutionalRepresentative);
    }

    private void intitliazeUniversity() {
        UNIVERSITY_FOR_TESTING.setName(NAME);
        UNIVERSITY_FOR_TESTING.setAcronym(ACRONYM);
        UNIVERSITY_FOR_TESTING.setJurisdiction(JURISDICTION);
        UNIVERSITY_FOR_TESTING.setCity(CITY);
        UNIVERSITY_FOR_TESTING.setIdCountry(ID_COUNTRY);
    }
    
    private void initializeInstitutionalRepresentative() {
        AUX_REPRESENTATIVE.setName(AUX_REPRESENTATIVE_NAME);
        AUX_REPRESENTATIVE.setPaternalSurname(AUX_PATERNAL_SURNAME);
        AUX_REPRESENTATIVE.setMaternalSurname(AUX_MATERNAL_SURNAME);
        AUX_REPRESENTATIVE.setPhoneNumber(AUX_PHONE_NUMBER);
        AUX_REPRESENTATIVE.setEmail(AUX_EMAIL);
    }
    
    @Test
    public void testDeleteUniversitySuccess() {
        int result = 0;
        
        try {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(AUX_REPRESENTATIVE);
            result = UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }
    
    @Test
    public void testDeleteUniversityFailByDependencies() {
        int result = 0;
        
        try {
            result = UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(result > 0);
    }
    
    @After
    public void tearDown() {
        try {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(AUX_REPRESENTATIVE);
            UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING);
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
