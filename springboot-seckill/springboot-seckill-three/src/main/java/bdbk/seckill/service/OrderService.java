package bdbk.seckill.service;

import bdbk.seckill.dao.OrderDao;
import bdbk.seckill.domain.OrderInfo;
import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.domain.UserOrder;
import bdbk.seckill.util.RedisUtil;
import bdbk.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class OrderService {
	
	@Autowired
    private OrderDao orderDao;

	@Autowired
	private RedisUtil redisUtil;

	public UserOrder getUserOrderByUserIdGoodsId(long userId, long goodsId) {
		// 先从缓存取，取不到再从数据库拿
		UserOrder order = (UserOrder) redisUtil.get("uo_ugid:" + userId + "_" + goodsId);
		if (order != null){
			return order;
		}
		order = orderDao.getUserOrderByUserIdGoodsId(userId, goodsId);
		// 存入缓存
		redisUtil.set("uo_ugid:" + userId + "_" + goodsId, order);
		return order;
	}

	OrderInfo getOrderInfoByUserIdGoodsId(long userId, long goodsId) {
		// 先从缓存取，取不到再从数据库拿
		OrderInfo order = (OrderInfo) redisUtil.get("oi_ugid:" + userId + "_" + goodsId);
		if (order != null){
			return order;
		}
		order = orderDao.getOrderInfoByUserIdGoodsId(userId, goodsId);
		// 存入缓存
		redisUtil.set("oi_ugid:" + userId + "_" + goodsId, order);
		return order;
	}

	/**
	 * 1、创建订单
 	 * 2、建立唯一索引防止超卖  ALTER TABLE user_order ADD UNIQUE u_uid_gid(user_id,goods_id)
	 */
	@Transactional
	OrderInfo createOrder(SeckillUser user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setName(goods.getName());
		orderInfo.setImg(goods.getImg());
		orderInfo.setTitle(goods.getTitle());
		orderInfo.setPrice(goods.getSeckillPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		String orderId = UUID.randomUUID().toString();
		orderInfo.setId(orderId);
		orderDao.insertOrderInfo(orderInfo);
		UserOrder userOrder = new UserOrder();
		userOrder.setGoodsId(goods.getId());
		userOrder.setOrderId(orderId);
		userOrder.setUserId(user.getId());
		orderDao.insertUserOrder(userOrder);
		// 存入缓存
		redisUtil.set("uo_ugid:" + user.getId() + "_" + goods.getId(), userOrder);
		redisUtil.set("oi_ugid:" + user.getId() + "_" + goods.getId(), userOrder);
		return orderInfo;
	}
	
}
