package mx.fei.coilvicapp.logic.implementations;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import log.Log;
import mx.fei.coilvicapp.logic.professor.Professor;

public class XLSXCreator {

    private static final String FILE_PATH = "files\\xlsx\\ProfesoresValidados.xlsx";

    public static void addProfessorIntoXLSX(Professor professor) {
        Workbook workbook;
        Sheet sheet;

        try (FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
            Files.createDirectories(Paths.get(FILE_PATH).getParent());
            workbook = new XSSFWorkbook(fileInputStream);
            sheet = workbook.getSheetAt(0);
        } catch (IOException exception) {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Profesores");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Nombre");
            headerRow.createCell(1).setCellValue("Apellido Paterno");
            headerRow.createCell(2).setCellValue("Apellido Materno");
            headerRow.createCell(3).setCellValue("Correo");
            headerRow.createCell(4).setCellValue("Universidad");
        }
        int rowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(professor.getName());
        row.createCell(1).setCellValue(professor.getPaternalSurname());
        row.createCell(2).setCellValue(professor.getMaternalSurname());
        row.createCell(3).setCellValue(professor.getEmail());
        row.createCell(4).setCellValue(professor.getUniversity().getName());
        for (int i = 0; i <= 4; i++) {
            sheet.autoSizeColumn(i);
        }
        try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
            workbook.write(fileOut);
        } catch (IOException exception) {
            Log.getLogger(XLSXCreator.class).error(exception.getMessage(), exception);
        }

        try {
            workbook.close();
        } catch (IOException exception) {
            Log.getLogger(XLSXCreator.class).error(exception.getMessage(), exception);
        }
    }

    public static void copyFileToDestination(String destinationPath) throws IOException {
        Files.copy(Paths.get(FILE_PATH), Paths.get(destinationPath, "ProfesoresValidados.xlsx"),
                StandardCopyOption.REPLACE_EXISTING);
    }

    public String getFilePath() {
        return FILE_PATH;
    }

}
