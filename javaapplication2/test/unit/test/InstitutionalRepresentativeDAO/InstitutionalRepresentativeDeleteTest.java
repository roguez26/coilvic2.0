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
    
    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University AUX_UNIVERSITY = new University();

    private static final CountryDAO COUNTRY_DAO = new CountryDAO();
    private static final Country AUX_COUNTRY = new Country();
    
    public InstitutionalRepresentativeDeleteTest() {
        
    } 
    
    @Before
    public void setUp() {
        int idRepresentative;
        int idCountry;
        int idUniversity;
        
        initializeAuxiliarCountry();
        intitliazeAuxiliarUniversity();
        initializeInstitutionalRepresentative();
        
        try {
            idCountry = COUNTRY_DAO.registerCountry(AUX_COUNTRY);
            AUX_COUNTRY.setIdCountry(idCountry);
            AUX_UNIVERSITY.setCountry(AUX_COUNTRY);
            idUniversity = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY);
            AUX_UNIVERSITY.setIdUniversity(idUniversity);
            REPRESENTATIVE_FOR_TESTING.setUniversity(AUX_UNIVERSITY);
            idRepresentative = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(idRepresentative);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeDAOGettersTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    private void initializeAuxiliarCountry() {
        AUX_COUNTRY.setName("Mexico");
    }
    
    private void initializeInstitutionalRepresentative() {
        REPRESENTATIVE_FOR_TESTING.setName("Carlos");
        REPRESENTATIVE_FOR_TESTING.setPaternalSurname("Oliva");
        REPRESENTATIVE_FOR_TESTING.setMaternalSurname("Ramirez");
        REPRESENTATIVE_FOR_TESTING.setPhoneNumber("2297253222");
        REPRESENTATIVE_FOR_TESTING.setEmail("Carlos@gmail.com");
    }
    
    private void intitliazeAuxiliarUniversity() {
        AUX_UNIVERSITY.setName("Universidad Veracruzana");
        AUX_UNIVERSITY.setAcronym("UV");
        AUX_UNIVERSITY.setJurisdiction("Veracruz");
        AUX_UNIVERSITY.setCity("Xalapa");
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
