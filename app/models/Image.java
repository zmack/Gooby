package models;

import java.util.ArrayList;
import java.util.List;
import play.data.validation.Constraints.*;

public class Image {
    @Required
    private String name;
    private Integer downloads;

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
    }

    public Image(String name, Integer downloads) {
        this.name = name;
        this.downloads = downloads;
    }

    public String getName() {
        return name;
    }

    public Integer getDownloads() {
        return downloads;
    }
}
