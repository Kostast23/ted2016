package gr.uoa.di.dao;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "item_pictures", schema = "public", catalog = "ted")
public class ItemPicturesEntity {
    private String filename;
    private byte[] image;
    private ItemEntity item;

    @Id
    @Column(name = "filename")
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Basic
    @Column(name = "image")
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemPicturesEntity that = (ItemPicturesEntity) o;

        if (filename != null ? !filename.equals(that.filename) : that.filename != null) return false;
        if (!Arrays.equals(image, that.image)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = filename != null ? filename.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(image);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "item", referencedColumnName = "id", nullable = false)
    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }
}
