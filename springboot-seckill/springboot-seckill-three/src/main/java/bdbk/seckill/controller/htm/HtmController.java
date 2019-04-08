package bdbk.seckill.controller.htm;

import bdbk.seckill.constant.CodeMsg;
import bdbk.seckill.domain.OrderInfo;
import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.domain.UserOrder;
import bdbk.seckill.service.GoodsService;
import bdbk.seckill.service.OrderService;
import bdbk.seckill.service.SeckillService;
import bdbk.seckill.vo.GoodsDetailVo;
import bdbk.seckill.vo.GoodsVo;
import bdbk.seckill.vo.ResponseDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  演示页面静态化
 * @author little_eight
 * @since 2019/4/2
 */
@Controller
@RequestMapping("/htm")
public class HtmController {

    @Autowired
    private GoodsService goodsService;


    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    /**
     *  商品详情静态化
     */
    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public ResponseDataVo<GoodsDetailVo> detail(SeckillUser user, @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        if (goods == null){
            return ResponseDataVo.error(CodeMsg.SERVER_ERROR.getMsg());
        }
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        // 秒杀还没开始，倒计时
        if(now < startAt ) {
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        // 秒杀已经结束
        }else  if(now > endAt){
            miaoshaStatus = 2;
            remainSeconds = -1;
        // 秒杀进行中
        }else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setSeckillStatus(miaoshaStatus);
        return ResponseDataVo.success(vo);
    }

    /**
     *  秒杀逻辑
     */
    @RequestMapping(value="/startSeckill", method= RequestMethod.POST)
    @ResponseBody
    public ResponseDataVo<OrderInfo> miaosha(Model model, SeckillUser user,
                                             @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return ResponseDataVo.error(CodeMsg.SESSION_ERROR.getMsg());
        }
        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStock();
        if(stock <= 0) {
            return ResponseDataVo.error(CodeMsg.SECKILL_OVER.getMsg());
        }
        // 判断是否已经秒杀到了
        UserOrder order = orderService.getUserOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return ResponseDataVo.success(seckillService.getOrderInfoByUserIdGoodsId(user.getId(), goodsId),
                    CodeMsg.REPEATE_SECKILL.getMsg());
        }
        // 减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        return ResponseDataVo.success(orderInfo);
    }

    /**
     *  订单详情静态化
     */
    @RequestMapping(value="/orderDetail/{userId}/{goodsId}")
    @ResponseBody
    public ResponseDataVo<OrderInfo> orderDetail(SeckillUser user, @PathVariable("userId")long userId, @PathVariable("goodsId")long goodsId) {
        OrderInfo orderInfo = seckillService.getOrderInfoByUserIdGoodsId(userId, goodsId);
        return ResponseDataVo.success(orderInfo);
    }
}
