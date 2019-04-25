package bdbk.seckill.service;

import bdbk.seckill.domain.OrderInfo;
import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.domain.UserOrder;
import bdbk.seckill.util.MD5Util;
import bdbk.seckill.util.RedisUtil;
import bdbk.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillService {
	
	@Autowired
    private GoodsService goodsService;
	
	@Autowired
    private OrderService orderService;

	@Autowired
	private RedisUtil redisUtil;

	private static char[] ops = new char[] {'+', '-', '*'};


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

	public OrderInfo getOrderInfoById(String OrderId) {
		return orderService.getOrderInfoById(OrderId);
	}
	/**
	 * 获取秒杀结果
	 */
	public String getSeckillResult(Long userId, long goodsId) {
		UserOrder order = orderService.getUserOrderByUserIdGoodsId(userId, goodsId);
		//秒杀成功
		if(order != null) {
			return order.getOrderId();
		}else {
			boolean isOver = redisUtil.hasKey("gsr_gid:"+goodsId);
			if(isOver) {
				return "error";
			}else {
				return "doing";
			}
		}
	}

	/**
	 *  校验路径
	 */
	public boolean checkPath(SeckillUser user, long goodsId, String path) {
		return !(user == null || path == null) && redisUtil.get("path_ugid:"+user.getId() + "_"+ goodsId).equals(path);
	}

	/**
	 *  校验验证码
	 */
	public boolean checkVerifyCode(SeckillUser user, long goodsId, int verifyCode) {
		if(user == null || goodsId <=0) {
			return false;
		}
		Integer codeOld = (Integer) redisUtil.get("vc_ugid:"+user.getId()+"_"+goodsId);
		if(codeOld == null || codeOld - verifyCode != 0 ) {
			return false;
		}
		// 用完删除
		redisUtil.del("vc_ugid:"+user.getId()+"_"+goodsId);
		return true;
	}

	public String addPath(SeckillUser user, long goodsId) {
		if(user == null || goodsId <=0) {
			return null;
		}
		String str = MD5Util.getMD5(UUID.randomUUID().toString());
		redisUtil.set("path_ugid:"+user.getId() + "_"+ goodsId, str);
		return str;
	}

	/**
	 * 生成验证码
	 */
	public BufferedImage getVerifyCode(SeckillUser user, long goodsId) {
		if(user == null || goodsId <=0) {
			return null;
		}
		int width = 80;
		int height = 32;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		Random rdm = new Random();
		for (int i = 0; i < 50; i++) {
			int x = rdm.nextInt(width);
			int y = rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		String verifyCode = generateVerifyCode(rdm);
		g.setColor(new Color(0, 100, 0));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();
		//把验证码存到redis中
		int rnd = calc(verifyCode);
		redisUtil.set("vc_ugid:"+user.getId()+"_"+goodsId, rnd,5, TimeUnit.MINUTES);
		//输出图片
		return image;
	}

	private static int calc(String exp) {
		try {
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("JavaScript");
			return (Integer)engine.eval(exp);
		}catch(Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * + - *
	 * */
	private String generateVerifyCode(Random rdm) {
		int num1 = rdm.nextInt(10);
		int num2 = rdm.nextInt(10);
		int num3 = rdm.nextInt(10);
		char op1 = ops[rdm.nextInt(3)];
		char op2 = ops[rdm.nextInt(3)];
		return ""+ num1 + op1 + num2 + op2 + num3;
	}
	
}
