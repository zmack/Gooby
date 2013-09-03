package models;

import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.mvc.Http.MultipartFormData.FilePart;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.imgscalr.Scalr;

@Entity
public class Image extends Model {

    @Id
    @Constraints.Min(10)
    public Long id;

    @Constraints.Required
    public String name;

    private String filePath;
    private String mimeType;

    private Integer downloads;
    private static Finder<Long,Image> find = new Finder<Long, Image>(Long.class, Image.class);

    public static List<Image> getAll() {
        return Image.find.all();
    }

    public static Image create() {
        return new Image();
    }

    public static Image getById(Long id) {
        return Image.find.byId(id);
    }

    public Image() {
        this.downloads = 0;
    }

    public Image(String name, Integer downloads) {
        this.name = name;
        this.downloads = downloads;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean setAttachedFileContentAsBytes(byte[] fileContent, String contentType) throws IOException {
        Path path = getFileTempPath();

        try {
            Files.write(path, fileContent);
        } catch(IOException e) {
            Logger.error("Could not write File contents");
        }
        this.mimeType = contentType;
        this.filePath = path.toString();
        return true;
    }

    public File addAttachedFile(FilePart imageFile) {
        if (imageFile == null) {
            return null;
        }

        Path path = getFileTempPath();

        File attachedFile = imageFile.getFile();
        File destinationFile = path.toFile();

        if (attachedFile.renameTo(destinationFile)) {
            Logger.info("This worked");
        } else {
            Logger.info("This did not work");
        }

        this.mimeType = imageFile.getContentType();
        this.filePath = path.toString();
        return destinationFile;
    }

    public String getName() {
        return name;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void generateThumbnail() {
        try {
            final BufferedImage source = ImageIO.read(getAttachedFile());
            final BufferedImage resized = Scalr.resize(
                    source, Scalr.Mode.FIT_TO_WIDTH, 200, Scalr.OP_ANTIALIAS, Scalr.OP_BRIGHTER);
            ImageIO.write(resized, "jpeg", getAttachedFileThumbnail());

        } catch(IOException exception) {
            Logger.error("Could not get a valid image up in here");
        }
    }


    public String getMimeType() {
        return mimeType;
    }

    public File getAttachedFile() {
        return new File(this.filePath);
    }

    public File getAttachedFileThumbnail() {
        return new File(this.filePath + "thumb");
    }

    private Path getFileTempPath() {
        return Paths.get(System.getProperty("user.dir"), "tmp", "gooby", UUID.randomUUID().toString());
    }
}
