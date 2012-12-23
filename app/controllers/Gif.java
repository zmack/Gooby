package controllers;

import models.Image;
import play.mvc.*;
import views.html.gifs.*;

import java.util.List;

public class Gif extends Controller {
    public static Result index() {
        List<Image> imageList = Image.getAll();
        return ok(index.render(imageList));
    }

    public static Result create() {
        return ok(views.html.gifs.form.render());
    }
}