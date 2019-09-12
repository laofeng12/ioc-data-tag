package com.openjava.datatag.statistic.repository;

import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 标签模型数据库访问层
 * @author maliang
 *
 *
 */

public interface TagDashboardRepository extends DynamicJpaRepository<DtTaggingModel, Long> {

    /**
     * 查询上个月数据集增长百分比
     *
     */
    @Query(nativeQuery = true,value = "select concat(round((c1 - c2) / case when c2=0 then 1 else c2 end,2) * 100, '%') as growth_rate\n" +
            "  from (select count(1) as c1\n" +
            "          from (select TAGGING_TABLE_NAME\n" +
            "                  from DT_TAGGING_MODEL \n" +
            "                 WHERE is_deleted=0 and CREATE_TIME >= trunc(add_months(sysdate, -1), 'mm')\n" +
            "                   AND CREATE_TIME <=\n" +
            "                       trunc(last_day(add_months(sysdate, -1))) - 1 / 86400 +1\n" +
            "                 group by TAGGING_TABLE_NAME) t1) t2\n" +
            "  join (select count(1) as c2\n" +
            "          from (select TAGGING_TABLE_NAME\n" +
            "                  from DT_TAGGING_MODEL \n" +
            "                 WHERE is_deleted=0 and CREATE_TIME >= trunc(add_months(sysdate, -2), 'mm')\n" +
            "                   AND CREATE_TIME <=\n" +
            "                       trunc(last_day(add_months(sysdate, -2))) - 1 / 86400 +1\n" +
            "                 group by TAGGING_TABLE_NAME) t3) t4\n" +
            "    on 1 = 1")
    String getDataSetIncreasePercentage();

    /**
     * 查询上个月新增唯一标签数量
     *
     */
    @Query(nativeQuery = true,value = "select count(1) as tag_sum\n" +
            "  from (select tag_name\n" +
            "          from DT_TAG\n" +
            "         where is_deleted=0 and create_time >= trunc(add_months(sysdate, -1), 'mm')\n" +
            "           and create_time <=\n" +
            "               trunc(last_day(add_months(sysdate, -1))) - 1 / 86400 + 1\n" +
            "         group by tag_name) dt")
    public int getLastMonthTagSum();

    /*
     *
     * 查询上个月每个数据集对应标签的平均数
     * */
    @Query(nativeQuery = true,value = "select round(tag_sum / case when dataset_sum=0 then 1 else dataset_sum end, 2) as numbers\n" +
            "  from (\n" +
            "        \n" +
            "        select count(1) as tag_sum\n" +
            "          from (select tag_name\n" +
            "                   from DT_TAG\n" +
            "                  where is_deleted=0 and create_time >= trunc(add_months(sysdate, -1), 'mm')\n" +
            "                    and create_time <=\n" +
            "                        trunc(last_day(add_months(sysdate, -1))) - 1 / 86400 + 1\n" +
            "                  group by tag_name) dtag) dtag_1\n" +
            "  join\n" +
            " (select count(1) as dataset_sum\n" +
            "    from (select TAGGING_TABLE_NAME\n" +
            "            from DT_TAGGING_MODEL\n" +
            "           WHERE is_deleted=0 and CREATE_TIME >= trunc(add_months(sysdate, -1), 'mm')\n" +
            "             AND CREATE_TIME <=\n" +
            "                 trunc(last_day(add_months(sysdate, -1))) - 1 / 86400\n" +
            "           group by TAGGING_TABLE_NAME) tmodel) tmodel_1\n" +
            "    on 1 = 1\n")
    public double getTagThanDataSet();

    /*
     *
     * 查询去年每个月标签变化
     * */
    @Query(nativeQuery = true,value = "with \n" +
            "dt_month_temp as\n" +
            "(\n" +
            "select 1 as mt from dual union all\n" +
            "select 2 as mt from dual union all\n" +
            "select 3 as mt from dual union all\n" +
            "select 4 as mt from dual union all\n" +
            "select 5 as mt from dual union all\n" +
            "select 6 as mt from dual union all\n" +
            "select 7 as mt from dual union all\n" +
            "select 8 as mt from dual union all\n" +
            "select 9 as mt from dual union all\n" +
            "select 10 as mt from dual union all\n" +
            "select 11 as mt from dual union all\n" +
            "select 12 as mt from dual \n" +
            ")select NB from(\n" +
            "select nvl(t2.nb,0) as nb from dt_month_temp dmt\n" +
            "left join (select mt, count(1) nb\n" +
            "  from (select extract(month from create_time) as mt, tag_name\n" +
            "          from DT_TAG\n" +
            "         where connect_by_isleaf = 1\n" +
            "        CONNECT BY PRIOR ID = PREA_TAG_ID\n" +
            "               and is_deleted = 0\n" +
            "               and create_time >= trunc(add_months(sysdate, -12), 'year')\n" +
            "               and create_time <=\n" +
            "                   last_day(add_months(trunc(sysdate, 'YYYY'), -1)) -\n" +
            "                   1 / 86400 + 1\n" +
            "         group by extract(month from create_time), tag_name) t1\n" +
            " group by mt) t2 on dmt.mt=t2.mt order by dmt.mt ) t3")
     List<Object> getMonthlyLabelChanges();

    /*
     *
     *获取标签变化左边数据列表
     * */
    @Query(nativeQuery = true,value = "select * " +
            "from (select count(1) as ALL_TAG_NB" +
            "  from (SELECT DISTINCT ( TAG_NAME ) from DT_TAG" +
            "        where PREA_TAG_ID >= 0 and is_deleted = 0))t1"+
            " join (" +
            "select  count(1) as YEAR_TAG_NB" +
            "  from (SELECT DISTINCT ( TAG_NAME ) from DT_TAG" +
            "        where PREA_TAG_ID >= 0 and is_deleted = 0" +
            "              and create_time >= trunc(add_months(sysdate, -12), 'year')" +
            "              and create_time <= last_day(add_months(trunc(sysdate, 'YYYY'), -1)) -1 / 86400 + 1))t2" +
            " on 1=1" +
            " join (" +
            "select count(1) as MONTH_TAG_NB" +
            "  from (SELECT DISTINCT ( TAG_NAME ) from DT_TAG" +
            "        where PREA_TAG_ID >= 0 and is_deleted = 0" +
            "              and create_time >= trunc(add_months(sysdate, -1), 'mm')\n" +
            "                   and create_time <=trunc(last_day(add_months(sysdate, -1))) - 1 / 86400 + 1))t3" +
            " on 1=1")
    Map<String,String> getAllYearMonth();



    /*
     *
     * 获取今天top5 的热门标签
     * */
    @Query(nativeQuery = true,value = "select tag_name as 今日\n" +
            "  from (select *\n" +
            "          from (select tag_id, count(1) as numbers\n" +
            "                  from DT_TAG_CONDITION dtc\n" +
            "                 where dtc.is_deleted = 0\n" +
            "                   and dtc.create_time >=\n" +
            "                       to_date(to_char(sysdate, 'yyyy/mm/dd'), 'yyyy/mm/dd')\n" +
            "                 group by tag_id\n" +
            "                 order by 2 desc) dtc1\n" +
            "         where rownum <= 5) tb\n" +
            "  join DT_TAG dt\n" +
            "    on tb.tag_id = dt.id\n" +
            " order by numbers desc\n")
    public List<Object> getSamedayHotTags();

    /*
     *
     *获取昨天top5热门标签
     * */
    @Query(nativeQuery = true,value = "select tag_name as 昨日\n" +
            "  from (select *\n" +
            "          from (select tag_id, count(1) as numbers\n" +
            "                  from DT_TAG_CONDITION dtc\n" +
            "                 where is_deleted = 0\n" +
            "                   and create_time >=\n" +
            "                       to_date(to_char(sysdate - 1, 'yyyy/mm/dd'),\n" +
            "                               'yyyy/mm/dd')\n" +
            "                   and create_time <= TRUNC(SYSDATE - 1) + 1 - 1 / 86400\n" +
            "                 group by tag_id\n" +
            "                 order by 2 desc) dtc1\n" +
            "         where rownum <= 5) tb\n" +
            "  join DT_TAG dt\n" +
            "    on tb.tag_id = dt.id\n" +
            " order by numbers desc")
    public List<Object> getYesterdayHotTags();

    /*
     *
     *获取最近一周 top5热门标签
     * */
    @Query(nativeQuery = true,value = "select tag_name as 最近一周\n" +
            "  from (select *\n" +
            "          from (select tag_id, count(1) as numbers\n" +
            "                  from DT_TAG_CONDITION dtc\n" +
            "                 where is_deleted = 0\n" +
            "                   and create_time >= trunc(sysdate, 'iw') - 7\n" +
            "                   and create_time <= trunc(sysdate, 'iw') - 1 / 86400\n" +
            "                 group by tag_id\n" +
            "                 order by 2 desc) dtc1\n" +
            "         where rownum <= 5) tb\n" +
            "  join DT_TAG dt\n" +
            "    on tb.tag_id = dt.id\n" +
            " order by numbers desc")
    public List<Object> getLastweekHotTags();

    /*
     *
     *获取最近一个月 top5热门标签
     * */
    @Query(nativeQuery = true,value = "select tag_name as 最近一个月\n" +
            "  from (select *\n" +
            "          from (select tag_id, count(1) as numbers\n" +
            "                  from DT_TAG_CONDITION dtc\n" +
            "                 where is_deleted = 0\n" +
            "                   and create_time >= trunc(add_months(sysdate, -1), 'mm')\n" +
            "                   and create_time <=\n" +
            "                       trunc(last_day(add_months(sysdate, -1))) - 1 / 86400 + 1\n" +
            "                 group by tag_id\n" +
            "                 order by 2 desc) dtc1\n" +
            "         where rownum <= 5) tb\n" +
            "  join DT_TAG dt\n" +
            "    on tb.tag_id = dt.id\n" +
            " order by numbers desc\n")
    public List<Object> getLastMonthHotTags();

    /*
     *
     *获取最近一个年 top5热门标签
     * */
    @Query(nativeQuery = true,value = "select tag_name as 最近一年\n" +
            "  from (select *\n" +
            "          from (select tag_id, count(1) as numbers\n" +
            "                  from DT_TAG_CONDITION dtc\n" +
            "                 where is_deleted = 0\n" +
            "                   and create_time >= add_months(trunc(sysdate, 'yyyy'), -12)\n" +
            "                   and create_time <=\n" +
            "                       ADD_MONTHS(ADD_MONTHS(TRUNC(sysdate, 'YYYY'), 12) - 1 -\n" +
            "                                  1 / 86400 + 1,\n" +
            "                                  -12)\n" +
            "                 group by tag_id\n" +
            "                 order by 2 desc) dtc1\n" +
            "         where rownum <= 5) tb\n" +
            "  join DT_TAG dt\n" +
            "    on tb.tag_id = dt.id\n" +
            " order by numbers desc\n")
    public  List<Object> getLastYearHotTags();


    /**
     * 上个月标签使用个数
     * @author zmk
     */
    @Query(nativeQuery = true,value = "select count(1) from (select t1.TAG_NAME from DT_TAG_CONDITION t ,DT_TAG t1 " +
            "where t.TAG_ID=t1.ID and t.IS_DELETED=0 and t.CREATE_TIME>= trunc(add_months(sysdate, -1), 'mm') " +
            "and t.CREATE_TIME<= trunc(last_day(add_months(sysdate, -1))) - 1 / 86400 + 1" +
            "group by t1.TAG_NAME)tagNameTable ")
    Long lastMonthTagCount();
    /**
     * 上个月数据集使用个数
     */
    @Query(nativeQuery = true,value = "select count(1) from (select t.DATA_SET_ID,t.RESOURCE_TYPE from  DT_TAGGING_MODEL t  " +
            "where t.IS_DELETED=0 and t.CREATE_TIME>= trunc(add_months(sysdate, -1), 'mm') " +
            "and t.CREATE_TIME<= trunc(last_day(add_months(sysdate, -1))) - 1 / 86400 + 1" +
            "group by t.DATA_SET_ID,t.RESOURCE_TYPE)tagNameTable ")
    Long lastMonthDataSetCount();

    /**
     * 根据时间返回2、3级唯一标签数量
     */
    @Query(nativeQuery = true, value = "select count (1) from (select DISTINCT(TAG_NAME) from DT_TAG where IS_DELETED=0 AND PREA_TAG_ID >= 0 AND CREATE_TIME BETWEEN :beginTime and :endTime)")
    Long countByCreateTime(@Param("beginTime")Date beginTime,@Param("endTime")Date endTime);
}
