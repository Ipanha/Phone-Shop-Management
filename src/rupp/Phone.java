package rupp;

public class Phone {
    private String name;
    private double price;
    private String imagePath;
    private int qty;

    public Phone(String name, double price, int qty, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
