package bdbk.seckill.dao;

import bdbk.seckill.domain.SeckillGoods;
import bdbk.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {
	
	@Select("select b.*,a.stock, a.start_date, a.end_date,a.seckill_price from seckill_goods a left join goods b on a.goods_id = b.id")
	List<GoodsVo> listGoodsVo();

	@Select("select b.*,a.stock, a.start_date, a.end_date,a.seckill_price from seckill_goods a left join goods b on a.goods_id = b.id where b.id = #{goodsId}")
	GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

	// 加一个 and stock > 0 防止超卖
	@Update("update seckill_goods set stock = stock - 1 where goods_id = #{goodsId} and stock > 0")
	void reduceStock(SeckillGoods g);
	
}
