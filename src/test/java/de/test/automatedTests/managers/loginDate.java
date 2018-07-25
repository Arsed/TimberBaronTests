package de.test.automatedTests.managers;

public enum loginDate {
    USER_ADMIN("admin", "654321", true);


    private final String username;
    private final String password;
    private final Boolean flag;

    loginDate(String username, String password, Boolean flag) {

        this.username = username;
        this.password = password;
        this.flag = flag;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {

        return password;
    }

    public boolean getFlag() {
        return flag;
    }
}
