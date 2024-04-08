/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit.test.University;

import mx.fei.coilvicapp.logic.university.UniversityDAO;
import mx.fei.coilvicapp.logic.university.University;
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
public class UniversityRegistrationTest {

    private static final UniversityDAO UNIVERSITY_DAO = new UniversityDAO();
    private static final University UNIVERSITY_FOR_TESTING = new University();
    private static final String NAME = "Universidad Veracruzana";
    private static final String ACRONYM = "UV";
    private static final String JURISDICTION = "Veracruz";
    private static final String CITY = "Xalapa";
    private static final int ID_COUNTRY = 1;

    private static final University AUX_UNIVERSITY_FOR_TESTING = new University();
    private static final String AUX_NAME = "Universidad Católica Andrés Bello";
    private static final String AUX_ACRONYM = "UCAB";
    private static final String AUX_JURISDICTION = "Caracas";
    private static final String AUX_CITY = "Guayana";
    private static final int AUX_ID_COUNTRY = 2;

    private void intitliazeUniversity() {
        UNIVERSITY_FOR_TESTING.setName(NAME);
        UNIVERSITY_FOR_TESTING.setAcronym(ACRONYM);
        UNIVERSITY_FOR_TESTING.setJurisdiction(JURISDICTION);
        UNIVERSITY_FOR_TESTING.setCity(CITY);
        UNIVERSITY_FOR_TESTING.setIdCountry(ID_COUNTRY);
    }

    private void intitliazeAuxiliarUniversity() {
        AUX_UNIVERSITY_FOR_TESTING.setName(AUX_NAME);
        AUX_UNIVERSITY_FOR_TESTING.setAcronym(AUX_ACRONYM);
        AUX_UNIVERSITY_FOR_TESTING.setJurisdiction(AUX_JURISDICTION);
        AUX_UNIVERSITY_FOR_TESTING.setCity(AUX_CITY);
        AUX_UNIVERSITY_FOR_TESTING.setIdCountry(AUX_ID_COUNTRY);
    }

    @Before
    public void setUp() {
        try {
            intitliazeAuxiliarUniversity();
            intitliazeUniversity();
            int idUniversity = UNIVERSITY_DAO.registerUniversity(AUX_UNIVERSITY_FOR_TESTING);
            AUX_UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    @Test
    public void testRegisterUniveritySuccess() {
        int idUniversity = 0;
        try {
            idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
            UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }
        assertTrue(idUniversity > 0);
    }
    
    @Test
    public void testRegisterUniverityFailByNameDuplication() {
        int idUniversity = 0;
        try {
            UNIVERSITY_FOR_TESTING.setName(AUX_NAME);
            idUniversity = UNIVERSITY_DAO.registerUniversity(UNIVERSITY_FOR_TESTING);
            UNIVERSITY_FOR_TESTING.setIdUniversity(idUniversity);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
            System.out.println(exception.getMessage());
        }
        assertTrue(idUniversity > 0);
    }

    @After
    public void tearDown() {
        try {
            UNIVERSITY_DAO.deleteUniversity(UNIVERSITY_FOR_TESTING);
            UNIVERSITY_DAO.deleteUniversity(AUX_UNIVERSITY_FOR_TESTING);
        } catch (DAOException exception) {
            Logger.getLogger(UniversityRegistrationTest.class.getName()).log(Level.SEVERE, null, exception);
        }

    }

}
