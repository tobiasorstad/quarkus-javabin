package example4;

import io.quarkus.security.identity.SecurityIdentity;

public abstract class UserAccess {

    public static boolean isAdmin(SecurityIdentity securityIdentity){
        return securityIdentity.hasRole("EmployeeAdmin");
    }
}
