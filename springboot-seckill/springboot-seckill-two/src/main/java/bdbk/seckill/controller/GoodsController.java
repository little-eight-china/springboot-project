package bdbk.seckill.controller;

import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.service.GoodsService;
import bdbk.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/list")
    public String list(Model model, SeckillUser user) {
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goodsList";
    }

    @RequestMapping("/detail/{goodsId}")
    public String detail(Model model,SeckillUser user,
                         @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {
            // 秒杀还没开始，倒计时
            seckillStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){
            // 秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            // 秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("status", seckillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goodsDetail";
    }
    
}
