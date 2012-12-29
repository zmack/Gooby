package controllers;

import models.Image;
import play.Logger;
import play.mvc.*;
import play.data.*;
import views.html.gifs.*;

import java.util.List;

public class Gif extends Controller {
    static Form<Image> imageForm = form(Image.class);

    public static Result index() {
        List<Image> imageList = Image.find.all();
        return ok(index.render(imageList));
    }

    public static Result create() {
        Form<Image> filledForm = imageForm.bindFromRequest();
        Image image;
        if (filledForm.hasErrors()) {
            return badRequest(
                    views.html.gifs.form.render(filledForm)
            );
        } else {
            image = filledForm.get();

            image.save();
            return redirect(routes.Gif.index());
        }
    }

    public static Result newGif() {
        return ok(views.html.gifs.form.render(imageForm));
    }

    public static Result show(Long id) {
        Image image = Image.getById(id);
        return ok(views.html.gifs.show.render(image));
    }
}