package com.hadi.zikr.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.hadi.zikr.domain.Zikr} entity. This class is used
 * in {@link com.hadi.zikr.web.rest.ZikrResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /zikrs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ZikrCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter count;

    private LongFilter typeId;

    public ZikrCriteria() {}

    public ZikrCriteria(ZikrCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.count = other.count == null ? null : other.count.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
    }

    @Override
    public ZikrCriteria copy() {
        return new ZikrCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getCount() {
        return count;
    }

    public LongFilter count() {
        if (count == null) {
            count = new LongFilter();
        }
        return count;
    }

    public void setCount(LongFilter count) {
        this.count = count;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public LongFilter typeId() {
        if (typeId == null) {
            typeId = new LongFilter();
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ZikrCriteria that = (ZikrCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(count, that.count) && Objects.equals(typeId, that.typeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count, typeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ZikrCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (count != null ? "count=" + count + ", " : "") +
            (typeId != null ? "typeId=" + typeId + ", " : "") +
            "}";
    }
}
