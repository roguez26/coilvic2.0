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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;

public class PDFCreator {

    private final String certificateTemplatePath = "files\\template\\certificate.pdf";

    public void generateCertificate(String name, String certificateDestination) throws IOException {
        LocalDate date = LocalDate.now();
        try {
            try (
                    PdfReader reader = new PdfReader(certificateTemplatePath)) {
                PdfWriter writer = new PdfWriter(certificateDestination + "\\" + date + "_" + name + ".pdf");
                try (PdfDocument pdfDoc = new PdfDocument(reader, writer); Document document = new Document(pdfDoc)) {
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
            Logger.getLogger(PDFCreator.class.getName()).log(Level.SEVERE, null, exception);
            throw new IOException("No fue posible generar la constancia");
        }

    }

    public boolean templateExists() {
        File file = new File(certificateTemplatePath);
        return file.exists();
    }
}
