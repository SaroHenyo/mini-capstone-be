package henyosisaro.minicapstonebe.service;

import henyosisaro.minicapstonebe.dto.BlogDTO;
import henyosisaro.minicapstonebe.entity.BlogEntity;
import henyosisaro.minicapstonebe.exception.UserAlreadyExist;
import henyosisaro.minicapstonebe.model.BlogRequest;
import henyosisaro.minicapstonebe.repository.BlogRepository;
import henyosisaro.minicapstonebe.util.DateTimeUtil;
import henyosisaro.minicapstonebe.util.S3StorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final DateTimeUtil dateTimeUtil;
    private final S3StorageUtil s3StorageUtil;

    public List<BlogDTO> getAllBlogs() {

        // Get all data from database
        List<BlogEntity> allBlogs = blogRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));

        // Initialize dto
        List<BlogDTO> allBlogsDTO = new ArrayList<>();

        allBlogs.forEach(product -> {
            allBlogsDTO.add(modelMapper.map(product, BlogDTO.class));
        });

        return allBlogsDTO;
    }

    public List<BlogDTO> addBlog(BlogRequest newBlog) {

        // Save new blog to database
        blogRepository.save(BlogEntity
                .builder()
                .blogId(UUID.randomUUID())
                .blogName(newBlog.getBlogName())
                .blogAuthor(newBlog.getBlogAuthor())
                .imageLink(null)
                .description(newBlog.getDescription())
                .createdDate(dateTimeUtil.currentDate())
                .modifiedDate(dateTimeUtil.currentDate())
                .build());

        return getAllBlogs();
    }

    public List<BlogDTO> deleteBlog(UUID blogId) {

        // Get blog
        BlogEntity blog = blogRepository.findByBlogId(blogId);

        // Check if product exist
        if(blog == null) throw new UserAlreadyExist("Blog doesn't exist");

        // Delete product
        blogRepository.deleteByBlogId(blogId);

        return getAllBlogs();
    }

    public List<BlogDTO> uploadBlogImage(UUID blogId, MultipartFile file) {
        // Initialize blog
        BlogEntity blog = blogRepository.findByBlogId(blogId);
        if (blog == null) throw new IllegalStateException("Blog doesn't exist");

        // Check if file validity
        s3StorageUtil.checkFile(file);

        // Grab some meta data
        Map<String, String> metadata = s3StorageUtil.getMetaData(file);

        // Store the image in S3
        String path = String.format("%s/%s", "minicapstone-mikez/saro/blogs", blogId);
        String fileName = String.format("%s-%s", "blog", file.getOriginalFilename());
        try {
            s3StorageUtil.save(path, fileName, Optional.of(metadata), file.getInputStream());
            blogRepository.save(BlogEntity
                    .builder()
                    .blogId(blog.getBlogId())
                    .blogName(blog.getBlogName())
                    .blogAuthor(blog.getBlogAuthor())
                    .imageLink(fileName)
                    .description(blog.getDescription())
                    .createdDate(blog.getCreatedDate())
                    .modifiedDate(dateTimeUtil.currentDate())
                    .build());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return getAllBlogs();
    }

    public byte[] downloadBlogImage(UUID blogId) {
        // Initialize blog
        BlogEntity blog = blogRepository.findByBlogId(blogId);
        if (blog == null) throw new IllegalStateException("Blog doesn't exist");

        String path = String.format("%s/%s", "minicapstone-mikez/saro/blogs", blogId);

        return s3StorageUtil.download(path, blog.getImageLink());
    }

}