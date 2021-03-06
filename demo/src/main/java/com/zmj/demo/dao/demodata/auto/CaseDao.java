package com.zmj.demo.dao.demodata.auto;


import com.zmj.demo.domain.auto.CaseChain;
import com.zmj.demo.domain.auto.CaseExcelChain;
import com.zmj.demo.domain.auto.CaseExecuteChain;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CaseDao {

    @Select({"<script>"+
            "SELECT count(*) FROM `demo`.`tb_case_manage`"+
            "WHERE 1=1 AND is_delete=0 "+
            "<if test=\"caseName!=null and caseName!=''\">"+
            "AND case_name = #{caseName}"+
            "</if>"+
            "<if test=\"interfaceManageID!=null and interfaceManageID!=''\">"+
            "AND interface_manage_id = #{interfaceManageID}"+
            "</if>"+
            "<if test=\"isSuccess!=null and isSuccess!=''\">"+
            "AND is_success = #{isSuccess}"+
            "</if>"+
            "</script>"})
    int acount(@Param("caseName") String caseName, @Param("interfaceManageID") Integer interfaceManageID,@Param("isSuccess") int isSuccess);

    @Select({"<script>"+
//            "select id,interface_manage_id interfaceManageID,case_name caseName,header_data headerData,param_data paramData,assert_type assertType,assert_data assertData,is_success isSuccess,result result,state,creator,create_date createDate,update_date updateDate from `demo`.`tb_case_manage`"+
            "select tc.id,interface_manage_id interfaceManageID,platform,project,module,name,case_name caseName,header_data headerData,param_data paramData,assert_type assertType,assert_data assertData,tc.is_success isSuccess,tc.result result,tc.state,tc.creator,tc.create_date createDate,tc.update_date updateDate"+
            " from `demo`.`tb_case_manage` tc inner join `demo`.`tb_interface_manage` ti on tc.interface_manage_id=ti.id inner join `demo`.`tb_platform_manage` tp on ti.platform_manage_id=tp.id  "+
            "WHERE 1=1"+
            "<if test=\"moduleId!=null and moduleId!=''\">"+
            "AND tp.id = #{moduleId}"+
            "</if>"+
            "<if test=\"caseName!=null and caseName!=''\">"+
            "AND case_name = #{caseName}"+
            "</if>"+
            "<if test=\"interfaceManageID!=null and interfaceManageID!=''\">"+
            "AND interface_manage_id = #{interfaceManageID}"+
            "</if>"+
            "<if test=\"isSuccess!=null and isSuccess!=''\">"+
            "AND tc.is_success = #{isSuccess}"+
            "</if>"+
            "AND tc.is_delete = 0 ORDER BY tc.update_date DESC LIMIT #{page},#{limit}"+
            "</script>"})
    List<CaseChain> list(@Param("moduleId") Integer moduleId,@Param("caseName") String caseName, @Param("interfaceManageID") Integer interfaceManageID,@Param("isSuccess") int isSuccess, @Param("page") int page, @Param("limit") int limit);

    @Select({"<script>"+
            "select tp.id platformId,ti.id interfaceId,ip,path,method,content_type contentType,tc.id,interface_manage_id interfaceManageID,platform,project,module,name,case_name caseName,header_data headerData,param_data paramData,assert_type assertType,assert_data assertData,tc.is_success isSuccess,tc.result result,tc.state,tc.creator,tc.create_date createDate,tc.update_date updateDate"+
            " from `demo`.`tb_case_manage` tc inner join `demo`.`tb_interface_manage` ti on tc.interface_manage_id=ti.id inner join `demo`.`tb_platform_manage` tp on ti.platform_manage_id=tp.id  "+
            "WHERE 1=1"+
            "<if test=\"id!=null and id!=''\">"+
            "AND tc.id = #{id}"+
            "</if>"+
            "</script>"})
    CaseChain info(@Param("id") Integer id);

    /**
     * ????????????id???????????????????????????ip????????????????????????????????????
     * @param caseList
     * @return
     */
    @Select({"<script>"+
            "select cm.id as id,pm.ip as ip,im.path as path,im.method as method,im.content_type as contentType,cm.header_data as headerData,cm.param_data as paramData,cm.assert_type as assertType,cm.assert_data as assertData " +
            "from `demo`.`tb_case_manage` as cm inner join `demo`.`tb_interface_manage` as im on cm.interface_manage_id= im.id inner join `demo`.`tb_platform_manage` as pm on im.platform_manage_id=pm.id "+
            "WHERE 1=1 AND cm.id in <foreach collection='caseList' item='item' open='(' separator=',' close=')'>#{item}</foreach> "+
            "AND cm.is_delete = 0 AND cm.is_success=0 ORDER BY cm.id asc LIMIT #{page},#{limit}"+
            "</script>"})
    List<CaseExecuteChain> caseAllInfoById(@Param("caseList") List<Integer> caseList, @Param("page") int page, @Param("limit") int limit);

    @Insert("INSERT INTO `demo`.`tb_case_manage` (`interface_manage_id`,`case_name`,`header_data`,`param_data`,`assert_type`,`assert_data`,`state`,`creator`,`create_date`,`update_date`) VALUES (#{interfaceManageID},#{caseName},#{headerData},#{paramData},#{assertType},#{assertData},#{state},#{creator},now(),now())")
    int add(@Param("interfaceManageID") Integer interfaceManageID, @Param("caseName") String caseName, @Param("headerData") String headerData, @Param("paramData") String paramData,@Param("assertType") String assertType,@Param("assertData") String assertData, @Param("state") String state, @Param("creator") String creator);

    /**
     * ?????????????????? ?????????tb_case_manage???
     * @param listCaseExcel
     */
    @Insert({"<script>" ,
            "INSERT INTO `demo`.`tb_case_manage` (`interface_manage_id`,`case_name`,`header_data`,`param_data`,`assert_type`,`assert_data`,`state`,`creator`,`create_date`,`update_date`) " + "values " +
            "<foreach collection='listCaseExcel' item='element' index='index' separator=','>" +
            "( " +
            "#{element.interfaceManageID,jdbcType=INTEGER},"+
            "#{element.caseName,jdbcType=VARCHAR},"+
            "#{element.headerData,jdbcType=VARCHAR},"+
            "#{element.paramData,jdbcType=VARCHAR},"+
            "#{element.assertType,jdbcType=VARCHAR},"+
            "#{element.assertData,jdbcType=VARCHAR},"+
            "#{element.state,jdbcType=VARCHAR},"+
            "#{element.creator,jdbcType=VARCHAR},"+
            "#{element.createDate,jdbcType=VARCHAR},"+
            "#{element.updateDate,jdbcType=VARCHAR}"+
            " )",
            "</foreach>",
            "</script>"})
    int addBatch(@Param("listCaseExcel") List<CaseExcelChain> listCaseExcel);

    @Delete("update `demo`.`tb_case_manage` set is_delete=1,update_date=now() where id = #{id} ")
    int delete(@Param("id") Integer id);

    @Update("update `demo`.`tb_case_manage` set interface_manage_id=#{interfaceManageID},case_name=#{caseName},header_data=#{headerData},param_data=#{paramData},assert_type=#{assertType},assert_data=#{assertData},state=#{state},creator=#{creator},update_date=now() where id = #{id}")
    int edit(@Param("id") Integer id,@Param("interfaceManageID") Integer interfaceManageID, @Param("caseName") String caseName, @Param("headerData") String headerData, @Param("paramData") String paramData,@Param("assertType") String assertType,@Param("assertData") String assertData, @Param("state") String state, @Param("creator") String creator);

    @Update("update `demo`.`tb_case_manage` set result= #{result},is_success=#{isSuccess},update_date=now() where id = #{id} ")
    int executeResult(@Param("id") Integer id,@Param("result") String result,@Param("isSuccess") Integer isSuccess);


}
