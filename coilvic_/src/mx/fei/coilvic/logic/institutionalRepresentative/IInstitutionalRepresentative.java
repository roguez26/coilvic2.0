/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.fei.coilvic.logic.institutionalRepresentative;
import java.util.ArrayList;
/**
 *
 * @author ivanr
 */
public interface IInstitutionalRepresentative {
    public int insertInstitutionalRepresentative(InstitutionalRepresentative instutionalRepresentative);
    
    public int updateInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative);
    
    public int deleteInstitutionalRepresentative(InstitutionalRepresentative institutionalRepresentative);
    
    public ArrayList<InstitutionalRepresentative> getAllInstitutionalRepresentatives();
}
