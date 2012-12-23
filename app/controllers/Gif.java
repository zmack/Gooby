package controllers;

import models.Image;
import play.mvc.*;
import play.data.*;
import views.html.gifs.*;

import java.util.List;

public class Gif extends Controller {
    static Form<Image> imageForm = form(Image.class);

    public static Result index() {
        List<Image> imageList = Image.getAll();
        return ok(index.render(imageList));
    }

    public static Result create() {
        Form<Image> filledForm = imageForm.bindFromRequest();
        if (filledForm.hasErrors()) {
            return badRequest(
                    views.html.gifs.form.render(filledForm)
            );
        } else {
            filledForm.get();
            return redirect(routes.Gif.index());
        }
    }

    public static Result newGif() {
        return ok(views.html.gifs.form.render(imageForm));
    }

    public static Result show(Long id) {
        return TODO;
    }
}