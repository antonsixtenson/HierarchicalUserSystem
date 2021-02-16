package mauri;

public class CheckInputs {

    /**
     * @param mail Takes String
     * @return True or False, depending on if the string matches a email pattern
     */

    public static boolean checkMail(String mail) {
        return !mail.equals("") && mail.contains("@") && mail.contains(".");
    }

    /**
     * @param str Takes String
     * @return Returns false if string contains anything but letters
     */

    public static boolean checkString(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param password Takes String
     * @return False if length is less than 6
     */

    public static boolean checkPasswordLength(String password) {
        if (password.length() < 6) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean checkIfBlank(String str) {
        return !str.isBlank();
    }

}

