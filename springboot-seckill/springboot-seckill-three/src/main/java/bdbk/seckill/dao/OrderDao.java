package bdbk.seckill.dao;

import bdbk.seckill.domain.OrderInfo;
import bdbk.seckill.domain.UserOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {
	
	@Select("select * from user_order where user_id=#{userId} and goods_id=#{goodsId}")
	UserOrder getUserOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

	@Select("select * from order_info where user_id=#{userId} and goods_id=#{goodsId}")
	OrderInfo getOrderInfoByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

	@Insert("insert into order_info(user_id, goods_id, name, goods_count, price, order_channel, status, create_date)values("
			+ "#{userId}, #{goodsId}, #{name}, #{goodsCount}, #{price}, #{orderChannel},#{status},#{createDate} )")
	@SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
	long insertOrderInfo(OrderInfo orderInfo);

	@Insert("insert into user_order (user_id, goods_id, order_id)values(#{userId}, #{goodsId}, #{orderId})")
	void insertUserOrder(UserOrder userOrder);


}
