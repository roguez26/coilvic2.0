package mx.fei.coilvicapp.logic.user;

import java.util.Objects;
/**
 *
 * @author ivanr
 */
public class User {

    private int idUser;
    private String password;
    private String type;
    private int idProfessor;

    public int getIdUser() {
        return idUser;
    }
    
    public int getIdProfessor() {
        return idProfessor;
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
    
    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return idUser == user.idUser
                && Objects.equals(password, user.password)
                && Objects.equals(type, user.type);
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
