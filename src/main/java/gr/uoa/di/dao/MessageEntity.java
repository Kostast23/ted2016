package gr.uoa.di.dao;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;

@Entity
@Table(name = "message", schema = "public", catalog = "ted")
public class MessageEntity {
    private int id;
    private String message;
    private Boolean isread;
    private Boolean deletedsender;
    private Boolean deletedreceiver;
    private UserEntity from;
    private UserEntity to;

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
    @Column(name = "isread")
    public Boolean getIsread() {
        return isread;
    }

    public void setIsread(Boolean isread) {
        this.isread = isread;
    }

    @Basic
    @Column(name = "deletedsender")
    public Boolean getDeletedsender() {
        return deletedsender;
    }

    public void setDeletedsender(Boolean deletedsender) {
        this.deletedsender = deletedsender;
    }

    @Basic
    @Column(name = "deletedreceiver")
    public Boolean getDeletedreceiver() {
        return deletedreceiver;
    }

    public void setDeletedreceiver(Boolean deletedreceiver) {
        this.deletedreceiver = deletedreceiver;
    }

    @Basic
    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (id != that.id) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "from", referencedColumnName = "id", nullable = false)
    public UserEntity getFrom() {
        return from;
    }

    public void setFrom(UserEntity from) {
        this.from = from;
    }

    @ManyToOne
    @JoinColumn(name = "to", referencedColumnName = "id", nullable = false)
    public UserEntity getTo() {
        return to;
    }

    public void setTo(UserEntity to) {
        this.to = to;
    }
}
