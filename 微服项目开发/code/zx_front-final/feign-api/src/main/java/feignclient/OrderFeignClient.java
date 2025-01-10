package feignclient;

import com.zx.domain.entity.order.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("order-service")
public interface OrderFeignClient {
    @GetMapping("/api/order/orderInfo/auth/getOrderInfo/{orderNo}")
    public OrderInfo getOrderInfoByNo(@PathVariable String orderNo);
}
