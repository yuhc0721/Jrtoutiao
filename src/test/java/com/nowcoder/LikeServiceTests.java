package com.nowcoder;

import com.nowcoder.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JrtoutiaoApplication.class)
public class LikeServiceTests {
    @Autowired
    LikeService likeService;

    //执行要测试的业务
    @Test
    public void testLike(){
        likeService.like(123,1,1);
        //验证要测试数据
        Assert.assertEquals(1,likeService.getLikeStatus(123,1,1));
    }

    @Test
    public void testDisLike(){
        likeService.disLike(123,1,1);
        Assert.assertEquals(-1,likeService.getLikeStatus(123,1,1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException(){
        throw new IllegalArgumentException("xxxx");
    }
    //初始化数据
    @Before
    public void  setUp(){
        System.out.println("setUp");
    }
    //清理数据
    @After
    public void tearDown(){
        System.out.println("tearDown");
    }

    @BeforeClass
    public static void beforeClass(){
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("afterClass");
    }
}
