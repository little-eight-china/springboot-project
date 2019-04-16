package bdbk.seckill.vo;


import bdbk.seckill.domain.Goods;

import java.util.Date;

public class GoodsVo extends Goods {
	private Double seckillPrice;
	private Integer seckillStock;
	private Date startDate;
	private Date endDate;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getSeckillPrice() {
		return seckillPrice;
	}

	public void setSeckillPrice(Double seckillPrice) {
		this.seckillPrice = seckillPrice;
	}

	public Integer getSeckillStock() {
		return seckillStock;
	}


	public void setSeckillStock(Integer seckillStock) {
		this.seckillStock = seckillStock;
	}
}
