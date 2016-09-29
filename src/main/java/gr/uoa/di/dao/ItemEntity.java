package gr.uoa.di.dao;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "item", schema = "public", catalog = "ted")
public class ItemEntity {
    private int id;
    private String name;
    private String description;
    private Integer buyprice;
    private Integer currentbid;
    private Integer firstbid;
    private String location;
    private Double lat;
    private Double lon;
    private String country;
    private Date startDate;
    private Date endDate;
    private Boolean finished;
    private Boolean ratedBySeller;
    private Boolean ratedByBuyer;
    private List<ItemPicturesEntity> pictures;
    private UserEntity owner;
    private List<BidEntity> bids;
    private CategoryEntity category;
    private List<RecommendationEntity> recommendations;

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

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "buyprice")
    public Integer getBuyprice() {
        return buyprice;
    }

    public void setBuyprice(Integer buyprice) {
        this.buyprice = buyprice;
    }

    @Basic
    @Column(name = "currentbid")
    public Integer getCurrentbid() {
        return currentbid;
    }

    public void setCurrentbid(Integer currentbid) {
        this.currentbid = currentbid;
    }

    @Basic
    @Column(name = "firstbid")
    public Integer getFirstbid() {
        return firstbid;
    }

    public void setFirstbid(Integer firstbid) {
        this.firstbid = firstbid;
    }

    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "lat")
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Basic
    @Column(name = "lon")
    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "startdate")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "enddate")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "finished")
    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    @Basic
    @Column(name = "ratedseller")
    public Boolean getRatedBySeller() {
        return ratedBySeller;
    }

    public void setRatedBySeller(Boolean ratedBySeller) {
        this.ratedBySeller = ratedBySeller;
    }

    @Basic
    @Column(name = "ratedbuyer")
    public Boolean getRatedByBuyer() {
        return ratedByBuyer;
    }

    public void setRatedByBuyer(Boolean ratedByBuyer) {
        this.ratedByBuyer = ratedByBuyer;
    }
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    public List<ItemPicturesEntity> getPictures() {
        return pictures;
    }

    public void setPictures(List<ItemPicturesEntity> pictures) {
        this.pictures = pictures;
    }

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "id", nullable = false)
    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity user) {
        this.owner = user;
    }

    @OneToMany(mappedBy = "item")
    public List<BidEntity> getBids() {
        return bids;
    }

    public void setBids(List<BidEntity> bids) {
        this.bids = bids;
    }

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id", nullable = false)
    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemEntity that = (ItemEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (buyprice != null ? !buyprice.equals(that.buyprice) : that.buyprice != null) return false;
        if (currentbid != null ? !currentbid.equals(that.currentbid) : that.currentbid != null) return false;
        if (firstbid != null ? !firstbid.equals(that.firstbid) : that.firstbid != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (lat != null ? !lat.equals(that.lat) : that.lat != null) return false;
        if (lon != null ? !lon.equals(that.lon) : that.lon != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (finished != null ? !finished.equals(that.finished) : that.finished != null) return false;
        if (ratedBySeller != null ? !ratedBySeller.equals(that.ratedBySeller) : that.ratedBySeller != null)
            return false;
        if (ratedByBuyer != null ? !ratedByBuyer.equals(that.ratedByBuyer) : that.ratedByBuyer != null) return false;
        if (pictures != null ? !pictures.equals(that.pictures) : that.pictures != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
        if (bids != null ? !bids.equals(that.bids) : that.bids != null) return false;
        return category != null ? category.equals(that.category) : that.category == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (buyprice != null ? buyprice.hashCode() : 0);
        result = 31 * result + (currentbid != null ? currentbid.hashCode() : 0);
        result = 31 * result + (firstbid != null ? firstbid.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (lon != null ? lon.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (finished != null ? finished.hashCode() : 0);
        result = 31 * result + (ratedBySeller != null ? ratedBySeller.hashCode() : 0);
        result = 31 * result + (ratedByBuyer != null ? ratedByBuyer.hashCode() : 0);
        result = 31 * result + (pictures != null ? pictures.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (bids != null ? bids.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "item")
    public List<RecommendationEntity> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<RecommendationEntity> recommendations) {
        this.recommendations = recommendations;
    }
}
