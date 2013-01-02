package controllers;

import models.Image;
import play.Logger;
import play.data.Form;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.gifs.index;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Gif extends Controller {
    static Form<Image> imageForm = form(Image.class);

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

    private static Result saveImageAsync(Image image, String imageUrl) {
        if (imageUrl == null) {
            image.save();
        } else {
            F.Promise<Result> result = WS.url(imageUrl).get().map(
                    new F.Function<WS.Response, Result>() {
                        @Override
                        public Result apply(WS.Response response) throws Throwable {
                            Logger.info(response.getBody());
                            return ok("Hello world");
                        }
                    }
            );
            async(result);
            return result.get(1000L, TimeUnit.SECONDS);
        }

        return redirect(routes.Gif.index());
    }
}