package it.chalmers.gamma.domain.dto.group;

import java.util.List;
import java.util.Objects;

public class GoldappsGroupsDTO {
    private String aliases;
    private List<String> members;
    private String email;
    private String type;
    private boolean expendable;

    public GoldappsGroupsDTO(String aliases, List<String> members, String email, String type, boolean expendable) {
        this.aliases = aliases;
        this.members = members;
        this.email = email;
        this.type = type;
        this.expendable = expendable;
    }

    public GoldappsGroupsDTO() {
    }

    public String getAliases() {
        return this.aliases;
    }

    public List<String> getMembers() {
        return this.members;
    }

    public String getEmail() {
        return this.email;
    }

    public String getType() {
        return this.type;
    }

    public boolean isExpendable() {
        return this.expendable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GoldappsGroupsDTO that = (GoldappsGroupsDTO) o;
        return this.expendable == that.expendable
                && Objects.equals(this.aliases, that.aliases)
                && Objects.equals(this.members, that.members)
                && Objects.equals(this.email, that.email)
                && Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.aliases, this.members, this.email, this.type, this.expendable);
    }

    @Override
    public String toString() {
        return "GoldappsGroupsDTO{"
                + "aliases='" + this.aliases + '\''
                + ", members=" + this.members
                + ", email='" + this.email + '\''
                + ", type='" + this.type + '\''
                + ", expendable=" + this.expendable
                + '}';
    }
}
