/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit.test.InstitutionalRepresentativeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.country.Country;
import mx.fei.coilvicapp.logic.country.CountryDAO;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvicapp.logic.university.University;
import mx.fei.coilvicapp.logic.university.UniversityDAO;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author ivanr
 */
public class InstitutionalRepresentativeDeleteTest {
    
    private static final InstitutionalRepresentativeDAO REPRESENTATIVE_DAO = new InstitutionalRepresentativeDAO();
    private static final InstitutionalRepresentative REPRESENTATIVE_FOR_TESTING = new InstitutionalRepresentative();
    private final String NAME = "Javier";
    private final String PATERNAL_SURNAME = "Hernandez";
    private final String MATERNAL_SURNAME = "Hernandez";
    private final String PHONE_NUMBER = "2293445226";
    private final String EMAIL = "javier@gmail.com";
    
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY = new University();
    private static final String AUX_NAME = "Universidad Católica Andrés Bello";
    private static final String AUX_ACRONYM = "UCAB";
    private static final String AUX_JURISDICTION = "Caracas";
    private static final String AUX_CITY = "Guayana";
    
    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();
    private static final String AUX_COUNTRY_NAME = "Mexico";
    
    public InstitutionalRepresentativeDeleteTest() {
        
    } 
    
    @Before
    public void setUp() {
        int idRepresentative;
        int idCountry;
        int idUniversity;
        
        AUX_COUNTRY.setName(AUX_COUNTRY_NAME);
        intitliazeAuxiliarUniversity();
        initializeRepresentative();
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            AUX_UNIVERSITY.setIdCountry(idCountry);
            idUniversity = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY);
            AUX_UNIVERSITY.setIdUniversity(idUniversity);
            REPRESENTATIVE_FOR_TESTING.setIdUniversity(idUniversity);
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(idRepresentative);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    private void initializeRepresentative() {
        REPRESENTATIVE_FOR_TESTING.setName(NAME);
        REPRESENTATIVE_FOR_TESTING.setPaternalSurname(PATERNAL_SURNAME);
        REPRESENTATIVE_FOR_TESTING.setMaternalSurname(MATERNAL_SURNAME);
        REPRESENTATIVE_FOR_TESTING.setPhoneNumber(PHONE_NUMBER);
        REPRESENTATIVE_FOR_TESTING.setEmail(EMAIL);
    }
    
    private void intitliazeAuxiliarUniversity() {
        AUX_UNIVERSITY.setName(AUX_NAME);
        AUX_UNIVERSITY.setAcronym(AUX_ACRONYM);
        AUX_UNIVERSITY.setJurisdiction(AUX_JURISDICTION);
        AUX_UNIVERSITY.setCity(AUX_CITY);
    }
    
    @Test
    public void testDeleteInstitutionalRepresentativeSuccess() {
        int result = 0;
        try {
            result = REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeUpdateTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(result > 0);
    }
    
    @After
    public void tearDown() {
        try {
            REPRESENTATIVE_DAO.deleteInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY.getIdUniversity());
            COUNTRY_DAO.deleteCountry(AUX_COUNTRY.getIdCountry());
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
}
