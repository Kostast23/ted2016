package gr.uoa.di.dto.item;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

public class ItemEditDto {
    @NotNull
    @Size(min=3)
    private String name;
    @NotNull
    @Size(min=10)
    private String description;
    @Min(1)
    private Integer buyprice;
    @NotNull
    @Min(0)
    private Integer firstbid;
    @NotNull
    @Size(min=2)
    private String location;
    @NotNull
    @Min(-90)
    @Max(90)
    private Double lat;
    @NotNull
    @Min(-180)
    @Max(180)
    private Double lon;
    @NotNull
    @Size(min=2)
    private String country;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    private List<String> images;
    @NotNull
    private Integer category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBuyprice() {
        return buyprice;
    }

    public void setBuyprice(Integer buyprice) {
        this.buyprice = buyprice;
    }

    public Integer getFirstbid() {
        return firstbid;
    }

    public void setFirstbid(Integer firstbid) {
        this.firstbid = firstbid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
