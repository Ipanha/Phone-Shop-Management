package rupp;

public class Phone {
    private String name;
    private double price;
    private String imagePath;
    private int qty;

    public Phone(String name, double price, int Qty, String imagePath) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.qty = Qty;

    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

}
