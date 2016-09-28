package gr.uoa.di.service.helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemRecommendations {
    private Map<Integer, Double> recommendations;

    public ItemRecommendations() {
        recommendations = new HashMap<>();
    }

    public void addRecommendation(Integer item, Double value) {
        Double oldValue = recommendations.computeIfAbsent(item, i -> 0.0);
        recommendations.put(item, oldValue + value);
    }

    public List<Integer> getTop(int n) {
        return recommendations.entrySet().stream()
                .sorted((o1, o2) -> Double.compare(o2.getValue(), o1.getValue()))
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}