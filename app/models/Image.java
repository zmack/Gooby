package models;

import javax.persistence.*;

import java.lang.Long;
import java.util.ArrayList;
import java.util.List;

import play.data.validation.Constraints.*;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;
import play.db.ebean.Model;

@Entity
public class Image extends Model {

    @Id
    @Constraints.Min(10)
    public Long id;

    @Constraints.Required
    public String name;

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
