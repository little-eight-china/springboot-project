package bdbk.seckill.rabbitmq;

import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.domain.UserOrder;
import bdbk.seckill.service.GoodsService;
import bdbk.seckill.service.OrderService;
import bdbk.seckill.service.SeckillService;
import bdbk.seckill.vo.GoodsVo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息消费类
 */
@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);


		@Autowired
		private GoodsService goodsService;
		
		@Autowired
		private OrderService orderService;
		
		@Autowired
		private SeckillService seckillService;
		
		@RabbitListener(queues=MQConfig.SECKILL_QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
			SeckillMessage seckillMessage  = JSON.parseObject(message, SeckillMessage.class);
			SeckillUser user = seckillMessage.getUser();
			long goodsId = seckillMessage.getGoodsId();
			
			GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
	    	int stock = goods.getStock();
	    	if(stock <= 0) {
	    		return;
	    	}
	    	//判断是否已经秒杀到了
	    	UserOrder order = orderService.getUserOrderByUserIdGoodsId(user.getId(), goodsId);
	    	if(order != null) {
	    		return;
	    	}
	    	//减库存 下订单 写入秒杀订单
	    	seckillService.seckill(user, goods);
		}
}
