package henyosisaro.minicapstonebe.controller;

import henyosisaro.minicapstonebe.dto.BlogDTO;
import henyosisaro.minicapstonebe.model.BlogRequest;
import henyosisaro.minicapstonebe.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/getAll")
    public List<BlogDTO> getAllBlogs() {
        return blogService.getAllBlogs();
    }

    @PutMapping("/add")
    public List<BlogDTO> addBlog(@RequestBody BlogRequest blogRequest) {
        return blogService.addBlog(blogRequest);
    }

    @DeleteMapping("/delete/{blogId}")
    public List<BlogDTO> deleteBlog(@PathVariable UUID blogId) {
        return blogService.deleteBlog(blogId);
    }

    @PutMapping(path = "/{blogId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BlogDTO> uploadBlogImage(@PathVariable UUID blogId, @RequestParam("file") MultipartFile file) {
        return blogService.uploadBlogImage(blogId, file);
    }

    @GetMapping(path = "{blogId}/download")
    public byte[] downloadBlogImage(@PathVariable UUID blogId) {
        return blogService.downloadBlogImage(blogId);
    }
}