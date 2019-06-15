package ovh.miroslaw.shoppinglist.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "ingredient")
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    private Integer popularity;

    @JsonIgnore
    @ManyToMany(mappedBy = "purchasedIngredients")
    private Set<User> purchasedIngredients = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Ingredient name(String name) {
        this.name = name.toLowerCase();
        return this;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

//    @OneToOne
//    @JoinColumn(unique = true)
//    private UnitOfMeasure unitOfMeasure;

//    public UnitOfMeasure getUnitOfMeasure() {
//        return unitOfMeasure;
//    }
//
//    public Ingredient unitOfMeasure(UnitOfMeasure unitOfMeasure) {
//        this.unitOfMeasure = unitOfMeasure;
//        return this;
//    }
//
//    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
//        this.unitOfMeasure = unitOfMeasure;
//    }
//
//    public Ingredient removeUserfromPurchasedIngredients(User user) {
//        purchasedIngredients.remove(user);
//        user.getPurchasedIngredients().remove(this);
//        return this;
//    }
//
//    public Ingredient addUserToPurchasedIngredients(User user) {
//        purchasedIngredients.add(user);
//        return this;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(popularity, that.popularity) &&
            Objects.equals(purchasedIngredients, that.purchasedIngredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, popularity, purchasedIngredients);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", popularity=" + popularity +
            '}';
    }
}
