package gr.uoa.di.dao;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category", schema = "public", catalog = "ted")
public class CategoryEntity {
    private int id;
    private String name;
    private CategoryEntity parentCategory;
    private List<CategoryEntity> subcategories;
    private List<ItemEntity> items;

    @Id
    @Generated(GenerationTime.INSERT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryEntity that = (CategoryEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "parent_category", referencedColumnName = "id")
    public CategoryEntity getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(CategoryEntity parentCategory) {
        this.parentCategory = parentCategory;
    }

    @OneToMany(mappedBy = "parentCategory")
    public List<CategoryEntity> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<CategoryEntity> subcategories) {
        this.subcategories = subcategories;
    }

    @OneToMany(mappedBy = "category")
    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
}

