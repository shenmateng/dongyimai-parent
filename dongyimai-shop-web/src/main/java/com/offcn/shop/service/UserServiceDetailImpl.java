package com.offcn.shop.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.offcn.pojo.TbSeller;
import com.offcn.sellergoods.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

//这个实现类的对象在安全配置文件中生成的
public class UserServiceDetailImpl implements UserDetailsService {
    @Reference
    private SellerService sellerService;

    public UserServiceDetailImpl(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    public UserServiceDetailImpl() {
    }

    //这里的username就是从页面拿过来的
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
        grantedAuthority.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        TbSeller tbSeller = this.sellerService.findOne(username);
        User user = null;
        if (tbSeller != null) {
            if (tbSeller.getStatus().equals("1")) {
                //根据商家ID查询商家信息
                //这里的密码必须是要加密过的，不然等不上去
                user = new User(username, tbSeller.getPassword(), grantedAuthority); //三个参数，名字，密码，权限
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }
}
