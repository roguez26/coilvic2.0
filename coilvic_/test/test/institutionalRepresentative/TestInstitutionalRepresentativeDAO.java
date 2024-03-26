/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test.institutionalRepresentative;
import mx.fei.coilvic.logic.institutionalRepresentative.InstitutionalRepresentativeDAO;
import mx.fei.coilvic.logic.institutionalRepresentative.InstitutionalRepresentative;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author ivanr
 */
public class TestInstitutionalRepresentativeDAO { 
    public TestInstitutionalRepresentativeDAO() {
        
    }
    
    @Test
    public void testSuccessfulInsertInstitutionalRepresentative() {
        InstitutionalRepresentativeDAO instanceDAO = new InstitutionalRepresentativeDAO();
        InstitutionalRepresentative instance = new InstitutionalRepresentative();
        int result = 0;
        int expected = 1;
        
        instance.setName("German");
        instance.setPaternalSurname("Martinez");
        instance.setMaternalSurname("Gomez");
        instance.setEmail("german232@gmail.com");
        instance.setPhoneNumber("2293027384");
        instance.setIdUniversity(1);
        
        result = instanceDAO.insertInstitutionalRepresentative(instance);
        assertEquals(result, expected);
    } 
    
    @Test
    public void testUpdateInstitutionalRepresentativeSuccess() {
        InstitutionalRepresentativeDAO instanceDAO = new InstitutionalRepresentativeDAO();
        InstitutionalRepresentative instance = new InstitutionalRepresentative();
        int result = 0;
        int expected = 1;
        
        instance.setIdInstitutionalRepresentative(2);
        instance.setName("German");
        instance.setPaternalSurname("Martinez");
        instance.setMaternalSurname(null);
        instance.setEmail("german232@gmail.com");
        instance.setPhoneNumber("2293027384");
        instance.setIdUniversity(1);
        
        result = instanceDAO.updateInstitutionalRepresentative(instance);
        assertEquals(expected, result);
    }
    
    @Test
    public void testSuccesfulDeleteInstitutionalRepresentative() {
        InstitutionalRepresentativeDAO instanceDAO = new InstitutionalRepresentativeDAO();
        InstitutionalRepresentative instance = new InstitutionalRepresentative();
        int result = 0;
        int expected = 1;
        
        instance.setIdInstitutionalRepresentative(2);
        instance.setName("German");
        instance.setPaternalSurname("Martinez");
        instance.setMaternalSurname(null);
        instance.setEmail("german232@gmail.com");
        instance.setPhoneNumber("2293027384");
        instance.setIdUniversity(1);
        
        result = instanceDAO.deleteInstitutionalRepresentative(instance);
        assertEquals(expected, result);
    }
    
    @Test 
    public void testGetAllInstitutionalRepresentativesSuccess() {
        InstitutionalRepresentativeDAO instanceDAO = new InstitutionalRepresentativeDAO();
        InstitutionalRepresentative instance = new InstitutionalRepresentative();
        ArrayList<InstitutionalRepresentative> expected = new ArrayList<>();
        ArrayList<InstitutionalRepresentative> result = new ArrayList<>();
       
        instance.setIdInstitutionalRepresentative(2);
        instance.setName("German");
        instance.setPaternalSurname("Martinez");
        instance.setMaternalSurname(null);
        instance.setEmail("german232@gmail.com");
        instance.setPhoneNumber("2293027384");
        instance.setIdUniversity(1);
        expected.add(instance);
        result = instanceDAO.getAllInstitutionalRepresentatives();
        assertEquals(result, expected);
    }
}
