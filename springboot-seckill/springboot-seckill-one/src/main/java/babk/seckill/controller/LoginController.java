package babk.seckill.controller;

import babk.seckill.service.SeckillUserService;
import babk.seckill.vo.LoginVo;
import babk.seckill.vo.ReturnDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class LoginController {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private SeckillUserService userService;

    @RequestMapping("")
    public String toLogin() {
        return "login";
    }
    
    @RequestMapping("validator")
    @ResponseBody
    public ReturnDataVo validator(HttpServletResponse response, LoginVo loginVo) {
    	log.info(loginVo.toString());
    	return userService.login(response, loginVo);
    }
}
