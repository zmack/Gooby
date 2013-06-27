package controllers;

import models.Image;
import play.*;
import play.data.Form;
import static play.data.Form.*;
import play.libs.F;
import play.libs.WS;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import views.html.gifs.index;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Gif extends Controller {
    public static Form<Image> imageForm = form(Image.class);

    public static Result index() {
        List<Image> imageList = Image.find.all();

        return ok(index.render(imageList));
    }

    public static Result create() {
        MultipartFormData body = request().body().asMultipartFormData();
        String[] imageUrl = body.asFormUrlEncoded().get("image_url");
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
            return saveImageAsync(image, imageUrl[0]);
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

    private static Result saveImageAsync(final Image image, String imageUrl) {
        Logger.info(imageUrl);
        if (imageUrl == null) {
            image.save();
        } else {
            F.Promise<Result> result = WS.url(imageUrl).get().map(
                    new F.Function<WS.Response, Result>() {
                        @Override
                        public Result apply(WS.Response response) throws Throwable {
                            image.setAttachedFileContentAsBytes(response.asByteArray(),response.getHeader("Content-Type"));
                            image.save();
                            return ok("Hello world");
                        }
                    }
            );
            return async(result);
        }

        return redirect(routes.Gif.index());
    }
}