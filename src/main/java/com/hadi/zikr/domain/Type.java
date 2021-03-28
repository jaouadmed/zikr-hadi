package com.hadi.zikr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Employee entity.
 */
@ApiModel(description = "The Employee entity.")
@Entity
@Table(name = "type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "color")
    private String color;

    @OneToMany(mappedBy = "type")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "type" }, allowSetters = true)
    private Set<Zikr> zikrs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Type title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return this.color;
    }

    public Type color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Set<Zikr> getZikrs() {
        return this.zikrs;
    }

    public Type zikrs(Set<Zikr> zikrs) {
        this.setZikrs(zikrs);
        return this;
    }

    public Type addZikr(Zikr zikr) {
        this.zikrs.add(zikr);
        zikr.setType(this);
        return this;
    }

    public Type removeZikr(Zikr zikr) {
        this.zikrs.remove(zikr);
        zikr.setType(null);
        return this;
    }

    public void setZikrs(Set<Zikr> zikrs) {
        if (this.zikrs != null) {
            this.zikrs.forEach(i -> i.setType(null));
        }
        if (zikrs != null) {
            zikrs.forEach(i -> i.setType(this));
        }
        this.zikrs = zikrs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Type)) {
            return false;
        }
        return id != null && id.equals(((Type) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Type{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", color='" + getColor() + "'" +
            "}";
    }
}
