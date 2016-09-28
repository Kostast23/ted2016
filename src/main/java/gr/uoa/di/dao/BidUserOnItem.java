package gr.uoa.di.dao;

public class BidUserOnItem {
    private Integer userId;
    private Integer itemId;

    public BidUserOnItem(Integer userId, Integer itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
