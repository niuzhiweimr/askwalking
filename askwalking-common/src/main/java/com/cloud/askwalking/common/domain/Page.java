package com.cloud.askwalking.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;

public class Page implements Serializable {

	private static final long serialVersionUID = -5211416548628315861L;

	/**
	 * 页码，默认是第一页
	 */
	private int pageNo = 1;

	/**
	 * 每页显示的记录数，默认是15
	 */
	private int pageSize = 15;

	/**
	 * 总记录数
	 */
	private int totalRecord;

	/**
	 * 总可选择记录数
	 */
	private int totalCanSelectRecord;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 是否返回总金额(true返回,否则不返回)
	 */
	private boolean hasTotalAmount;

	/**
	 * 总金额(根据每个接口业务定义)
	 */
	private BigDecimal totalAmount;

	/**
	 * 总可选金额
	 */
	private BigDecimal totalCanSelectAmount;

	public Page() {
		super();
	}

	public Page(Integer pageNo, Integer pageSize) {
		super();
		if (pageNo != null) {
			this.pageNo = pageNo;
		}
		if (pageSize != null) {
			this.pageSize = pageSize;
		}
	}

	public Page(int pageNo, int pageSize, boolean hasTotalAmount) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.hasTotalAmount = hasTotalAmount;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		// 在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。
		this.totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public boolean isHasTotalAmount() {
		return hasTotalAmount;
	}

	public void setHasTotalAmount(boolean hasTotalAmount) {
		this.hasTotalAmount = hasTotalAmount;
	}

	public BigDecimal getTotalCanSelectAmount() {
		return totalCanSelectAmount;
	}

	public void setTotalCanSelectAmount(BigDecimal totalCanSelectAmount) {
		this.totalCanSelectAmount = totalCanSelectAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * 从第几条开始
	 * 
	 * @return start
	 */
	@JsonIgnore
	public int getLimitStart() {
		return (this.pageNo - 1) * this.pageSize;
	}

	/**
	 * 查询多少条
	 * 
	 * @return 记录数
	 */
	@JsonIgnore
	public int getLimitEnd() {
		return this.pageSize;
	}

	public int getTotalCanSelectRecord() {
		return totalCanSelectRecord;
	}

	public void setTotalCanSelectRecord(int totalCanSelectRecord) {
		this.totalCanSelectRecord = totalCanSelectRecord;
	}

}
