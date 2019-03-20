package bdbk.seckill.service;

import bdbk.seckill.constant.CodeMsg;
import bdbk.seckill.dao.SeckillUserDao;
import bdbk.seckill.domain.SeckillUser;
import bdbk.seckill.util.MD5Util;
import bdbk.seckill.util.RedisUtil;
import bdbk.seckill.vo.LoginVo;
import bdbk.seckill.vo.ReturnDataVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class SeckillUserService {

	@Autowired
	private SeckillUserDao seckillUserDao;

	@Autowired
	private RedisUtil redisUtil;

	public ReturnDataVo login(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			return ReturnDataVo.error(CodeMsg.SERVER_ERROR.getMsg());
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		SeckillUser user = seckillUserDao.getByMobile(mobile);
		if(user == null ) {
			return ReturnDataVo.error(CodeMsg.MOBILE_NOT_EXIST.getMsg());
		}
		//验证密码
		String dbPass = user.getPassword();
		String calcPass = MD5Util.getMD5(formPass+user.getSalt());
		if(!calcPass.equals(dbPass)) {
			return ReturnDataVo.error(CodeMsg.PASSWORD_ERROR.getMsg());
		}
		//生成cookie
		String token = UUID.randomUUID().toString().replace("-", "");
		addCookie(response, token, user);
		return ReturnDataVo.success();
	}

	public SeckillUser getByToken(HttpServletResponse response, String token) {
		if(StringUtils.isEmpty(token)) {
			return null;
		}
		SeckillUser user = (SeckillUser)redisUtil.get(token);
		//延长有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}

	private void addCookie(HttpServletResponse response, String token, SeckillUser user) {
		redisUtil.set(token, user);
		Cookie cookie = new Cookie("token", token);
		cookie.setMaxAge(3600*24 * 2);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
