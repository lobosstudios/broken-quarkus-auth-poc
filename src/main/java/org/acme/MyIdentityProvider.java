package org.acme;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
@Priority(1002)
public class MyIdentityProvider implements IdentityProvider<UsernamePasswordAuthenticationRequest> {
    private static final Map<String, String> CREDENTIALS = Map.of("bob", "bob");

    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    public Uni<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest request,
                                              AuthenticationRequestContext authenticationRequestContext) {
        if (new String(request.getPassword().getPassword()).equals(CREDENTIALS.get(request.getUsername()))) {
            return Uni.createFrom().item(QuarkusSecurityIdentity.builder()
                    .setPrincipal(new QuarkusPrincipal(request.getUsername()))
                    .addCredential(request.getPassword())
                    .setAnonymous(false)
                    .addRole("admin")
                    .build());
        }
        throw new AuthenticationFailedException("password invalid or user not found");
    }
}

