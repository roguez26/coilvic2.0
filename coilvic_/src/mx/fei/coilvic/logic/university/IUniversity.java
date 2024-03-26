/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.fei.coilvic.logic.university;
import java.util.ArrayList;
/**
 *
 * @author ivanr
 */
public interface IUniversity {
    public int insertUniversity(University university);
    public int updateUniversity(University university);
    public int deleteUniversity(University university);
    public ArrayList<University> getAllUniversities();
}
