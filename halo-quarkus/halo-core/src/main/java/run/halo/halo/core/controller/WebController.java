package run.halo.halo.core.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import io.qute.TemplateInstance;
import run.halo.halo.core.theme.ThemeEngine;
import run.halo.halo.core.entity.Post;
import run.halo.halo.core.service.PostService;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class WebController {
    
    @Inject
    ThemeEngine themeEngine;
    
    @Inject
    PostService postService;
    
    @GET
    public TemplateInstance index() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Halo Quarkus Blog");
        return themeEngine.render("index", data);
    }
    
    @GET
    @Path("/post/{slug}")
    public TemplateInstance post(@PathParam("slug") String slug) {
        Post post = postService.findBySlug(slug).await().indefinitely();
        Map<String, Object> data = new HashMap<>();
        data.put("post", post);
        data.put("title", post.title);
        return themeEngine.render("post", data);
    }
    
    @GET
    @Path("/category/{slug}")
    public TemplateInstance category(@PathParam("slug") String slug) {
        Map<String, Object> data = new HashMap<>();
        data.put("category", slug);
        data.put("title", "Category: " + slug);
        return themeEngine.render("category", data);
    }
    
    @GET
    @Path("/tag/{slug}")
    public TemplateInstance tag(@PathParam("slug") String slug) {
        Map<String, Object> data = new HashMap<>();
        data.put("tag", slug);
        data.put("title", "Tag: " + slug);
        return themeEngine.render("tag", data);
    }
    
    @GET
    @Path("/page/{slug}")
    public TemplateInstance page(@PathParam("slug") String slug) {
        Map<String, Object> data = new HashMap<>();
        data.put("page", slug);
        return themeEngine.render("page", data);
    }
}
