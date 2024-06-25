package unit.test.PDFCreator;

import java.io.File;
import java.io.IOException;
import mx.fei.coilvicapp.logic.implementations.PDFCreator;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PDFCreatorTest {

    private final PDFCreator PDF_CREATOR = new PDFCreator();

    @Test
    public void testGenerateCertificateSuccess() throws IOException {
        String nameParticipantExample = "Abraham Gonzales Hernández";
        String destinationPath = "files";
        String generatedCertificate = "";

        generatedCertificate = PDF_CREATOR.generateCertificate(nameParticipantExample, destinationPath);
        File file = new File(generatedCertificate);
        System.out.println(file.getAbsolutePath());
        assertTrue(file.exists());
        file.delete();
    }

    @Test(expected = IOException.class)
    public void testGenerateCertificateFailByNoSelectedDestination() throws IOException {
        String nameParticipantExample = "Abraham Gonzales Hernández";
        String destinationPath = "";

        PDF_CREATOR.generateCertificate(nameParticipantExample, destinationPath);
    }

    @Test(expected = IOException.class)
    public void testGenerateCertificateFailByNullDestination() throws IOException {
        String nameParticipantExample = "Abraham Gonzales Hernández";
        String destinationPath = null;

        PDF_CREATOR.generateCertificate(nameParticipantExample, destinationPath);

    }

    @Test(expected = IOException.class)
    public void testGenerateCertificateFailByNonexistenceTemplate() throws IOException {
        String nameParticipantExample = "Abraham Gonzales Hernández";
        String destinationPath = "files";
        File originalTemplate = new File("files\\template\\certificate.pdf");
        File auxTemplate = new File("files\\template\\certificate2.pdf");
        originalTemplate.renameTo(auxTemplate);

        try {
            PDF_CREATOR.generateCertificate(
                    nameParticipantExample, destinationPath);
        } finally {
            auxTemplate.renameTo(originalTemplate);
        }
    }

    @Test
    public void testTemplateExistsSuccess() {
        System.out.println(PDF_CREATOR.templateExists());
        assertTrue(PDF_CREATOR.templateExists());
    }

    @Test
    public void testTemplateExistsFailByNonexistenceTemplate() {
        File originalTemplate = new File("files\\template\\certificate.pdf");
        File auxTemplate = new File("files\\template\\certificate2.pdf");
        originalTemplate.renameTo(auxTemplate);
        System.out.println(PDF_CREATOR.templateExists());
        assertTrue(!PDF_CREATOR.templateExists());
        auxTemplate.renameTo(originalTemplate);
    }
}
