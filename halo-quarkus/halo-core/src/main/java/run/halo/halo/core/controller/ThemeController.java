package run.halo.halo.core.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import run.halo.halo.core.theme.ThemeEngine;

import java.util.List;

@Path("/api/themes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ThemeController {
    
    @Inject
    ThemeEngine themeEngine;
    
    @GET
    @RolesAllowed({"SUPER_ADMIN", "ADMIN"})
    public Response listThemes() {
        List<String> themes = themeEngine.listThemes();
        return Response.ok(themes).build();
    }
    
    @GET
    @Path("/active")
    public Response getActiveTheme() {
        return Response.ok(Map.of("theme", themeEngine.getActiveTheme())).build();
    }
    
    @PUT
    @Path("/active")
    @RolesAllowed({"SUPER_ADMIN", "ADMIN"})
    public Response setActiveTheme(Map<String, String> request) {
        String themeName = request.get("theme");
        if (themeName == null || themeName.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Theme name is required\"}")
                    .build();
        }
        themeEngine.setActiveTheme(themeName);
        return Response.ok(Map.of("message", "Theme activated")).build();
    }
}
