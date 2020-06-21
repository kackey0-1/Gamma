package it.chalmers.gamma.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Objects;

public class GoldappsITUserDTO {
    private String nick;
    private String mail;
    @JsonAlias("second_name")
    private String secondName;
    @JsonAlias("first_name")
    private String firstName;
    @JsonAlias("gdpr_education")
    private boolean gdprEducation;
    private String cid;

    public GoldappsITUserDTO(ITUserDTO userDTO) {
        this.nick = userDTO.getNick();
        this.mail = userDTO.getEmail();
        this.secondName = userDTO.getLastName();
        this.firstName = userDTO.getFirstName();
        this.gdprEducation = userDTO.isGdpr();
        this.cid = userDTO.getCid();
    }

    public GoldappsITUserDTO() { // Needed for Jackson serialization

    }

    public String getNick() {
        return nick;
    }

    public String getMail() {
        return mail;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean isGdprEducation() {
        return gdprEducation;
    }

    public String getCid() {
        return cid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GoldappsITUserDTO that = (GoldappsITUserDTO) o;
        return this.gdprEducation == that.gdprEducation
                && Objects.equals(this.nick, that.nick)
                && Objects.equals(this.mail, that.mail)
                && Objects.equals(this.secondName, that.secondName)
                && Objects.equals(this.firstName, that.firstName)
                && Objects.equals(this.cid, that.cid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.nick,
                this.mail,
                this.secondName,
                this.firstName,
                this.gdprEducation,
                this.cid);
    }

    @Override
    public String toString() {
        return "GoldappsITUserDTO{"
                + "nick='" + this.nick + '\''
                + ", mail='" + this.mail + '\''
                + ", secondName='" + this.secondName + '\''
                + ", firstName='" + this.firstName + '\''
                + ", gdprEducation=" + this.gdprEducation
                + ", cid='" + this.cid + '\''
                + '}';
    }
}
