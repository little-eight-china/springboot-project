package bdbk.seckill.service;

import bdbk.seckill.dao.GoodsDao;
import bdbk.seckill.domain.SeckillGoods;
import bdbk.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
	
	@Autowired
	private GoodsDao goodsDao;
	
	public List<GoodsVo> listGoodsVo(){
		return goodsDao.listGoodsVo();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId) {
		return goodsDao.getGoodsVoByGoodsId(goodsId);
	}

	void reduceStock(GoodsVo goods) {
		SeckillGoods seckillGoods = new SeckillGoods();
		seckillGoods.setGoodsId(goods.getId());
		goodsDao.reduceStock(seckillGoods);
	}
	
	
	
}
