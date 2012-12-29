package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.mvc.Http.MultipartFormData.FilePart;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
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
        return new Image("<script>alert('Hello')</script>", 0);
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

    public File addAttachedFile(FilePart imageFile) {
        if (imageFile == null) {
            return null;
        }

        Path path = Paths.get("/tmp/gooby", UUID.randomUUID().toString());

        File attachedFile = imageFile.getFile();
        File destinationFile = path.toFile();
        attachedFile.renameTo(destinationFile);
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
}
