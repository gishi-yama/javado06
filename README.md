# Java Do #06

## Mavenプロジェクトを作成しよう

### ① 内蔵されたHTTPサーバ(Grizzly)で動作させる場合

```bash
mvn archetype:generate -DarchetypeArtifactId=jersey-quickstart-grizzly2 \
  -DarchetypeGroupId=org.glassfish.jersey.archetypes -DinteractiveMode=false \
  -DgroupId=javado -DartifactId=javado06 -Dpackage=javado.lec06 \
  -DarchetypeVersion=2.23.1
```

### ② Tomcatなどにwarを配置して動作させる場合

```bash
mvn archetype:generate -DarchetypeArtifactId=jersey-quickstart-webapp \
  -DarchetypeGroupId=org.glassfish.jersey.archetypes -DinteractiveMode=false \
  -DgroupId=javado -DartifactId=javado06 -Dpackage=javado.lec06 \
  -DarchetypeVersion=2.23.1
```

このハンズオンでは、環境の影響を受けづらい①がオススメです。

## MavenプロジェクトをIDEで読み込もう

### IntelliJ

- Import Project
 - javado06/pom.xml を選択
 - 上から3番目のImport Maven projects automatically にチェック
 - あとは指示に従ってNextしていく。（JDKの設定ができていることだけ注意）
 
### NetBeans

- ファイル > プロジェクトを開く
  - javado06 を選択

### Eclipse

- File > Import...（日本語化していれば ファイル > インポート）
  - Maven > Existing Maven Project  
（日本語化していれば、 Maven > 既存のMavenプロジェクト）
  - Browse > javado06 を選択


## pom.xmlのJavaのバージョンを上げよう

pom.xml の以下の部分を修正（1.7 → 1.8）

```xml
...
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>2.5.1</version>
  <inherited>true</inherited>
  <configuration>
    <source>1.8</source>
    <target>1.8</target>
  </configuration>
</plugin>
...
```

## 起動の確認をしよう

javado.lec06.Main を実行する。

コンソールに `情報: [HttpServer] Started.` と表示されたら、ブラウザを起動して、http://localhost:8080/myapp/myresource にアクセスする。

`Got it!` と表示されればOK。

## index.htmlを表示しよう

Main.javaのmainメソッドに、2行追加。

```java
public static void main(String[] args) throws IOException {
  final HttpServer server = startServer();
  
  //以下の行を追加
       server.getServerConfiguration()
                .addHttpHandler(new CLStaticHttpHandler(Main.class.getClassLoader(), "/"), "/static");
  
  //以下略...
}
```

src/main/resources フォルダを作成し、index.htmlを複製

```html
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
</head>
<body>
HTML!
</body>
</html>
```

javado.lec06.Main を実行しなおす。

http://localhost:8080/static/ にアクセスし、`HTML!`と表示されればOK。

## 自分でResourceを作ってみよう

```java
package javado.lec06;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("calc")
public class CalcResource {

    @Path("add/{a}/{b}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public int add(@PathParam("a") int a, @PathParam("b") int b) {
        return a + b;
    }
}
```

javado.lec06.Main を実行しなおす。

http://localhost:8080/myapp/calc/add/2/3 にアクセスして、`5`が表示されればOK。

### 演習

次のURLで整数のかけ算の結果`6`をテキストで返すように、CalcResourceを書き換えてみてください。

http://localhost:8080/myapp/calc/div/2/3

## ResourceでXMLをレスポンスしてみよう

Productクラスを作成する。

```java
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
```

ProductResource クラスを作成する。

```java
package javado.lec06;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("product")
public class ProductResource {

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Product getProduct() {
        Product product = new Product();
        product.setName("Java Do");
        product.setPrice(2800);
        return product;
    }

}
```

javado.lec06.Main を実行しなおす。

http://localhost:8080/myapp/product にアクセスして、下のようなXMLが表示されればOK。

```xml
<product>
<id>200</id>
<name>Java Do</name>
<price>2800</price>
</product>
```

## ResourceでJSONのレスポンスを返してみよう

pom.xml の dependency を追加する。

```xml
...
<dependencies>
  <dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-grizzly2-http</artifactId>
  </dependency>

  <!-- 下の4行を追加 -->
  <dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-json-jackson</artifactId>
  </dependency>
  ...
```

ProductResource クラスの @Produces の値を `MediaType.APPLICATION_JSON` に変更する。

javado.lec06.Main を実行しなおす。

http://localhost:8080/myapp/product にアクセスして、下のようなJSONが表示されればOK。

```json
{"id":200,"name":"Java Do","price":2800}
```

## 商品の一連の操作を実装してみよう（前準備）

### DAOMockの準備

ハンズオンの中でDatabaseの代わりに動作する DAOMock クラスを作成する。（時間が無い人はコピーで）

IDAOMock.java を作成する。

```java
package javado.lec06;


import java.util.List;

public interface IDAOMock {

    List<Product> select();

    Product select(int id);

    void insert(Product product);

    void update(Product product);

    void delete(int id);
}
```

IDAOMock.java を作成する。


```java
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

```

ProductResource クラスに次のメソッドを追加する。

```java
@GET
@Path("all")
@Produces(MediaType.APPLICATION_JSON)
public List<Product> getProducts() {
  IDAOMock dao = DAOMock.getInstance();
  return dao.select();
}
```

javado.lec06.Main を実行しなおす。

http://localhost:8080/myapp/product/all にブラウザでアクセスして、下のようなJSONが表示されればOK。

```json
[{"id":1,"name":"孤独のグルメ 【新装版】","price":1234},{"id":2,"name":"孤独のグルメ2","price":994}]
```

### Rest Client を準備

以下のサイトを参考にして、Chrome, Firefoxプラグインか、アプリケーションをダウンロードする。

- [https://developer.ntt.com/ja/blog/REST-APIを使った開発のお供に。クライアントソフトウェアまとめ](https://developer.ntt.com/ja/blog/REST-API%E3%82%92%E4%BD%BF%E3%81%A3%E3%81%9F%E9%96%8B%E7%99%BA%E3%81%AE%E3%81%8A%E4%BE%9B%E3%81%AB%E3%80%82%E3%82%AF%E3%83%A9%E3%82%A4%E3%82%A2%E3%83%B3%E3%83%88%E3%82%BD%E3%83%95%E3%83%88%E3%82%A6%E3%82%A7%E3%82%A2%E3%81%BE%E3%81%A8%E3%82%81)

#### Chrome 

[Chrome Web Store](https://chrome.google.com/webstore/detail/advanced-rest-client/hgmloofddffdnphfgcellkdfbfbjeloo) からインストール。

#### Firefox

[Firefox Addons](https://addons.mozilla.org/ja/firefox/addon/restclient/)からインストール。

#### wiztools/rest-client

https://github.com/wiztools/rest-client にアクセス。

Download の [releases](https://github.com/wiztools/rest-client/releases) をクリックして、 restclient-ui-fat-3.6.jar をダウンロード。

ダウンロードしたjarを起動する。(Macの場合、右クリックで「開く」)

#### 動作確認

javado.lec06.Main を実行しなおす。

利用するRest Clientで、 http://localhost:8080/myapp/product/all の URL をGet で開く。

Body のタブに下のようなJSONが表示されていればOK。

```json
[{"id":1,"name":"孤独のグルメ 【新装版】","price":1234},{"id":2,"name":"孤独のグルメ2","price":994}]
```

## 商品を取得

ProductResource クラスに次のメソッドを追加する。

```java
@GET
@Path("{id}")
@Produces(MediaType.APPLICATION_JSON)
public Response getProducts(@PathParam("id") int id) {
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
```

javado.lec06.Main を実行しなおす。

Rest Client で、http://localhost:8080/myapp/product/2 を開くと下のようなJSONGが、

```json
{"id":2,"name":"孤独のグルメ2","price":994}
```

http://localhost:8080/myapp/product/3 を開くと、 `400: Bad Request` が表示されればOK。


## 商品を登録




# 参考
- [JAX-RSを始める #javaee 裏紙](http://backpaper0.github.io/2014/12/01/javaee_advent_calendar_2014.html)
- [JAX-RS入門および実践](http://backpaper0.github.io/ghosts/jaxrs-getting-started-and-practice.html#1)
- [JavaEEでもできる！JAX-RSでお手軽REST開発](https://cloudear.jp/blog/?p=2005)
