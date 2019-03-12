package bdbk.seckill.controller;

import bdbk.seckill.constant.CodeMsg;
import bdbk.seckill.domain.Order;
import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.service.GoodsService;
import bdbk.seckill.service.OrderService;
import bdbk.seckill.service.SeckillUserService;
import bdbk.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

	@Autowired
	SeckillUserService userService;

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;
	/**
	 * 开始秒杀
	 */
    @RequestMapping("/start")
    public String start(Model model, SeckillUser user,
                       @RequestParam("goodsId")long goodsId) {
    	model.addAttribute("user", user);
    	// 如果为空跳登录
    	if(user == null) {
    		return "login";
    	}
		//判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getStock();
		if(stock <= 0) {
			model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
			return "seckillFail";
		}

		//判断是否已经秒杀到了
		Order order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			model.addAttribute("errmsg", CodeMsg.REPEATE_SECKILL);
			return "seckillFail";
		}

        return "orderDetail";
    }
}
