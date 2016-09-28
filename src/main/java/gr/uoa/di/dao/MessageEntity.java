package gr.uoa.di.dao;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message", schema = "public", catalog = "ted")
public class MessageEntity {
    private int id;
    private String message;
    private String subject;
    private Boolean isread;
    private Boolean deletedsender;
    private Boolean deletedreceiver;
    private Date sentdate;
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

    @Basic
    @Column(name = "subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (id != that.id) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        if (isread != null ? !isread.equals(that.isread) : that.isread != null) return false;
        if (deletedsender != null ? !deletedsender.equals(that.deletedsender) : that.deletedsender != null)
            return false;
        if (deletedreceiver != null ? !deletedreceiver.equals(that.deletedreceiver) : that.deletedreceiver != null)
            return false;
        if (sentdate != null ? !sentdate.equals(that.sentdate) : that.sentdate != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        return to != null ? to.equals(that.to) : that.to == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (isread != null ? isread.hashCode() : 0);
        result = 31 * result + (deletedsender != null ? deletedsender.hashCode() : 0);
        result = 31 * result + (deletedreceiver != null ? deletedreceiver.hashCode() : 0);
        result = 31 * result + (sentdate != null ? sentdate.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "from_user", referencedColumnName = "id", nullable = false)
    public UserEntity getFrom() {
        return from;
    }

    public void setFrom(UserEntity from) {
        this.from = from;
    }

    @Basic
    @Column(name = "sentdate")
    public Date getSentdate() {
        return sentdate;
    }

    public void setSentdate(Date sentdate) {
        this.sentdate = sentdate;
    }

    @ManyToOne
    @JoinColumn(name = "to_user", referencedColumnName = "id", nullable = false)
    public UserEntity getTo() {
        return to;
    }

    public void setTo(UserEntity to) {
        this.to = to;
    }
}
