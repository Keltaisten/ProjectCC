package beer;

public class BrandsWithPrices {
    private String brandName;
    private int allPrice;
    private int numberOfBeersByBrand;
    private Double averagePrice;

    public BrandsWithPrices(String brandName) {
        this.brandName = brandName;
    }

    public void incrementPrice(int price) {
        this.allPrice += price;
        numberOfBeersByBrand++;
        calculateAveragePrice();
    }

    public void calculateAveragePrice() {
        averagePrice =  ((double) allPrice / numberOfBeersByBrand);
    }

    public String getBrandName() {
        return brandName;
    }

    public int getAllPrice() {
        return allPrice;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

}
