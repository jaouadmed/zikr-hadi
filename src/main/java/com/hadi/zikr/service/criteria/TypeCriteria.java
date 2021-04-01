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
 * Criteria class for the {@link com.hadi.zikr.domain.Type} entity. This class is used
 * in {@link com.hadi.zikr.web.rest.TypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter color;

    private StringFilter img;

    private LongFilter zikrId;

    public TypeCriteria() {}

    public TypeCriteria(TypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.color = other.color == null ? null : other.color.copy();
        this.img = other.img == null ? null : other.img.copy();
        this.zikrId = other.zikrId == null ? null : other.zikrId.copy();
    }

    @Override
    public TypeCriteria copy() {
        return new TypeCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getColor() {
        return color;
    }

    public StringFilter color() {
        if (color == null) {
            color = new StringFilter();
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public StringFilter getImg() {
        return img;
    }

    public StringFilter img() {
        if (img == null) {
            img = new StringFilter();
        }
        return img;
    }

    public void setImg(StringFilter img) {
        this.img = img;
    }

    public LongFilter getZikrId() {
        return zikrId;
    }

    public LongFilter zikrId() {
        if (zikrId == null) {
            zikrId = new LongFilter();
        }
        return zikrId;
    }

    public void setZikrId(LongFilter zikrId) {
        this.zikrId = zikrId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TypeCriteria that = (TypeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(color, that.color) &&
            Objects.equals(img, that.img) &&
            Objects.equals(zikrId, that.zikrId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, color, img, zikrId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (color != null ? "color=" + color + ", " : "") +
            (img != null ? "img=" + img + ", " : "") +
            (zikrId != null ? "zikrId=" + zikrId + ", " : "") +
            "}";
    }
}
