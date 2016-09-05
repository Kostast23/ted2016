package gr.uoa.di.dto.category;

import gr.uoa.di.dto.item.ItemListResponseDto;

import java.util.List;

public class CategoryInfoResponseDto {
    private String name;
    private long count;
    private List<ItemListResponseDto> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<ItemListResponseDto> getItems() {
        return items;
    }

    public void setItems(List<ItemListResponseDto> items) {
        this.items = items;
    }
}
