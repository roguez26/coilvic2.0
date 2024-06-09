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

    private File intializeValidFile() {
        File file = new File("files\\File.txt");
        return file;
    }

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
        String expectedDestionationPath = "files\\activities" + "\\" + idCollaborativeProjectExample + "\\certificate.pdf";
        String result = "";
        File file = new File("files\\template\\certificate.pdf");

        FILE_MANAGER_FOR_TEST.setFile(file);
        FILE_MANAGER_FOR_TEST.setDestinationDirectory(idCollaborativeProjectExample);
        try {
            result = FILE_MANAGER_FOR_TEST.saveFile();
        } catch (IOException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        }
        assertEquals(expectedDestionationPath, result);
        File savedFile = new File(result);
        FILE_MANAGER_FOR_TEST.deleteFile(savedFile);
    }

    @Test
    public void isValidForSaveSucces() {
        boolean result = false;
        try {
            result = FILE_MANAGER_FOR_TEST.isValidFileForSave(intializeValidFile());
        } catch (IllegalArgumentException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        }
        assertTrue(result);
    }

    @Test
    public void isValidForSaveFailByTooLarge() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FILE_MANAGER_FOR_TEST.isValidFileForSave(initializeLargeFile()));
        System.out.println(exception.getMessage());
    }

    @Test
    public void isValidForSaveFailByNullFile() {
         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FILE_MANAGER_FOR_TEST.isValidFileForSave(null));
        System.out.println(exception.getMessage());
    }

    @Test
    public void isValidForSaveFailByExistenceFileAlready() {
        int idCollaborativeProjectExample = 1234;
        File file = new File("files\\template\\certificate.pdf");
        String destionationPath = "";

        FILE_MANAGER_FOR_TEST.setFile(file);
        FILE_MANAGER_FOR_TEST.setDestinationDirectory(idCollaborativeProjectExample);
        try {
            destionationPath = FILE_MANAGER_FOR_TEST.saveFile();
        } catch (IOException exception) {
            Log.getLogger(FileManagerTest.class).error(exception.getMessage(), exception);
        }
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> FILE_MANAGER_FOR_TEST.isValidFileForSave(file));
        System.out.println(exception.getMessage());
        FILE_MANAGER_FOR_TEST.deleteFile(new File(destionationPath));
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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FILE_MANAGER_FOR_TEST.openFile("archivo no existente"));
        System.out.println(exception.getMessage());
    }

}
