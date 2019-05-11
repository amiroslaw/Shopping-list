package ovh.miroslaw.shoppinglist.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "unit_of_measure")
public class UnitOfMeasure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "uom", nullable = false)
    private String uom;

    @OneToOne(mappedBy = "unitOfMeasure")
    @JsonIgnore
    private Ingredient ingredient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUom() {
        return uom;
    }

    public UnitOfMeasure uom(String uom) {
        this.uom = uom;
        return this;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public UnitOfMeasure ingredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UnitOfMeasure unitOfMeasure = (UnitOfMeasure) o;
        if (unitOfMeasure.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), unitOfMeasure.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UnitOfMeasure{" +
            "id=" + getId() +
            ", uom='" + getUom() + "'" +
            "}";
    }
}
