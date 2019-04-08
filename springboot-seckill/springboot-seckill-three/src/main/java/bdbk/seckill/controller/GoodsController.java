package bdbk.seckill.controller;

import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.service.GoodsService;
import bdbk.seckill.util.RedisUtil;
import bdbk.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     *  跳转商品列表，页面取缓存
     */
    @RequestMapping(value = "/list", produces="text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, SeckillUser user) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return getHtmlCache(request,response,model,"gl","goodsList");
    }

    @RequestMapping(value = "/detail/{goodsId}", produces="text/html")
    @ResponseBody
    public String detail(HttpServletRequest request, HttpServletResponse response, Model model,SeckillUser user,
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
        return getHtmlCache(request,response,model,"gd","goodsDetail");
    }

    /**
     *  取页面缓存共同方法
     */
    private String getHtmlCache(HttpServletRequest request, HttpServletResponse response, Model model,
                                 String key, String htmlName){
        // 页面取缓存
        String html = (String) redisUtil.get("html:"+key);
        if(!StringUtils.isEmpty(html)) {
            return html;
        }
        SpringWebContext ctx = new SpringWebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap(), applicationContext);
        // 手动渲染
        html = thymeleafViewResolver.getTemplateEngine().process(htmlName, ctx);
        if(!StringUtils.isEmpty(html)) {
            redisUtil.set("html:"+key, html, 60, TimeUnit.SECONDS);
        }
        return html;
    }
}
