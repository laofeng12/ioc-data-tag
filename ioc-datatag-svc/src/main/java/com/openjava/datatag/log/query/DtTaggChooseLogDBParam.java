package com.openjava.datatag.log.query;

import java.util.Date;

import lombok.Data;
import org.ljdp.core.db.RoDBQueryParam;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询对象
 * @author lch
 *
 */
@Data
public class DtTaggChooseLogDBParam extends RoDBQueryParam {
	private Long eq_id;//日志编号 --主键查询
	
	private Long eq_copiedTagg;//被拷贝的标签组编号 = ?
	private Long eq_copyTagg;//拷贝的标签组编号 = ?
	private Long eq_chooseUser;//选用者 = ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date le_chooseTime;//选用时间 <= ?
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date ge_chooseTime;//选用时间 >= ?

	private String keyword;//搜索关键词
	private String sql_key;//

}