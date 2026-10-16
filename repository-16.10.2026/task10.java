interface ValidationStrategy {
    boolean isValid(String password);
}

class LengthValidator implements ValidationStrategy {
    public boolean isValid(String password) {
        if (password.length() >= 8) {
            return true;
        }
        return false;
    }
}

class DigitValidator implements ValidationStrategy {
    public boolean isValid(String password) {
        char[] chars = password.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                return true;
            }
        }
        return false;
    }
}

class NoSpaceValidator implements ValidationStrategy {
    public boolean isValid(String password) {
        if (password.indexOf(" ") == -1) {
            return true;
        }
        return false;
    }
}