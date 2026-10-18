package edu.spms.web.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TokenStoreTest {
    @Test void issuesAndRevokesOpaqueTokens(){TokenStore store=new TokenStore();SpmsPrincipal principal=new SpmsPrincipal(7,"demo","HR_MANAGER",null,false);String token=store.issue(principal);assertNotNull(token);assertEquals(principal,store.resolve(token));store.revoke(token);assertNull(store.resolve(token));}
    @Test void canRevokeAllSessionsForAnAccount(){TokenStore store=new TokenStore();String first=store.issue(new SpmsPrincipal(7,"demo","HR_MANAGER",null,false));String second=store.issue(new SpmsPrincipal(8,"other","ADMIN",null,false));store.revokeUser(7);assertNull(store.resolve(first));assertNotNull(store.resolve(second));}
}
