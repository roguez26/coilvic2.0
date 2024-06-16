package unit.test.FileManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import log.Log;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FileManagerTest {

    private final FileManager FILE_MANAGER_FOR_TEST = new FileManager();

    private File initializeLargeFile() throws IOException {
        File largeFile = File.createTempFile("LargeFile", ".txt");
      
        try (FileWriter writer = new FileWriter(largeFile)) {
            StringBuilder data = new StringBuilder();
            for (int i = 0; i < 1000000; i++) {
                data.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ");
            }
            writer.write(data.toString());
            
        } catch (IOException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        }

        return largeFile;
    }

    @Test
    public void testSaveAssignment() {
        int idCollaborativeProjectExample = 1234;
        String expectedDestionationPath = "files\\activities" + "\\" + idCollaborativeProjectExample
                + "\\certificate.pdf";
        String result = "";
        File file = new File("files\\template\\certificate.pdf");

        try {
            result = FILE_MANAGER_FOR_TEST.saveAssignment(file, idCollaborativeProjectExample);
        } catch (IOException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        }
        assertEquals(expectedDestionationPath, result);
        File savedFile = new File(result);
        System.out.println(savedFile);
        try {
            FILE_MANAGER_FOR_TEST.deleteFile(savedFile);
        } catch (IOException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        } 
    }
    
    @Test
    public void testSaveSyllabus() {
        int idCollaborativeProjectRequestExample = 1234;
        String expectedDestionationPath = "files\\syllabus" + "\\" + idCollaborativeProjectRequestExample
                + "\\certificate.pdf";
        String result = "";
        File file = new File("files\\template\\certificate.pdf");

        try {
            result = FILE_MANAGER_FOR_TEST.saveSyllabus(file, idCollaborativeProjectRequestExample);
        } catch (IOException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        }
        assertEquals(expectedDestionationPath, result);
        File savedFile = new File(result);
        System.out.println(savedFile);
        try {
            FILE_MANAGER_FOR_TEST.deleteFile(savedFile);
        } catch (IOException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        } 
    }
    
    @Test
    public void testSaveSyllabusFailByAllreadyFileAlready() {
        int idCollaborativeProjectRequestExample = 1234;
        String result = "";
        File file = new File("files\\template\\certificate.pdf");

        try {
            result = FILE_MANAGER_FOR_TEST.saveSyllabus(file, idCollaborativeProjectRequestExample);
        } catch (IOException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        }
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, ()-> FILE_MANAGER_FOR_TEST.saveSyllabus(file, idCollaborativeProjectRequestExample));
        System.out.println(exception.getMessage());
        File savedFile = new File(result);
        try {
            FILE_MANAGER_FOR_TEST.deleteFile(savedFile);
        } catch (IOException ioException) {
            Log.getLogger(FileManagerTest.class).error(ioException.getMessage(), ioException);
        } 
    }
    
    @Test
    public void testSaveAssignmentFailByAllreadyFileAlready() {
        int idCollaborativeProjectExample = 1234;
        String result = "";
        File file = new File("files\\template\\certificate.pdf");

        try {
            result = FILE_MANAGER_FOR_TEST.saveAssignment(file, idCollaborativeProjectExample);
        } catch (IOException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        }
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class, ()-> FILE_MANAGER_FOR_TEST.saveAssignment(file, idCollaborativeProjectExample));
        System.out.println(exception.getMessage());
        File savedFile = new File(result);
        try {
            FILE_MANAGER_FOR_TEST.deleteFile(savedFile);
        } catch (IOException ioException) {
            Log.getLogger(FileManagerTest.class).error(ioException.getMessage(), ioException);
        } 
    }

    @Test
    public void isValidForSaveSucces() {
        boolean result = false;
        
        try {
            result = FILE_MANAGER_FOR_TEST.isValidFileForSave(new File("files\\template\\certificate.pdf"));
        } catch (IllegalArgumentException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        }
        System.out.println(result);
        assertTrue(result);
    }

    @Test
    public void isValidForSaveFailByTooLarge() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                FILE_MANAGER_FOR_TEST.isValidFileForSave(initializeLargeFile()));
       
        System.out.println(exception.getMessage());
    }

    @Test
    public void isValidForSaveFailByNullFile() {
         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                 FILE_MANAGER_FOR_TEST.isValidFileForSave(null));
        System.out.println(exception.getMessage());
    }

    @Test
    public void isValidForSaveFailByExistenceFile() {
        File file = new File("certificateEjemploArchivoNoExistente.pdf");
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->
                FILE_MANAGER_FOR_TEST.isValidFileForSave(file));
        System.out.println(exception.getMessage());
        
    }
    
    @Test
    public void testOpenFileSucess() {
        try {
            FILE_MANAGER_FOR_TEST.openFile("files\\template\\certificate.pdf");
        } catch (IOException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        }
    }
    
    @Test
    public void testOpenFileFailByNonexistenceFile() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                FILE_MANAGER_FOR_TEST.openFile("archivo no existente"));
        System.out.println(exception.getMessage());
    }

}
