package ovh.miroslaw.shoppinglist.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/**
 * Entity class for Authority.
 */
@Entity
public class Authority implements Serializable {

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

    public Authority() {
    }

    public Authority(String user) {
        this.name = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Authority authority = (Authority) o;

        return Objects.equals(name, authority.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Authority{" +
               "name='" + name + '\'' +
               "}";
    }
}
