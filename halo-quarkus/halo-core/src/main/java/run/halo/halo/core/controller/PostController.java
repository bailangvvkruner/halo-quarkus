package run.halo.halo.core.controller;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import run.halo.halo.core.dto.PostDTO;
import run.halo.halo.core.entity.Post;
import run.halo.halo.core.service.PostService;

@Path("/api/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostController {
    
    @Inject
    PostService postService;
    
    @Inject
    JsonWebToken jwt;
    
    @GET
    public Multi<PostDTO> getAllPosts() {
        return postService.findAll();
    }
    
    @GET
    @Path("/published")
    public Multi<PostDTO> getPublishedPosts() {
        return postService.findPublished();
    }
    
    @GET
    @Path("/{id}")
    public Uni<Response> getPost(@PathParam("id") Long id) {
        return postService.findById(id)
                .map(post -> Response.ok(post).build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @GET
    @Path("/slug/{slug}")
    public Uni<Response> getPostBySlug(@PathParam("slug") String slug) {
        return postService.findBySlug(slug)
                .map(post -> Response.ok(post).build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @POST
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "CONTRIBUTOR"})
    public Uni<Response> createPost(Post post) {
        return postService.create(post)
                .map(createdPost -> Response.status(Response.Status.CREATED)
                        .entity(createdPost)
                        .build())
                .onFailure().recoverWithItem(throwable -> 
                    Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"" + throwable.getMessage() + "\"}")
                        .build()
                );
    }
    
    @PUT
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN", "CONTRIBUTOR"})
    public Uni<Response> updatePost(@PathParam("id") Long id, Post post) {
        return postService.update(id, post)
                .map(updatedPost -> Response.ok(updatedPost).build())
                .onFailure().recoverWithItem(throwable -> 
                    Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"" + throwable.getMessage() + "\"}")
                        .build()
                );
    }
    
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN"})
    public Uni<Response> deletePost(@PathParam("id") Long id) {
        return postService.delete(id)
                .map(v -> Response.noContent().build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).build());
    }
}
