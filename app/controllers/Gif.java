package controllers;

import models.Image;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.gifs.index;

import java.util.List;

public class Gif extends Controller {
    static Form<Image> imageForm = form(Image.class);

    public static Result index() {
        List<Image> imageList = Image.find.all();
        return ok(index.render(imageList));
    }

    public static Result create() {
        MultipartFormData body = request().body().asMultipartFormData();
        FilePart imageFile = body.getFile("image_file");
        Form<Image> filledForm = imageForm.bindFromRequest();
        Image image;

        if (filledForm.hasErrors()) {
            return badRequest(
                    views.html.gifs.form.render(filledForm)
            );
        } else {
            image = filledForm.get();
            image.addAttachedFile(imageFile);
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

    public static Result getImage(Long id) {
        Image image = Image.getById(id);
        return ok(image.getAttachedFile()).as(image.getMimeType());
    }
}