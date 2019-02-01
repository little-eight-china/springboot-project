package babk.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {


    @RequestMapping("/")
    public String list() {
        return "goodsList";
    }
    
}
