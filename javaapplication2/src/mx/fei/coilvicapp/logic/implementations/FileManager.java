package mx.fei.coilvicapp.logic.implementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 *
 * @author ivanr
 */
public class FileManager {

    private static final long MAX_SIZE_BYTES = 10 * 1024 * 1024;
    String activitiesDestination = "files\\activities";
    Path destination;
    Path fileDestination;
    String selectedFilePath;
    File file;
    String destinationDirectory;

    public FileManager() {

    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setDestinationDirectory(int idCollaborativeProject) {
        this.destinationDirectory = activitiesDestination + "\\" + String.valueOf(idCollaborativeProject) + "\\";
    }

    public String saveAssignment() throws IOException {
        if (!directoryExists(destinationDirectory)) {
            File newDirectory = new File(destinationDirectory);
            newDirectory.mkdir();
        }
        destination = Paths.get(destinationDirectory);
        fileDestination = destination.resolve(file.getName());
        Files.copy(file.toPath(), fileDestination.toAbsolutePath());
        selectedFilePath = fileDestination.toString();
        return selectedFilePath;
    }

    public boolean isValidFileForSave() {
        boolean result = false;
        if (file != null) {
            if (isFileLenghtValid(file)) {
                if (!fileExists(destinationDirectory + "\\" + file.getName())) {
                    result = true;
                } else {
                    throw new IllegalArgumentException("Ya existe un archivo con este nombre");
                }
            } else {
                throw new IllegalArgumentException("El archivo debe ser menor o igual a 10MB");
            }
        } else {
            throw new IllegalArgumentException("El archivo no puede estar vacio");
        }
        return result;
    }
    
    public File selectPDF(Window window) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Archivos PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(pdfFilter);
        return fileChooser.showOpenDialog(window.getScene().getWindow());
    }
    
    public String selectDirectoryPath(Window window) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(window.getScene().getWindow()).getAbsolutePath();
    
    }

    private boolean fileExists(String filePath) {
        File fileForValidate = new File(filePath);
        return fileForValidate.exists();
    }

    private boolean directoryExists(String directoryPath) {
        File fileForValidate = new File(directoryPath);
        return fileForValidate.exists();
    }

    public void undoSaveAssignment() {
        File fileForDelete = new File(selectedFilePath);
        fileForDelete.delete();
    }

    private boolean isFileLenghtValid(File forValidate) {
        return forValidate.length() <= MAX_SIZE_BYTES;
    }

}
