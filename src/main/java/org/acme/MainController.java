package org.acme;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Path;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkiverse.renarde.Controller;

public class MainController extends Controller {

    @CheckedTemplate
    static class Templates {
        public static native TemplateInstance index();
        public static native TemplateInstance login();
        public static native TemplateInstance restricted();
    }

    @Path("/")
    public TemplateInstance index() {
        return Templates.index();
    }

    @Path("/error")
    public TemplateInstance error() {
        return Templates.index();
    }

    @Path("/login")
    public TemplateInstance login() {
        return Templates.login();
    }

    @Path("/restricted")
    @RolesAllowed("loggedin")
    public TemplateInstance restricted() {
        return Templates.restricted();
    }

}