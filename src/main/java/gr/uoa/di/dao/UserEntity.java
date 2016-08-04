package gr.uoa.di.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user", schema = "public", catalog = "ted")
public class UserEntity {
    private int id;
    private String username;
    private String password;
    private String salt;
    private Boolean admin;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String afm;
    private String location;
    private Double lat;
    private Double lon;
    private String country;
    private Integer buyerrating = 0;
    private Integer sellerrating = 0;
    private Boolean validated = false;
    private List<MessageEntity> sentMessages;
    private List<MessageEntity> receivedMessages;
    private List<BidEntity> bids;
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
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "salt")
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Basic
    @Column(name = "admin")
    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
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
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "afm")
    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
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
    @Column(name = "buyerrating")
    public Integer getBuyerrating() {
        return buyerrating;
    }

    public void setBuyerrating(Integer buyerrating) {
        this.buyerrating = buyerrating;
    }

    @Basic
    @Column(name = "sellerrating")
    public Integer getSellerrating() {
        return sellerrating;
    }

    public void setSellerrating(Integer sellerrating) {
        this.sellerrating = sellerrating;
    }

    @Basic
    @Column(name = "validated")
    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (salt != null ? !salt.equals(that.salt) : that.salt != null) return false;
        if (admin != null ? !admin.equals(that.admin) : that.admin != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (afm != null ? !afm.equals(that.afm) : that.afm != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (lat != null ? !lat.equals(that.lat) : that.lat != null) return false;
        if (lon != null ? !lon.equals(that.lon) : that.lon != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (buyerrating != null ? !buyerrating.equals(that.buyerrating) : that.buyerrating != null) return false;
        if (sellerrating != null ? !sellerrating.equals(that.sellerrating) : that.sellerrating != null) return false;
        if (validated != null ? !validated.equals(that.validated) : that.validated != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (admin != null ? admin.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (afm != null ? afm.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (lon != null ? lon.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (buyerrating != null ? buyerrating.hashCode() : 0);
        result = 31 * result + (sellerrating != null ? sellerrating.hashCode() : 0);
        result = 31 * result + (validated != null ? validated.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "from")
    public List<MessageEntity> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<MessageEntity> sentMessages) {
        this.sentMessages = sentMessages;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "to")
    public List<MessageEntity> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<MessageEntity> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    public List<BidEntity> getBids() {
        return bids;
    }

    public void setBids(List<BidEntity> bids) {
        this.bids = bids;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
}
