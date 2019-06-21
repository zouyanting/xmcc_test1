package com.xmcc.repository;

import com.google.common.collect.Lists;
import com.xmcc.entity.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test //查询单个，与修改测试
    public void findOne() {
        Optional<ProductCategory> byId = productCategoryRepository.findById(1);
        ProductCategory productCategory = null;
        if (byId.isPresent()) {//判断是否有  我们这里直接用有的测试了
            productCategory = byId.get();
            System.out.println(productCategory);
        }
        productCategory.setCategoryName("家电");
        //修改测试 也是save方法  如果实体类一点没变 不会去修改的
        productCategoryRepository.save(productCategory);
    }

    @Test
    public void findByTypeIn(){
        List<ProductCategory> byCategoryTypeIn=productCategoryRepository.findByCategoryTypeIn(Lists.newArrayList(1,2,3));
        byCategoryTypeIn.stream().forEach(System.out::println);
    }

    @Test//自定义sql查询
    public void findBySql(){
        List<String> strings = productCategoryRepository.queryNameByIdAndType(1, 1);
        strings.stream().forEach(System.out::println);
    }





}
