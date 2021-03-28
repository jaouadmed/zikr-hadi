package com.hadi.zikr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Zikr.
 */
@Entity
@Table(name = "zikr")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Zikr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "count")
    private Long count;

    @ManyToOne
    @JsonIgnoreProperties(value = { "zikrs" }, allowSetters = true)
    private Type employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Zikr id(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public Zikr content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCount() {
        return this.count;
    }

    public Zikr count(Long count) {
        this.count = count;
        return this;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Type getEmployee() {
        return this.employee;
    }

    public Zikr employee(Type type) {
        this.setEmployee(type);
        return this;
    }

    public void setEmployee(Type type) {
        this.employee = type;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Zikr)) {
            return false;
        }
        return id != null && id.equals(((Zikr) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Zikr{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", count=" + getCount() +
            "}";
    }
}
