package bdbk.seckill.dao;

import bdbk.seckill.domain.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SeckillUserDao {
	
	@Select("select * from seckill_user where mobile = #{mobile}")
    SeckillUser getByMobile(@Param("mobile") String mobile);
}
