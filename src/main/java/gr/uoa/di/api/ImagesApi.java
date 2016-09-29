package gr.uoa.di.api;

import gr.uoa.di.dao.ItemPicturesEntity;
import gr.uoa.di.exception.item.ItemCannotBeEditedException;
import gr.uoa.di.repo.ItemPicturesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private ItemPicturesRepository itemPicturesRepository;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public String upload(@RequestParam("file") MultipartFile uploadFile) throws IOException {
        String thisImage = new String(imagePath);
        if (!thisImage.endsWith("/")) {
            thisImage += "/";
        }
        String uuid = UUID.randomUUID().toString();

        /* save image on host */
        thisImage += uuid + uploadFile.getOriginalFilename();
        uploadFile.transferTo(new File(thisImage));

        /* create image entry on database */
        ItemPicturesEntity imageEnt = new ItemPicturesEntity();
        imageEnt.setUuid(uuid);
        imageEnt.setFilename(uploadFile.getOriginalFilename());
        itemPicturesRepository.save(imageEnt);
        return uuid;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public FileSystemResource getFile(@PathVariable("id") String uuid) {
        /* get image by uuid */
        String filename = itemPicturesRepository.findOneByUuid(uuid).getFilename();
        String thisImage = new String(imagePath);
        if (!thisImage.endsWith("/")) {
            thisImage += "/";
        }
        thisImage += uuid + filename;
        return new FileSystemResource(thisImage);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteFile(@PathVariable("id") String uuid) {
        ItemPicturesEntity image = itemPicturesRepository.findOneByUuid(uuid);
        String currentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (image.getItem() != null && !image.getItem().getOwner().getUsername().equals(currentUser)) {
            throw new ItemCannotBeEditedException();
        }
        itemPicturesRepository.delete(image);
    }
}
