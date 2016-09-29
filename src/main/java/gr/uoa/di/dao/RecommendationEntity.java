package gr.uoa.di.dao;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@Entity
@Table(name = "recommendation", schema = "public", catalog = "ted")
public class RecommendationEntity {
    private int id;
    private double recValue;
    private UserEntity user;
    private ItemEntity item;

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
    @Column(name = "rec_value")
    public double getRecValue() {
        return recValue;
    }

    public void setRecValue(double recValue) {
        this.recValue = recValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecommendationEntity that = (RecommendationEntity) o;

        if (Double.compare(that.recValue, recValue) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(recValue);
        return (int) (temp ^ (temp >>> 32));
    }

    @ManyToOne
    @JoinColumn(name = "for_user", referencedColumnName = "id", nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "item", referencedColumnName = "id", nullable = false)
    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }
}
