
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.fei.coilvicapp.logic.student.*;
import org.junit.Assert;
import org.junit.Test;
import mx.fei.coilvicapp.logic.implementations.DAOException;

public class StudentTest {

    @Test
    public void testSuccessInsertStudent() {
        Student student = new Student();
        StudentDAO studentDao = new StudentDAO();

        student.setName("Miguel");
        student.setPaternalSurName("Gomez");
        student.setMaternalSurName("Canuas");
        student.setEmail("mgc@gmail.com");
        student.setGender("Hombre");
        student.setLineage("Mexicano");

        System.out.print("insertStudent success");
        try {
            Assert.assertEquals(1, studentDao.insertStudent(student));
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    @Test
    public void testFailureInsertStudent() {
        Student student = new Student();
        StudentDAO studentDao = new StudentDAO();

        student.setName("Miguel");
        student.setPaternalSurName("Gomez");
        student.setMaternalSurName("Canuas");
        student.setEmail("mgc@gmail.com");
        student.setGender("Hombre");

        System.out.print("insertStudent Failure");
        try {
            Assert.assertEquals(1, studentDao.insertStudent(student));
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    @Test
    public void testSuccessGetStudentsByEmail() {
        System.out.print("GetStudentsByEmail Success");
        StudentDAO studentDao = new StudentDAO();
        try {
            Assert.assertNotNull(studentDao.getStudentByEmail("mgc@gmail.com"));
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

    @Test
    public void testFailureGetStudentsByEmail() {
        System.out.print("GetStudentsByEmail Failure");
        StudentDAO studentDao = new StudentDAO();
        try {
            Assert.assertNotNull(studentDao.getStudentByEmail("2"));
        } catch (DAOException exception) {
            Logger.getLogger(StudentTest.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
