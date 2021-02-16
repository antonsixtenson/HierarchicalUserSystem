package mauri;

public class PushTestInstitution {

    public static void main(String[] args) {
        App a = new App();
        int date = Integer.parseInt(java.time.LocalDate.now().toString().replace("-", ""));
        Institution i0 = new Institution("Badhuset", "Stockholm", "badhuset@sthlm.se",
                date);
        String password = "badhuset";
        byte [] salt = Password.genSalt();
        String sha256_pwd = Password.genPassword(salt, password);
        a.addInstitution(i0, salt, sha256_pwd);

    }

}
