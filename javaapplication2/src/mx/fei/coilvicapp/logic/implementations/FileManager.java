package mx.fei.coilvicapp.logic.implementations;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import log.Log;
import mx.fei.coilvicapp.gui.controllers.ValidateCollaborativeProjectController;

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
        String directoryPath = "";

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(window.getScene().getWindow());
        if (selectedDirectory != null) {
            directoryPath = selectedDirectory.getAbsolutePath();
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

    public void undoSaveAssignment() {
        File fileForDelete = new File(selectedFilePath);
        fileForDelete.delete();
    }

    private boolean isFileLenghtValid(File forValidate) {
        return forValidate.length() <= MAX_SIZE_BYTES;
    }

    public void openFile(String filePath) throws IOException {
        File fileForOpen = new File(filePath);

        if (!fileForOpen.exists()) {
            throw new IllegalArgumentException("No se encontro el archivo");
        }

        if (!Desktop.isDesktopSupported()) {
            throw new IllegalArgumentException("La apertura de archivos no es compatible con este sistema.");
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.OPEN)) {
            throw new IllegalArgumentException("La apertura de archivos no es compatible con este sistema.");
        }

        try {
            desktop.open(fileForOpen);
        } catch (IOException exception) {
            Log.getLogger(FileManager.class).error(exception.getMessage(), exception);
            throw new IOException("Ocurrio un error al intentar abrir el archivo");
        }
    }

}
