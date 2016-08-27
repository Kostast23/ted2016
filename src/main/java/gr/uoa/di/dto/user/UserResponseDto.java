package gr.uoa.di.dto.user;

public class UserResponseDto {
    private int id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String telephone;
    private String location;
    private String country;
    private Double latitude;
    private Double longitude;
    private String afm;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
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
