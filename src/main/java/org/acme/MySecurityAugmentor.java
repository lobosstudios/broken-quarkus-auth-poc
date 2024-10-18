package org.acme;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class MySecurityAugmentor implements SecurityIdentityAugmentor {
    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {

        Uni<SecurityIdentity> returnValue = null;

        CompletableFuture<SecurityIdentity> cs = new CompletableFuture<>();
        boolean futureCompleted;

        if (identity.isAnonymous()) {
            futureCompleted = cs.complete(identity);
        } else {
            // create a new builder and copy principal, attributes, credentials and roles from the original
            QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder()
                    .setPrincipal(identity.getPrincipal())
                    .addAttributes(identity.getAttributes())
                    .addCredentials(identity.getCredentials())
                    .addRoles(identity.getRoles());

            // add custom role source here
            builder.addRole("dummy");

            futureCompleted = cs.complete(builder.build());
        }

        return Uni.createFrom().completionStage(cs);
    }
}

