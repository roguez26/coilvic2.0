package mx.fei.coilvicapp.logic.implementations;

/**
 *
 * @author ivanr
 */
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import log.Log;

public class PDFCreator {

    private final String CERTIFICATE_TEMPLATE_PATH = "files\\template\\certificate.pdf";

    public String generateCertificate(String name, String certificateDestination) throws IOException {
        LocalDate date = LocalDate.now();
        String destionationCertificatePath;
        
        if (certificateDestination == null || certificateDestination.equals("")) {
            throw new IOException ("Es necesario especificar la ruta para iniciar la descarga");
        }

        try {
            try (PdfReader reader = new PdfReader(CERTIFICATE_TEMPLATE_PATH)) {
                destionationCertificatePath = certificateDestination + "\\" + date + "-" + name + "-constancia.pdf";
                PdfWriter writer = new PdfWriter(destionationCertificatePath);
                try (PdfDocument pdfDocument = new PdfDocument(reader, writer); Document document = new Document(
                        pdfDocument)) {
                    Paragraph paragraph = new Paragraph(name);
                    paragraph.setFixedPosition(0, 335, 855);
                    paragraph.setTextAlignment(TextAlignment.CENTER);
                    paragraph.setFontSize(20);
                    paragraph.setCharacterSpacing(4);
                    paragraph.setBold();
                    document.add(paragraph);
                }
            }
        } catch (IOException exception) {
            Log.getLogger(PDFCreator.class).error(exception.getMessage(), exception);
            throw new IOException("No fue posible generar la constancia");
        }
        return destionationCertificatePath;
    }

    public boolean templateExists() {
        return new File(CERTIFICATE_TEMPLATE_PATH).exists();
    }
}
