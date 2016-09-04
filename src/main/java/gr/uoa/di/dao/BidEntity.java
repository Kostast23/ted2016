package gr.uoa.di.dao;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bid", schema = "public", catalog = "ted")
public class BidEntity {
    private int id;
    private Date time;
    private Integer amount;
    private ItemEntity item;
    private UserEntity owner;

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
    @Column(name = "time")
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Basic
    @Column(name = "amount")
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BidEntity bidEntity = (BidEntity) o;

        if (id != bidEntity.id) return false;
        if (time != null ? !time.equals(bidEntity.time) : bidEntity.time != null) return false;
        if (amount != null ? !amount.equals(bidEntity.amount) : bidEntity.amount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "item", referencedColumnName = "id", nullable = false)
    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id", nullable = false)
    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity user) {
        this.owner = user;
    }
}
