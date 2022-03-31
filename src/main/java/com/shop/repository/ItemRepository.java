package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    /**
     * itemNm(상품명)으로 데이터를 조회하기 위해서 By 뒤에 필드명인 ItemNm을 메소드의 이름에 붙여줍니다.
     * 엔티티명은 생략이 가능하므로 findItemByItemNm 대신에 findByItemNm으로 메소드명을 만들어줍니다.
     * 매개변수로는 검색할 때 사용할 상품명 변수를 넘겨줍니다.
     * @param itemNm
     * @return
    **/
    List<Item> findByItemNm(String itemNm);

    /**
     * 상품을 상품명과 상품상세설명을 OR조건을 이용하여 조회하는 쿼리 메소드입니다.
     * @param itemNm
     * @param itemDetail
     * @return
     */
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    /**
     * 파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회하는 쿼리 메소드입니다.
     * @param price
     * @return
     */
    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    /**
     * SQL의 경우 데이터베이스의 테이블을 대상으로 쿼리를 수행하고
     * JPQL은 엔티티 객체를 대상으로 쿼리를 수행한다. 테이블이 아닌 객체를 대상으로 검색하는 객체지향 쿼리이다.
     * JPQL은 SQL을 추상화해서 사용하기 때문에 특정 데이터베이스SQL에 의존하지 않는다.
     * >> JPQL로 작성했다면 데이터베이스가 변경되어도 애플리케이션이 영향을 받지 않는다.
     * @param itemDetail
     * @return
     */

    /**
     * @Query 어노테이션 안에 JPQL로 작성한 쿼리문을 넣어줍니다.
     * from 뒤에는 엔티티 클래스로 작성한 Item을 지정해주었고, Item으로부터 데이터를 select 하겠다는 것을 의미한다.
     */
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    /**
     * 파라미터에 @Param 어노테이션을 이용하여 파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정해줄 수 있습니다.
     * 현재는 itemDetail 변수를 "like % %" 사이에 ":itemDetail"로 값이 들어가도록 작성했습니다.
     */
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    /**
     * value 안에 네이티브 쿼리문을 작성하고 "nativeQuery=true" 지정합니다.
     */
    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailNative(@Param("itemDetail") String itemDetail);
}
