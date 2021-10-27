package com.jcom.product.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ms.core.common.model.EntityBase;

/**
 * A Producto.
 */
@Entity
@Table(name = "product")
public class Product extends EntityBase {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 200)
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "sellerid")
    private Long sellerid;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getSellerid() {
		return sellerid;
	}

	public void setSellerid(Long sellerid) {
		this.sellerid = sellerid;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", sellerid=" + getSellerid() +
            "}";
    }
    
}
