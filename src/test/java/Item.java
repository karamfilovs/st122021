public class Item {
    public String getName() {
        return name;
    }

    public String getPrice_for_quantity() {
        return price_for_quantity;
    }

    public String getQuantity_unit() {
        return quantity_unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice_for_quantity(String price_for_quantity) {
        this.price_for_quantity = price_for_quantity;
    }

    public void setQuantity_unit(String quantity_unit) {
        this.quantity_unit = quantity_unit;
    }

    private String name;
    private String price_for_quantity;
    private String quantity_unit;

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private double price;


    public Item(String name, String price_for_quantity, double price, String quantity_unit){
        this.name = name;
        this.price_for_quantity = price_for_quantity;
        this.price = price;
        this.quantity_unit = quantity_unit;
    }




}
