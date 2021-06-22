package com.semihbkgr.Corbeau.controller;

import com.semihbkgr.Corbeau.entity.Image;
import com.semihbkgr.Corbeau.entity.Post;
import com.semihbkgr.Corbeau.entity.Subject;
import com.semihbkgr.Corbeau.service.ImageService;
import com.semihbkgr.Corbeau.service.PostService;
import com.semihbkgr.Corbeau.service.SubjectService;
import com.semihbkgr.Corbeau.util.Images;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/authorized")
public class AuthorizedController {

    private final ImageService imageService;
    private final SubjectService subjectService;
    private final PostService postService;

    @Autowired
    public AuthorizedController(ImageService imageService, SubjectService subjectService, PostService postService) {
        this.imageService = imageService;
        this.subjectService = subjectService;
        this.postService = postService;
    }

    @PutMapping(value="/image", consumes = {"multipart/form-data"},produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] saveImage(@RequestPart(value = "urlEndpoint") String urlEndpoint,
                            @RequestPart("content") MultipartFile file) throws IOException {

        if(urlEndpoint==null || file==null || /*file.getBytes()==null ||*/ urlEndpoint.length()==0 || file.getBytes().length==0){
            throw new NullPointerException("urlEndpoint and content cannot be null or empty");
        }

        if(Images.contains(urlEndpoint)){
            throw new IllegalArgumentException("urlEndpoint is duplicated with predefined image names");
        }

        byte[]content=file.getBytes();
        Image image=new Image();
        image.setContent(content);
        image.setUrlEndpoint(urlEndpoint);
        return imageService.save(image).getContent();
    }

    @DeleteMapping("/image/{urlEndpoint}")
    public void deleteImage(@PathVariable("urlEndpoint") String urlEndpoint){
        imageService.deleteByUrlEndpoint(urlEndpoint);
    }

    @PutMapping(value="/post",consumes={"multipart/form-data"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String savePost(@RequestPart("subjectName") String subjectName,
                           @RequestPart("urlEndpoint") String urlEndpoint,
                           @RequestPart("title")String title,
                           @RequestPart("content") MultipartFile file) throws IOException {

        if(subjectName==null || urlEndpoint==null || title==null || file==null ||
                subjectName.length()==0 || urlEndpoint.length()==0 || title.length()==0 ||
                /*file.getBytes()==null ||*/ file.getBytes().length==0){
            throw new NullPointerException("subjectName, urlEndpoint, title, and content cannot be null or empty");
        }

        Subject subject=subjectService.findByName(subjectName);

        if(subject==null){
            throw new NullPointerException("subjectName:"+subjectName+" is not exists");
        }

        String content=new String(file.getBytes());
        Post post=postService.findByTitle(title);
        String response="saved";

        if(post==null){
            post=new Post();
            response="created";
        }

        post.setTitle(title);
        post.setContent(content);
        post.setSubject(subject);
        post.setUrlEndpoint(urlEndpoint);

        postService.save(post);

        return response;

    }

    @DeleteMapping("/post/{title}")
    public void deletePost(@PathVariable("title") String title){
        postService.deleteByTitle(title);
    }

    @PutMapping(value="/subject", consumes = {"multipart/form-data"})
    public String saveSubject(@RequestPart("name") String name,
                              @RequestPart("urlEndpoint") String urlEndpoint){

        if(name==null || urlEndpoint==null || name.length()==0 || urlEndpoint.length()==0)
            throw new NullPointerException("name and urlEndpoint cannot be null or empty");
        else if(name.equals("s"))
            throw new IllegalArgumentException("Subject name cannot be 's'");

        Subject subject=subjectService.findByName(name);

        if(subject!=null){
            subject.setUrlEndpoint(urlEndpoint);
            subjectService.save(subject);
            return "saved";
        }

        subject=new Subject();
        subject.setName(name);
        subject.setUrlEndpoint(urlEndpoint);
        subjectService.save(subject);
        return "created";

    }

    @DeleteMapping("/subject/{name}")
    public void deleteSubject(@PathVariable("name") String name){
        subjectService.deleteByName(name);
    }

    @PostMapping("/post/change/{currentTitle}/{newTitle}")
    public String changePostTitle(@PathVariable("currentTitle") String currentTitle,
                                  @PathVariable("newTitle") String newTitle){
        Post post=postService.findByTitle(currentTitle);
        if(post==null){
            throw new NullPointerException("title:"+currentTitle+" is not exists");
        }
        post.setTitle(newTitle);
        postService.save(post);
        return "changed";
    }

    @PostMapping("/subject/change/{currentName}/{newName}")
    public String changeSubjectName(@PathVariable("currentName")String currentName,
                                    @PathVariable("newName")String newName){

        Subject subject=subjectService.findByName(currentName);
        if(subject==null){
            throw new NullPointerException("name:"+currentName+" is not exists");
        }
        subject.setName(newName);
        subjectService.save(subject);
        return "changed";
    }


}