package mx.fei.coilvicapp.logic.implementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public FileManager() {

    }

    public String saveAssignment(File selectedFile, int idCollaborativeProject) throws IOException {
        if (selectedFile != null) {
            if (isFileLenghtValid(selectedFile)) {
                String destinationDirectory = activitiesDestination + "\\" + String.valueOf(idCollaborativeProject) + "\\";
                if (!fileExists(destinationDirectory + "\\" + selectedFile.getName())) {
                    if (!directoryExists(destinationDirectory)) {
                        File newDirectory = new File(destinationDirectory);
                        newDirectory.mkdir();
                    }
                    destination = Paths.get(destinationDirectory);
                    fileDestination = destination.resolve(selectedFile.getName());
                    Files.copy(selectedFile.toPath(), fileDestination.toAbsolutePath());
                    selectedFilePath = fileDestination.toString();
                } else {
                    throw new IllegalArgumentException("Ya existe un archivo con este nombre");
                }
            } else {
                throw new IllegalArgumentException("El archivo debe ser menor o igual a 10MB");
            }
        } else {
            throw new IllegalArgumentException("El archivo no puede estar vacio");
        }
        
        return selectedFilePath;
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
