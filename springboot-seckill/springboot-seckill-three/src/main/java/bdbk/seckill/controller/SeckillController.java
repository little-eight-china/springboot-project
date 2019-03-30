package bdbk.seckill.controller;

import bdbk.seckill.constant.CodeMsg;
import bdbk.seckill.domain.OrderInfo;
import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.domain.UserOrder;
import bdbk.seckill.service.GoodsService;
import bdbk.seckill.service.OrderService;
import bdbk.seckill.service.SeckillService;
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

	@Autowired
	SeckillService seckillService;
	/**
	 * 开始秒杀
	 */
    @RequestMapping("/start")
    public String start(Model model, SeckillUser user,
                       @RequestParam("goodsId")long goodsId) {
    	model.addAttribute("user", user);

    	// 如果为空跳登录
    	if(user == null || user.getId() == null) {
    		return "login";
    	}

		// 判断库存
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goods.getStock();
		if(stock <= 0) {
			model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
			return "seckillFail";
		}

		// 判断是否已经秒杀到了,直接跳转订单详情
		// TODO 要修改先跳转到失败页面才行，不能直接跳订单详情
		UserOrder userOrder = orderService.getUserOrderByUserIdGoodsId(user.getId(), goodsId);
		if(userOrder != null) {
			OrderInfo orderInfo = seckillService.getOrderInfoByUserIdGoodsId(userOrder.getUserId(), userOrder.getGoodsId());
			model.addAttribute("orderInfo", orderInfo);
			model.addAttribute("goods", goods);
			return "orderDetail";
		}

		// 减库存 下订单 写入秒杀订单
		OrderInfo orderInfo = seckillService.seckill(user, goods);
		model.addAttribute("orderInfo", orderInfo);
		model.addAttribute("goods", goods);
        return "orderDetail";
    }
}
