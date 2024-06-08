package unit.test.PDFCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import log.Log;
import mx.fei.coilvicapp.logic.implementations.PDFCreator;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author ivanr
 */
public class PDFCreatorTest {

    private final PDFCreator PDF_CREATOR = new PDFCreator();
    private final String ORIGINAL_TEMPLATE_PATH = "files\\template\\certificate.pdf";
    private final String TEMPORARY_PATH = "files\\certificate2.pdf";

    @Test
    public void testGenerateCertificateSuccess() {
        String nameParticipantExample = "Abraham Gonzales Hernández";
        String destinationPath = "files";
        String generatedCertificate = "";

        try {
            generatedCertificate = PDF_CREATOR.generateCertificate(nameParticipantExample, destinationPath);
        } catch (IOException exception) {
            Log.getLogger(PDFCreatorTest.class).error(exception.getMessage(), exception);
        }

        File file = new File(generatedCertificate);
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testGenerateCertificateFailByNoSelectedDestination() {
        String nameParticipantExample = "Abraham Gonzales Hernández";
        String destinationPath = "";
        String generatedCertificate = "";

        try {
            generatedCertificate = PDF_CREATOR.generateCertificate(nameParticipantExample, destinationPath);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            Log.getLogger(PDFCreatorTest.class).error(exception.getMessage(), exception);
        }

        File file = new File(generatedCertificate);
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testGenerateCertificateFailByNullDestination() {
        String nameParticipantExample = "Abraham Gonzales Hernández";
        String destinationPath = null;
        String generatedCertificate = "";

        try {
            generatedCertificate = PDF_CREATOR.generateCertificate(nameParticipantExample, destinationPath);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            Log.getLogger(PDFCreatorTest.class).error(exception.getMessage(), exception);
        }

        File file = new File(generatedCertificate);
        assertTrue(file.exists());
        file.delete();
    }
    
    private void saveFileTemporary() {
        try {
            Files.copy(Paths.get(ORIGINAL_TEMPLATE_PATH), Paths.get(TEMPORARY_PATH));
        } catch (IOException exception) {
            Log.getLogger(PDFCreatorTest.class).error(exception.getMessage(), exception);
        }
    }
    
    private void returnFileToOriginalPath() {
        try {
            Files.copy(Paths.get(TEMPORARY_PATH), Paths.get(ORIGINAL_TEMPLATE_PATH));
        } catch (IOException exception) {
            Log.getLogger(PDFCreatorTest.class).error(exception.getMessage(), exception);
        }
    }

     
    @Test
    public void testGenerateCertificateFailByNonexistenceTemplate() {
        String nameParticipantExample = "Abraham Gonzales Hernández";
        String destinationPath = "files";
        String generatedCertificate = "";
        saveFileTemporary();
        File fileForTest = new File(ORIGINAL_TEMPLATE_PATH);
        File fileTemporary = new File(TEMPORARY_PATH);
        fileForTest.delete();
        try {
            generatedCertificate = PDF_CREATOR.generateCertificate(nameParticipantExample, destinationPath);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            Log.getLogger(PDFCreatorTest.class).error(exception.getMessage(), exception);
        }
        returnFileToOriginalPath();
        File file = new File(generatedCertificate);
        fileTemporary.delete();
        assertTrue(file.exists());
        
    }
}
