package javado.lec06;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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

  @GET
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  public List<Product> getProducts() {
    IDAOMock dao = DAOMock.getInstance();
    return dao.select();
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProduct(@PathParam("id") int id) {
    IDAOMock dao = DAOMock.getInstance();
    try {
      Product product = dao.select(id);
      return Response.ok(product).build();
    } catch (Exception e) {
      e.printStackTrace();
      // statusに4xxや5xxを設定してリクエストに対してHTTPのプロトコルを使い、
      // サーバの処理状態（リクエスト不正やサーバ処理エラーなど）を通知することができる
      int status = 400;
      return Response.status(status).build();
    }
  }


  // Consumesはリクエスト、ProducesはレスポンスのContent-Typeを表す
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postProducts(Product product) {
    IDAOMock dao = DAOMock.getInstance();
    try {
      dao.insert(product);
      return Response.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      int status = 400;
      return Response.status(status).build();
    }
  }

  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response putProducts(@PathParam("id") int id, Product product) {
    IDAOMock dao = DAOMock.getInstance();
    try {
      dao.update(id, product);
      return Response.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      int status = 400;
      return Response.status(status).build();
    }
  }

  @DELETE
  @Path("{id}")
  public Response deleteProduct(@PathParam("id") int id) {
    IDAOMock dao = DAOMock.getInstance();
    try {
      dao.delete(id);
      return Response.ok().build();
    } catch (Exception e) {
      e.printStackTrace();
      int status = 400;
      return Response.status(status).build();
    }
  }

}
