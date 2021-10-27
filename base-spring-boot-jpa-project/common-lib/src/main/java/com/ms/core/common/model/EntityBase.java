package com.ms.core.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EntityBase implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final Integer STATUS_INACTIVE = 1;
    public static final Integer STATUS_ACTIVE = 0;
    public static final Integer STATUS_DELETED = -1;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private Integer status = 0; //default status

    public EntityBase() {
    }

    public EntityBase(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EntityBase)) {
            return false;
        }
        EntityBase other = (EntityBase) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "EntityBase{" +
                "id=" + id +
                '}';
    }


}
