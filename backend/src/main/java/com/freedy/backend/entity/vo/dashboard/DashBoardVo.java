package com.freedy.backend.entity.vo.dashboard;

import io.swagger.models.Operation;
import lombok.Data;

import javax.servlet.ServletRegistration.Dynamic;
import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/18 22:55
 */
@Data
public class DashBoardVo {
    //文章数量
    private Integer articleNum;
    //评论数量
    private String commentNum;
    //访问数量
    private String visitNum;
    //建立天数
    private String establishmentTime;
    //文章分布
    /*
     [
		{
			name: 'java',
			children: [
				{
					name: 'Uncle Leo',
					value: 1,
				}
			]
		},
		{
			name: 'Father',
			value: 1,
		}
	]
    */
    private List<ArticleDistribution> articleDistribution;
    @Data
    public static class ArticleDistribution{
        //分类名称
        private String name;
        //分类下的子文章
        private List<Children> children;
        @Data
        public static class Children{
            //文章名称
            private String name;
            //文章权重
            private Integer value;
        }
    }
    /*
     [
				['Match Latte', 43.3, 85.8, 93.7],
				['Milk Tea', 83.1, 73.4, 55.1],
				['Cheese Cocoa', 86.4, 65.2, 82.5],
				['Walnut Brownie', 72.4, 53.9, 39.1]
	  ]
     */
    private Object[][] articlePopular;
    /* [820, 932, 901, 934, 1290, 1330, 1320] */
    private List<Integer> visitorNum;
}
