package mx.fei.coilvicapp.logic.implementations;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import log.Log;

/**
 *
 * @author ivanr
 */
public class FileManager {

    private final long MAX_SIZE_BYTES = 10 * 1024 * 1024;
    private static final String FILE_PATH = "files\\xlsx\\ProfesoresValidados.xlsx";
    
    public String saveAssignment(File assignmentFile, int idCollaborativeProject) throws IOException {
        String resultPath = "";
        
        if (isValidFileForSave(assignmentFile) && idCollaborativeProject > 0) {
            String destinationAssignmentPath = "files\\activities" +"\\" + String.valueOf(
                idCollaborativeProject) + "\\";
            if (!fileExists(destinationAssignmentPath + assignmentFile.getName())) {
                resultPath = saveFile(assignmentFile, destinationAssignmentPath);
            } else {
                throw new IllegalArgumentException("Ya existe una actividad en este proyecto con el mismo nombre, intente con otro.");
            }
        }
        return resultPath;
    }
    
    public String saveSyllabus(File syllabusFile, int idCollaborativeProjectRequest) throws IOException {
        String resultPath = "";
        
        if (isValidFileForSave(syllabusFile) && idCollaborativeProjectRequest > 0) {
            String destinationSyllabusPath = "files\\syllabus\\" + String.valueOf(idCollaborativeProjectRequest) + "\\";
            if (!fileExists(destinationSyllabusPath + syllabusFile.getName())) {
                resultPath = saveFile(syllabusFile, destinationSyllabusPath);
            } else {
                throw new IllegalArgumentException("Ya existe un syllabus de este proyecto con el mismo nombre, intente con otro.");
            }
        }
        return resultPath;
    }

    public String saveFile(File fileForSave, String destinationPath) throws IOException {
        if (destinationPath == null || destinationPath.length() == 0) {
            throw new IOException("El directorio destion no puede ser nulo");
        }
        if (!directoryExists(destinationPath)) {
            File newDirectory = new File(destinationPath);
            newDirectory.mkdir();
        }
        if (fileForSave == null) {
            throw new IOException ("El archivo para guardar no puede estar vacío");
        }
        Path destination = Paths.get(destinationPath);
        Path fileDestination = destination.resolve(fileForSave.getName());
        Files.copy(fileForSave.toPath(), fileDestination.toAbsolutePath());
        String finalDestinationPath = fileDestination.toString();
        return finalDestinationPath;
    }

    public boolean isValidFileForSave(File fileForCheck) {
        if (fileForCheck == null) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        if (!fileForCheck.exists()) {
            throw new IllegalArgumentException("No se encontró el archivo que se desea guardar");
        }
        if (!isFileLenghtValid(fileForCheck)) {
            throw new IllegalArgumentException("El archivo debe ser menor o igual a 10MB");
        } 
        return true;
    }

    public File selectPDF(Window window) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", 
                "*.pdf");
        fileChooser.getExtensionFilters().add(pdfFilter);
        return fileChooser.showOpenDialog(window.getScene().getWindow());
    }

    public File selectXLSXFile(Window window) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter xlsxFilter = new FileChooser.ExtensionFilter("Archivos XLSX (*.xlsx)",
                "*.xlsx");
        fileChooser.getExtensionFilters().add(xlsxFilter);
        return fileChooser.showOpenDialog(window.getScene().getWindow());
    }

    public static void copyFileToDestination(String destinationPath) throws IOException {
        Files.copy(Paths.get(FILE_PATH), Paths.get(destinationPath, "ProfesoresValidados.xlsx"),
                StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean copyXLSXToSelectedDirectory(Window window) throws IOException, IllegalArgumentException {
        Path sourcePath = Paths.get(FILE_PATH);

        if (!Files.exists(sourcePath)) {
            throw new IllegalArgumentException("No se encontraron los recursos para generar el archivo");
        }
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(window);

        if (selectedDirectory != null) {
            Path destinationPath = selectedDirectory.toPath().resolve(sourcePath.getFileName().toString());
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } else {
            throw new IllegalArgumentException("Debe seleccionar un directorio");
        }
        return true;
    }

    public String selectDirectoryPath(Window window) {
        String directoryPath = "";

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(window.getScene().getWindow());
        if (selectedDirectory != null) {
            directoryPath = selectedDirectory.getAbsolutePath();
        } else {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        return directoryPath;
    }

    private boolean fileExists(String filePath) {
        File fileForValidate = new File(filePath);
        return fileForValidate.exists();
    }

    private boolean directoryExists(String directoryPath) {
        File fileForValidate = new File(directoryPath);
        return fileForValidate.exists();
    }

    public void deleteFile(File file) throws IOException {
        if (file == null) {
            throw new IOException("No se puede eliminar un archivo vacío");
        }
        if (!file.exists()) {
            throw new IOException("No se encontró el archivo para eliminar");
        }
        file.delete();
    }

    private boolean isFileLenghtValid(File forValidate) {
        return forValidate.length() <= MAX_SIZE_BYTES;
    }

    public void openFile(String filePath) throws IOException {
        File fileForOpen = new File(filePath);

        if (!fileForOpen.exists()) {
            throw new IllegalArgumentException("No se encontró el archivo");
        }

        if (!Desktop.isDesktopSupported()) {
            throw new IllegalArgumentException("La apertura de archivos no es compatible con este sistema");
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.OPEN)) {
            throw new IllegalArgumentException("La apertura de archivos no es compatible con este sistema");
        }

        try {
            desktop.open(fileForOpen);
        } catch (IOException exception) {
            Log.getLogger(FileManager.class).error(exception.getMessage(), exception);
            throw new IOException("Ocurrió un error al intentar abrir el archivo");
        }
    }
}
