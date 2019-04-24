package bdbk.seckill.service;

import bdbk.seckill.dao.OrderDao;
import bdbk.seckill.domain.UserOrder;
import bdbk.seckill.domain.OrderInfo;
import bdbk.seckill.domain.SeckillUser;
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

	public UserOrder getUserOrderByUserIdGoodsId(long userId, long goodsId) {
		return orderDao.getUserOrderByUserIdGoodsId(userId, goodsId);
	}

	OrderInfo getOrderInfoByUserIdGoodsId(long userId, long goodsId) {
		return orderDao.getOrderInfoByUserIdGoodsId(userId, goodsId);
	}

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
		return orderInfo;
	}
	
}
