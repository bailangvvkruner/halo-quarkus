package run.halo.halo.core.controller;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import run.halo.halo.core.entity.Comment;
import run.halo.halo.core.service.CommentService;

@Path("/api/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentController {
    
    @Inject
    CommentService commentService;
    
    @GET
    @Path("/target/{targetId}/{targetType}")
    public Multi<Comment> getCommentsByTarget(@PathParam("targetId") Long targetId, 
                                               @PathParam("targetType") Comment.TargetType targetType) {
        return commentService.findByTarget(targetId, targetType);
    }
    
    @POST
    public Uni<Response> createComment(Comment comment) {
        return commentService.create(comment)
                .map(createdComment -> Response.status(Response.Status.CREATED)
                        .entity(createdComment)
                        .build())
                .onFailure().recoverWithItem(throwable -> 
                    Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"" + throwable.getMessage() + "\"}")
                        .build()
                );
    }
    
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN"})
    public Uni<Response> deleteComment(@PathParam("id") Long id) {
        return commentService.delete(id)
                .map(v -> Response.noContent().build())
                .onFailure().recoverWithItem(Response.status(Response.Status.NOT_FOUND).build());
    }
}
