import java.util.*;

public class Password {
    private int passwdLength;
    private String password;

    //Getter and Setter
    public int getPasswdLength() {
        return passwdLength;
    }

    public void setPasswdLength(int passwdLength) {
        this.passwdLength = passwdLength;
    }

    //Constructors
    public Password(){
        setPasswdLength(16);
        this.password = "";
    }

    public Password(int passwordLength){
        setPasswdLength(passwordLength);
        this.password = "";
    }

    public Password(Password cp){
        setPasswdLength(cp.getPasswdLength());
        this.password = cp.password;
    }

    //Methods

    /**
     *Create a password by an array of char
     *
     * @return
     */
    public String genPassword(){
        Random random = new Random();
        StringBuilder str = new StringBuilder();

        final char[] characters =
                {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'e', 'h', 'i', 'j', 'l', 'k',
                        'm', 'n', 'o', 'p', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6',
                        '7', '8', '9', '0', '@', '#', '!',':', ';','+', '!'};

        for (int i = 0; i < getPasswdLength(); i++) {
            str.append(characters[random.nextInt(characters.length)]);
        }

        this.password = str.toString();
        return this.password;
    }

    @Override
    public String toString() {
        return this.password;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Password aux){
            return getPasswdLength() == aux.getPasswdLength() && this.password.equals(aux.password);
        }else return  false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPasswdLength(), this.passwdLength);
    }
}
