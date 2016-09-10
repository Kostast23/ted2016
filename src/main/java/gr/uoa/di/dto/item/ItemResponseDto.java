package gr.uoa.di.dto.item;

import gr.uoa.di.dto.category.CategoryResponseDto;

import java.util.Date;
import java.util.List;

public class ItemResponseDto {
    private int id;
    private String name;
    private String description;
    private Integer buyprice;
    private Integer currentbid;
    private String location;
    private Double lat;
    private Double lon;
    private String country;
    private Date startDate;
    private Date endDate;
    private List<PictureDto> pictures;
    private String sellerUsername;
    private CategoryResponseDto category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Integer getCurrentbid() {
        return currentbid;
    }

    public void setCurrentbid(Integer currentbid) {
        this.currentbid = currentbid;
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

    public List<PictureDto> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureDto> pictures) {
        this.pictures = pictures;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public CategoryResponseDto getCategory() {
        return category;
    }

    public void setCategory(CategoryResponseDto category) {
        this.category = category;
    }
}
