package models;

import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.mvc.Http.MultipartFormData.FilePart;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public static Finder<Long,Image> find = new Finder<Long, Image>(Long.class, Image.class);

    public static List<Image> getAll() {
        List<Image> list = new ArrayList<Image>(20);

        for (int i = 0; i < 20; i++) {
            list.add(new Image("Hello world", 0));
        }

        return list;
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

    public Boolean setAttachedFileContent(String fileContent, String contentType) throws IOException {
        Path path = getFileTempPath();
        BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("US-ASCII"));

        try {
            writer.write(fileContent);
        } catch(IOException e) {

        } finally {
            writer.close();
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

    public String getMimeType() {
        return mimeType;
    }

    public File getAttachedFile() {
        Logger.info(this.filePath);
        return new File(this.filePath);
    }

    private Path getFileTempPath() {
        return Paths.get(System.getProperty("user.dir"), "tmp", "gooby", UUID.randomUUID().toString());
    }
}
