package bdbk.seckill.controller;

import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.service.SeckillUserService;
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

	/**
	 * 开始秒杀
	 */
    @RequestMapping("/start")
    public String start(Model model, SeckillUser user,
                       @RequestParam("goodsId")long goodsId) {
    	model.addAttribute("user", user);
    	if(user == null) {
    		return "login";
    	}
        return "orderDetail";
    }
}
