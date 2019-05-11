package ovh.miroslaw.shoppinglist.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the UnitOfMeasure entity.
 */
public class UnitOfMeasureDTO implements Serializable {

    private Long id;

    @NotNull
    private String uom;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UnitOfMeasureDTO unitOfMeasureDTO = (UnitOfMeasureDTO) o;
        if (unitOfMeasureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), unitOfMeasureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UnitOfMeasureDTO{" +
            "id=" + getId() +
            ", uom='" + getUom() + "'" +
            "}";
    }
}
