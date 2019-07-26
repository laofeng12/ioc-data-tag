package com.openjava.datatag.tagcol.query;

import org.ljdp.core.db.RoDBQueryParam;

/**
 * 查询对象
 * @author hyq
 *
 */
public class DtCooperationSetColParam  {
	private Long userId;//协作用户Id
	
	private Long modelId;//协作模型Id

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}