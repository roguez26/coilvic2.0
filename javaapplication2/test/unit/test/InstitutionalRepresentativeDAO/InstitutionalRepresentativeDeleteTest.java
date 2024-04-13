/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unit.test.InstitutionalRepresentativeDAO;

import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.implementations.DAOException;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentative;
import mx.fei.coilvicapp.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import org.junit.Before;
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
    private final int ID_UNIVERSITY = 1;
    
    public InstitutionalRepresentativeDeleteTest() {
        
    } 
    
    @Before
    public void setUp() {
        int representativeId;
        initializeRepresentative();
        
        try {
            representativeId = REPRESENTATIVE_DAO.registerInstitutionalRepresentative(REPRESENTATIVE_FOR_TESTING);
            REPRESENTATIVE_FOR_TESTING.setIdInstitutionalRepresentative(representativeId);
        } catch (DAOException exception) {
            Logger.getLogger(InstitutionalRepresentativeDeleteTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    private void initializeRepresentative() {
        REPRESENTATIVE_FOR_TESTING.setName(NAME);
        REPRESENTATIVE_FOR_TESTING.setPaternalSurname(PATERNAL_SURNAME);
        REPRESENTATIVE_FOR_TESTING.setMaternalSurname(MATERNAL_SURNAME);
        REPRESENTATIVE_FOR_TESTING.setPhoneNumber(PHONE_NUMBER);
        REPRESENTATIVE_FOR_TESTING.setEmail(EMAIL);
        REPRESENTATIVE_FOR_TESTING.setIdUniversity(ID_UNIVERSITY);
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
    
}
