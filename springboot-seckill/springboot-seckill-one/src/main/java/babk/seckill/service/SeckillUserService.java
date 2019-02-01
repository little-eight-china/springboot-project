package babk.seckill.service;

import babk.seckill.constant.CodeMsg;
import babk.seckill.dao.SeckillUserDao;
import babk.seckill.domain.SeckillUser;
import babk.seckill.util.MD5Util;
import babk.seckill.vo.LoginVo;
import babk.seckill.vo.ReturnDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class SeckillUserService {

	@Autowired
	private SeckillUserDao seckillUserDao;

	public ReturnDataVo login(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			return ReturnDataVo.error(CodeMsg.SERVER_ERROR.getMsg());
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		SeckillUser user = seckillUserDao.getByMobile(mobile);
		if(user == null) {
			return ReturnDataVo.error(CodeMsg.MOBILE_NOT_EXIST.getMsg());
		}
		//验证密码
		String dbPass = user.getPassword();
		String calcPass = MD5Util.getMD5(formPass+user.getSalt());
		if(!calcPass.equals(dbPass)) {
			return ReturnDataVo.error(CodeMsg.PASSWORD_ERROR.getMsg());
		}
		return ReturnDataVo.success();
	}

}
