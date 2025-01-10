package feignclient;

import com.zx.domain.entity.base.Region;
import com.zx.domain.entity.user.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "user-service")
public interface UserFeignClient {

    @GetMapping(  "user/userAddress/auth/getUserAddressById/{id}")
    public UserAddress getUserAddressById(@RequestHeader("userInfo") String userInfo,
                                          @PathVariable("id") Long id);


    @GetMapping("user/region/getRegionByCode/{code}")
    public Region getRegionById(@PathVariable("code") String code);
}
