package mx.fei.coilvicapp.logic.user;

import java.util.Objects;
import mx.fei.coilvicapp.logic.professor.Professor;
/**
 *
 * @author ivanr
 */
public class User {

    private int idUser;
    private String password;
    private String type;
    
    public User() {

    }
    

    public int getIdUser() {
        return idUser;
    }
    


    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }
    


    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;
        
        if (this == object) {
            isEqual = true;
        } else if (object != null && getClass() == object.getClass()) {
            User toCompare = (User) object;
            isEqual = idUser == toCompare.idUser
                && Objects.equals(password, toCompare.password)
                && Objects.equals(type, toCompare.type);
        }
        return isEqual;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idUser, password, type);
    }

    @Override
    public String toString() {
        return "User{"
                + "idUser=" + idUser
                + ", password='" + password + '\''
                + ", type='" + type + '\''
                + '}';
    }

}
