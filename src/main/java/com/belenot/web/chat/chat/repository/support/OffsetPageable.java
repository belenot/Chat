package com.belenot.web.chat.chat.repository.support;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetPageable implements Pageable, Serializable {

    private static final long serialVersionUID = -25822477129613575L;

    private int limit;
    private long offset;
    private final Sort sort;

    public static Pageable of(Pageable pageable, long offset) {
        return new OffsetPageable(pageable.getPageSize() *pageable.getPageNumber() + offset, pageable.getPageSize(), pageable.getSort());
    }
    
    public OffsetPageable(long offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }
    
    public OffsetPageable(int offset, int limit, Sort.Direction direction, String... properties) {
        this(offset, limit, new Sort(direction, properties));
    }

    public OffsetPageable(int offset, int limit) {
        this(offset, limit, new Sort(Sort.Direction.ASC,"id"));
    }

    @Override
    public int getPageNumber() {
        return (int)offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetPageable(getOffset() + getPageSize(), getPageSize(), getSort());
    }

    public OffsetPageable previous() {
        return hasPrevious() ? new OffsetPageable(getOffset() - getPageSize(), getPageSize(), getSort()) : this;
    }


    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetPageable(0, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof OffsetPageable)) return false;

        OffsetPageable that = (OffsetPageable) o;

        return new EqualsBuilder()
                .append(limit, that.limit)
                .append(offset, that.offset)
                .append(sort, that.sort)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(limit)
                .append(offset)
                .append(sort)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("limit", limit)
                .append("offset", offset)
                .append("sort", sort)
                .toString();
    }
}