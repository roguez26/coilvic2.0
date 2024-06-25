package unit.test.FileManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import mx.fei.coilvicapp.logic.implementations.FileManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FileManagerTest {

    private final FileManager FILE_MANAGER_FOR_TEST = new FileManager();

    private File initializeLargeFile() throws IOException {
        File largeFile = File.createTempFile("LargeFile", ".txt");

        FileWriter writer = new FileWriter(largeFile);
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            data.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ");
        }
        writer.write(data.toString());

        return largeFile;
    }

    @Test
    public void testSaveAssignment() throws IOException {
        int idCollaborativeProjectExample = 1234;
        String expectedDestionationPath = "files\\activities" + "\\" + idCollaborativeProjectExample
                + "\\certificate.pdf";
        String result = "";
        File file = new File("files\\template\\certificate.pdf");

        result = FILE_MANAGER_FOR_TEST.saveAssignment(file, idCollaborativeProjectExample);
        assertEquals(expectedDestionationPath, result);
        File savedFile = new File(result);
        System.out.println(savedFile);
        FILE_MANAGER_FOR_TEST.deleteFile(savedFile);
    }

    @Test
    public void testSaveSyllabus() throws IOException {
        int idCollaborativeProjectRequestExample = 1234;
        String expectedDestionationPath = "files\\syllabus" + "\\" + idCollaborativeProjectRequestExample
                + "\\certificate.pdf";
        String result = "";
        File file = new File("files\\template\\certificate.pdf");

        result = FILE_MANAGER_FOR_TEST.saveSyllabus(file, idCollaborativeProjectRequestExample);
        assertEquals(expectedDestionationPath, result);
        File savedFile = new File(result);
        System.out.println(savedFile);
        FILE_MANAGER_FOR_TEST.deleteFile(savedFile);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveSyllabusFailByAllreadyFileAlready() throws IOException {
        int idCollaborativeProjectRequestExample = 1234;
        String result = "";
        File file = new File("files\\template\\certificate.pdf");

        result = FILE_MANAGER_FOR_TEST.saveSyllabus(file, idCollaborativeProjectRequestExample);
        try {
            FILE_MANAGER_FOR_TEST.saveSyllabus(file, idCollaborativeProjectRequestExample);
        } finally {
            File savedFile = new File(result);
            FILE_MANAGER_FOR_TEST.deleteFile(savedFile);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveAssignmentFailByAllreadyFileAlready() throws IOException {
        int idCollaborativeProjectExample = 1234;
        String result = "";
        File file = new File("files\\template\\certificate.pdf");

        result = FILE_MANAGER_FOR_TEST.saveAssignment(file, idCollaborativeProjectExample);

        try {
            FILE_MANAGER_FOR_TEST.saveAssignment(file, idCollaborativeProjectExample);
        } finally {
            File savedFile = new File(result);
            FILE_MANAGER_FOR_TEST.deleteFile(savedFile);
        }
    }

    @Test
    public void isValidForSaveSucces() {
        boolean result = false;

        result = FILE_MANAGER_FOR_TEST.isValidFileForSave(new File("files\\template\\certificate.pdf"));
        System.out.println(result);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValidForSaveFailByTooLarge() throws IOException {
        FILE_MANAGER_FOR_TEST.isValidFileForSave(initializeLargeFile());
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValidForSaveFailByNullFile() {
        FILE_MANAGER_FOR_TEST.isValidFileForSave(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValidForSaveFailByExistenceFile() {
        File file = new File("certificateEjemploArchivoNoExistente.pdf");

        FILE_MANAGER_FOR_TEST.isValidFileForSave(file);
    }

    @Test
    public void testOpenFileSucess() throws IOException {
        FILE_MANAGER_FOR_TEST.openFile("files\\template\\certificate.pdf");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenFileFailByNonexistenceFile() throws IOException {
        FILE_MANAGER_FOR_TEST.openFile("archivo no existente");
    }

}
