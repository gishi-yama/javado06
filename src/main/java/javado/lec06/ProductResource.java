package javado.lec06;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("product")
public class ProductResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Product getProduct() {
        Product product = new Product();
        product.setId(200);
        product.setName("Java Do");
        product.setPrice(2800);
        return product;
    }

}
