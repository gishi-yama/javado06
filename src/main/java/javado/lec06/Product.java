package javado.lec06;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product {

    private int id;
    private String name;
    private int price;

    public Product() {
        this.id = 0;
        this.name = "";
        this.price = 0;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}

