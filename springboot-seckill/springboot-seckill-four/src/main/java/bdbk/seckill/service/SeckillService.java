package bdbk.seckill.service;

import bdbk.seckill.domain.OrderInfo;
import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.domain.UserOrder;
import bdbk.seckill.util.RedisUtil;
import bdbk.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {
	
	@Autowired
    private GoodsService goodsService;
	
	@Autowired
    private OrderService orderService;

	@Autowired
	private RedisUtil redisUtil;
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

	/**
	 * 获取秒杀结果
	 */
	public long getSeckillResult(Long userId, long goodsId) {
		UserOrder order = orderService.getUserOrderByUserIdGoodsId(userId, goodsId);
		//秒杀成功
		if(order != null) {
			return order.getOrderId();
		}else {
			boolean isOver = redisUtil.hasKey("gsr_gid:"+goodsId);
			if(isOver) {
				return -1;
			}else {
				return 0;
			}
		}
	}
	
}
