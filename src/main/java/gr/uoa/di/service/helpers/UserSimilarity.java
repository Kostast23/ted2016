package gr.uoa.di.service.helpers;

public class UserSimilarity {
    private Integer user;
    private double similarity;

    public UserSimilarity(Integer user, double similarity) {
        this.user = user;
        this.similarity = similarity;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}
