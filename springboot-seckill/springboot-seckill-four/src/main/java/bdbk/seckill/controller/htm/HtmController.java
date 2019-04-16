package bdbk.seckill.controller.htm;

import bdbk.seckill.constant.CodeMsg;
import bdbk.seckill.domain.OrderInfo;
import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.domain.UserOrder;
import bdbk.seckill.rabbitmq.MQSender;
import bdbk.seckill.rabbitmq.SeckillMessage;
import bdbk.seckill.service.GoodsService;
import bdbk.seckill.service.OrderService;
import bdbk.seckill.service.SeckillService;
import bdbk.seckill.util.RedisUtil;
import bdbk.seckill.vo.GoodsDetailVo;
import bdbk.seckill.vo.GoodsVo;
import bdbk.seckill.vo.ResponseDataVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 *  演示页面静态化
 * @author little_eight
 * @since 2019/4/2
 */
@Controller
@RequestMapping("/htm")
public class HtmController  implements InitializingBean {

    @Autowired
    private GoodsService goodsService;


    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MQSender sender;
    /**
     * 存是否已经无库存
     */
    private HashMap<Long, Boolean> localOverMap =  new HashMap<Long, Boolean>();
    /**
     * 系统初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if(goodsList == null) {
            return;
        }
        for(GoodsVo goods : goodsList) {
            redisUtil.set("goodsStock_gid:"+goods.getId(), goods.getSeckillStock());
            localOverMap.put(goods.getId(), false);
        }
    }


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
    public ResponseDataVo<Integer> miaosha(Model model, SeckillUser user,
                                             @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return ResponseDataVo.error(CodeMsg.SESSION_ERROR.getMsg());
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if(over) {
            return ResponseDataVo.error(CodeMsg.SECKILL_OVER.getMsg());
        }

        //预减库存
        long stock = redisUtil.decr("goodsStock_gid:"+goodsId);
        if(stock < 0) {
            localOverMap.put(goodsId, true);
            return ResponseDataVo.error(CodeMsg.SECKILL_OVER.getMsg());
        }

        // 判断是否已经秒杀到了
        UserOrder order = orderService.getUserOrderByUserIdGoodsId(user.getId(), goodsId);
        if(order != null) {
            return ResponseDataVo.error(CodeMsg.REPEATE_SECKILL.getMsg());
        }
        // 入队
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setUser(user);
        seckillMessage.setGoodsId(goodsId);
        sender.sendSeckillMessage(seckillMessage);
        return ResponseDataVo.success(0);
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


    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/seckill/result", method=RequestMethod.GET)
    @ResponseBody
    public ResponseDataVo<Long> miaoshaResult(Model model,SeckillUser user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return ResponseDataVo.error(CodeMsg.SESSION_ERROR.getMsg());
        }
        long result = seckillService.getSeckillResult(user.getId(), goodsId);
        return ResponseDataVo.success(result);
    }
}
