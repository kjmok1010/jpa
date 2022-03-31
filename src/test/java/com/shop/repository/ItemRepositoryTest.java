package com.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    /**
     * 영속성 컨텍스트를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈을 주입한다.
     */
    @PersistenceContext
    EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    /**
     * ItemRepository 인터페이스에 작성했던 findByItemNm 메소드를 호출합니다.
     * 파라미터로는 '테스트 상품1' 이라는 상품명을 전달
     */
    public void createItemList(){
        for(int i=1;i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : itemList ){
            /**
             * 조회 결과 얻은 item 객체들을 출력합니다.
             */
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 OR 테스트")
    public void findByItemNmOrItemDetailTest(){
        /**
         * 기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여 조회할 대상을 만들어 주겠습니다.
         */
        this.createItemList();
        /**
         * 상품명이 "테스트 상품1" 또는 상품상세설명이 "테스트 상품 상세 설명5"이면 해당 상품을 itemList에 할당합니다.
         * 테스트 코드를 실행하면 조건대로 2개의 상품이 출력되는 것을 볼수 있습니다.
         */
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        for(Item item : itemList){

            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest(){
        this.createItemList();
        /**
         * 현재 데이터베이스에 저장된 가격은 10001~10010입니다.
         * 테스트 코드 실행 시 10개의 상품을 저장하는 로그가 콘솔에 나타나고 맨 마지막에 가격이 10005보다 작은 4개의 상품을 출력해주는 것을 확인할 수 있습니다.
         */
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        System.out.println("=====================================================");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
        System.out.println("=====================================================");
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품 조회 테스트")
    public void findByItemDetailNative(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetailNative("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회테스트1")
    public void queryDslTest(){
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

}