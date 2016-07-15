package javado.lec06;

import java.util.*;
import java.util.stream.Collectors;

/**
 * データベースの処理を模したMockクラス。シングルトンオブジェクトを生成する。
 */
public class DAOMock implements IDAOMock {

    private static DAOMock instance = new DAOMock();

    private Map<Integer, Product> productMap;

    private DAOMock() {
        productMap = new HashMap<>();

        Product product1 = new Product();
        product1.setName("孤独のグルメ 【新装版】");
        product1.setPrice(1234);
        insert(product1);

        Product product2 = new Product();
        product2.setName("孤独のグルメ2");
        product2.setPrice(994);
        insert(product2);
    }

    public static DAOMock getInstance() {
        return instance;
    }

    @Override
    public List<Product> select() {
        List<Product> products = productMap.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(Product::getId))
                .collect(Collectors.toList());
        return products;
    }

    @Override
    public Product select(int id) {
        Optional<Product> product = productMap.entrySet()
                .stream()
                .filter(e -> Objects.equals(e.getKey(), id))
                .map(e -> e.getValue())
                .findFirst();
        return product.orElseThrow(() -> new IllegalArgumentException("検索しようとしたidが存在しません:" + id));
    }

    @Override
    public synchronized void insert(Product product) {
        int maxId = productMap.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .max((a, b) -> a.compareTo(b)).orElse(0);
        int newId = maxId + 1;
        product.setId(newId);
        productMap.put(newId, product);
    }

    @Override
    public synchronized void update(Product product) {
        int id = product.getId();
        if (count(id) > 0) {
            productMap.replace(id, product);
            return;
        }
        throw new IllegalArgumentException("更新しようとしたidが存在しません:" + id);
    }

    @Override
    public synchronized void delete(int id) {
        if (count(id) <= 0) {
            productMap.remove(id);
            return;
        }
        throw new IllegalArgumentException("削除しようとしたidが存在しません:" + id);

    }

    protected long count(int id) {
        return productMap.entrySet()
                .stream()
                .filter(e -> Objects.equals(e.getKey(), id))
                .count();
    }

}
