package de.test.automatedTests.managers;

public enum loginDates {
    USER_ADMIN("admin", "654321"),
    USER_BED_PASS_GOOD("admiN","654321"),
    USER_BED_PASS_BED("ADMIN","123456"),
    USER_GOOD_PASS_BED("admin","132465"),
    USER2_BED_PASS_GOOD("admIN","654321"),
    USER3_BED_PASS_GOOD("adMIN","654321");


    private final String username;
    private final String password;

    loginDates(String username, String password) {

        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {

        return password;
    }

}
