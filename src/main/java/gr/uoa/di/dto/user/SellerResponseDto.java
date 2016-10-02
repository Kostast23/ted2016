package gr.uoa.di.dto.user;

public class SellerResponseDto {
    private int id;
    private String username;
    private String location;
    private String country;
    private Integer buyerrating;
    private Integer sellerrating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Integer getBuyerrating() {
        return buyerrating;
    }

    public void setBuyerrating(Integer buyerrating) {
        this.buyerrating = buyerrating;
    }

    public Integer getSellerrating() {
        return sellerrating;
    }

    public void setSellerrating(Integer sellerrating) {
        this.sellerrating = sellerrating;
    }
}
