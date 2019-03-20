package bdbk.seckill.controller;

import bdbk.seckill.domain.SeckillUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {


    @RequestMapping("/list")
    public String list(Model model, SeckillUser user) {
        model.addAttribute("user", user);
        model.addAttribute("goodsList", null);
        return "goodsList";
    }
}
