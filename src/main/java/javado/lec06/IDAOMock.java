package javado.lec06;


import java.util.List;

public interface IDAOMock {

  List<Product> select();

  Product select(int id);

  void insert(Product product);

  void update(Product product);

  void delete(int id);
}
