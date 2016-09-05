package gr.uoa.di.dto.item;

import java.util.Date;

public class ItemListResponseDto {
    private int id;
    private String name;
    private Integer buyprice;
    private Integer currentbid;
    private String location;
    private String country;
    private Date endDate;
    private PictureDto picture;
    private String sellerUsername;

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public PictureDto getPicture() {
        return picture;
    }

    public void setPicture(PictureDto picture) {
        this.picture = picture;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }
}
