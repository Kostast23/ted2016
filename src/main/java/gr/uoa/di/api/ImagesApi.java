package gr.uoa.di.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImagesApi {
    @Value("${image-directory}")
    private String imagePath;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public String upload(@RequestParam("file") MultipartFile uploadFile) throws IOException {
        String thisImage = new String(imagePath);
        if (!thisImage.endsWith("/")) {
            thisImage += "/";
        }
        String uuid = UUID.randomUUID().toString();
        thisImage += uuid;
        uploadFile.transferTo(new File(thisImage));
        return uuid;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public FileSystemResource getFile(@PathVariable("id") String uuid) {
        String thisImage = new String(imagePath);
        if (!thisImage.endsWith("/")) {
            thisImage += "/";
        }
        thisImage += uuid;
        return new FileSystemResource(thisImage);
    }
}
