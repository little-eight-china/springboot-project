package bdbk.seckill.service;

import bdbk.seckill.dao.OrderDao;
import bdbk.seckill.domain.Order;
import bdbk.seckill.domain.OrderInfo;
import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
	
	@Autowired
	OrderDao orderDao;
	
	public Order getOrderByUserIdGoodsId(long userId, long goodsId) {
		return orderDao.getOrderByUserIdGoodsId(userId, goodsId);
	}

	@Transactional
	public OrderInfo createOrder(SeckillUser user, GoodsVo goods) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goods.getId());
		orderInfo.setName(goods.getName());
		orderInfo.setPrice(goods.getSeckillPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		long orderId = orderDao.insert(orderInfo);
		Order order = new Order();
		order.setGoodsId(goods.getId());
		order.setOrderId(orderId);
		order.setUserId(user.getId());
		orderDao.insertOrder(order);
		return orderInfo;
	}
	
}
