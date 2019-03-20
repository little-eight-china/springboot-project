package bdbk.seckill.service;

import bdbk.seckill.domain.OrderInfo;
import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;

	@Transactional
	public OrderInfo seckill(SeckillUser user, GoodsVo goods) {
		//减库存 下订单 写入秒杀订单
		goodsService.reduceStock(goods);
		//order_info user_order
		return orderService.createOrder(user, goods);
	}

	public OrderInfo getOrderInfoByUserIdGoodsId(long userId, long goodsId) {
		return orderService.getOrderInfoByUserIdGoodsId(userId, goodsId);
	}
	
}
