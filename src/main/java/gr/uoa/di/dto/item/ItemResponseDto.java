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
    private Integer firstbid;
    private String location;
    private Double lat;
    private Double lon;
    private String country;
    private Date startDate;
    private Date endDate;
    private Boolean finished;
    private List<String> images;
    private String sellerUsername;
    private Integer sellerrating;
    private String winnerUsername;
    private Boolean sellerHappy;
    private Boolean buyerHappy;
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

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public Integer getSellerrating() {
        return sellerrating;
    }

    public void setSellerrating(Integer sellerrating) {
        this.sellerrating = sellerrating;
    }

    public String getWinnerUsername() {
        return winnerUsername;
    }

    public void setWinnerUsername(String winnerUsername) {
        this.winnerUsername = winnerUsername;
    }

    public Boolean getSellerHappy() {
        return sellerHappy;
    }

    public void setSellerHappy(Boolean sellerHappy) {
        this.sellerHappy = sellerHappy;
    }

    public Boolean getBuyerHappy() {
        return buyerHappy;
    }

    public void setBuyerHappy(Boolean buyerHappy) {
        this.buyerHappy = buyerHappy;
    }

    public CategoryResponseDto getCategory() {
        return category;
    }

    public void setCategory(CategoryResponseDto category) {
        this.category = category;
    }
}
