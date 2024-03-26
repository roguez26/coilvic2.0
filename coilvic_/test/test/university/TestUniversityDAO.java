/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test.university;

import mx.fei.coilvic.logic.university.University;
import mx.fei.coilvic.logic.university.UniversityDAO;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
/**
 *
 * @author ivanr
 */
public class TestUniversityDAO {
    
    public TestUniversityDAO() {
        
    }
    
    @Test
    public void testInsertUniversitySuccess() {
        UniversityDAO instanceDAO = new UniversityDAO();
        University instance = new University();
        int result = 0;
        int expected = 1;
        
        instance.setName("Universidad Veracruzana");
        instance.setCity("Xalapa");
        instance.setJurisdiction("Veracruz");
        instance.setIdCountry(116);
        
        result = instanceDAO.insertUniversity(instance);
        assertEquals(expected, result);
    }
    
    @Test
    public void testUpdateUniversitySuccess() {
        UniversityDAO instanceDAO = new UniversityDAO();
        University instance = new University();
        int result = 0;
        int expected = 1;
        
        instance.setName("Universidad Veracruzana");
        instance.setCity("Xalapa");
        instance.setJurisdiction("Veracruz");
        instance.setIdCountry(116);
        instance.setIdUniversity(1);
        
        result = instanceDAO.updateUniversity(instance);
        assertEquals(expected, result);
    }
    
    @Test
    public void testDeleteUniversitySuccess() {
        UniversityDAO instanceDAO = new UniversityDAO();
        University instance = new University();
        int result = 0;
        int expected = 1;
        
        instance.setName("Universidad Veracruzana");
        instance.setCity("Xalapa");
        instance.setJurisdiction("Veracruz");
        instance.setIdCountry(116);
        instance.setIdUniversity(1);
        
        result = instanceDAO.deleteUniversity(instance);
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetAllUniversities() {
        UniversityDAO instanceDAO = new UniversityDAO();
        University instance = new University();
        ArrayList<University> expected = new ArrayList<>();
        ArrayList<University> result = new ArrayList<>();
       
        instance.setName("Universidad Veracruzana");
        instance.setCity("Xalapa");
        instance.setJurisdiction("Veracruz");
        instance.setIdCountry(116);
        instance.setIdUniversity(2);
        expected.add(instance);
        result = instanceDAO.getAllUniversities();
        
        assertEquals(result, expected);
        
    }
}
