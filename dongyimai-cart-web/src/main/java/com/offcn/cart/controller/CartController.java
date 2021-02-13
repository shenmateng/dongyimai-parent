package com.offcn.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.offcn.cart.service.CartService;
import com.offcn.entity.Cart;
import com.offcn.entity.Result;
import com.offcn.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/cart")
@SuppressWarnings("all")
public class CartController {

    @Reference(timeout = 6000)
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    //购物车列表
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        //工具类获取Cookie
        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if (cartListString == null || cartListString.equals("")) {
            cartListString = "[]";
        }
        //转换成集合
        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);
        if (username.equals("anonymousUser")) { //如果未登录

            return cartList_cookie;

        } else {   //如果已登录

            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);//从redis中提取
            if (cartList_cookie.size() > 0) {//如果本地存在购物车
                //合并购物车
                cartList_redis = cartService.mergeCartList(cartList_redis, cartList_cookie);
                //清除本地cookie的数据
                CookieUtil.deleteCookie(request, response, "cartList");
                //将合并后的数据存入redis
                cartService.saveCartListToRedis(username, cartList_redis);
            }


            return cartList_redis;

        }

    }


    //添加商品到购物车
    //@CrossOrigin(origins="http://localhost:9105",allowCredentials="true")   这个注解相当于下面两个开启资源和资源共享
    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId, Integer num) {
        //开启资源共享，需求9105访问
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9105");
        //允许携带参数
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录用户：" + username);
        try {
            //获取购物车列表
            List<Cart> cartList = findCartList();
            //调用服务添加到购物车列表
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);

            if (username.equals("anonymousUser")) { //如果是未登录，保存到cookie
                //存到cookie中
                CookieUtil.setCookie(request, response, "cartList", JSON.toJSONString(cartList), 3600 * 24, "UTF-8");
                System.out.println("向cookie存入数据");
            } else { //如果是已登录，保存到redis
                cartService.saveCartListToRedis(username, cartList);
                System.out.println("向redis存入数据");
            }
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }
}
