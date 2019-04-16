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
import org.springframework.amqp.rabbit.annotation.Queue;
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

	/**
	 * 三种模式,由于版本问题，这里只能用第一种，所以还是自己去config里新建一个bean
	 * queues=queueName要手动创建队列名（我这里配置）
	 * queuesToDeclare = @Queue("queueNmae")，自动创建
	 * 还有一种是bindings,自动  创建queue和自动绑定Exchanges(交换机)上,bindings=@QueueBinding(value=@Queue("queueNmae"),exchange=@Exchange("exchangeName")))
	 */
	@RabbitListener(queues = MQConfig.SECKILL_QUEUE)
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
