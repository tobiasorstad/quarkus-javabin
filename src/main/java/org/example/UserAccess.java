package org.example;

import io.quarkus.security.identity.SecurityIdentity;

public class UserAccess {

    public static boolean IsAdmin(SecurityIdentity securityIdentity){
        return securityIdentity.hasRole("EmployeeAdmin");
    }
}
