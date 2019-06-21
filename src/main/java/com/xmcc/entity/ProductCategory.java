package com.xmcc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity //表示该类为实体类并将实体类交给spring容器管理

//设置为true,表示update对象的时候,生成动态的update语句,如果这个字段的值是null就不会被加入到update语句中
@DynamicUpdate
@Data //相当于set、get、toString方法
@AllArgsConstructor
@NoArgsConstructor
//指定是那一张表（表名），如果表中包含下划线可以省略下划线，首字母大写，这样可以不定义Table
@Table(name="product_category")
public class ProductCategory implements Serializable {
    /**
     * 类目id
     * 指定主键
     */
    @Id//主键

//    生成策略一共包含两个（oracle和msql）
//    表示自增IDENTITY：mysql SEQUENCE:oracle
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    //类目的名字
    private String categoryName;
    // 类目编号.
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

}
