package com.zmj.demo.dao.auto;


import com.zmj.demo.domain.auto.CaseChain;
import com.zmj.demo.domain.auto.InterfaceChain;
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
            "select id,interface_manage_id interfaceManageID,case_name caseName,header_data headerData,param_data paramData,assert_type assertType,assert_data assertData,is_success isSuccess,result result,state,creator,create_date createDate,update_date updateDate from `demo`.`tb_case_manage`"+
            "WHERE 1=1"+
            "<if test=\"caseName!=null and caseName!=''\">"+
            "AND case_name = #{caseName}"+
            "</if>"+
            "<if test=\"interfaceManageID!=null and interfaceManageID!=''\">"+
            "AND interface_manage_id = #{interfaceManageID}"+
            "</if>"+
            "<if test=\"isSuccess!=null and isSuccess!=''\">"+
            "AND is_success = #{isSuccess}"+
            "</if>"+
            "AND is_delete = 0 ORDER BY update_date DESC LIMIT #{page},#{limit}"+
            "</script>"})
    List<CaseChain> list(@Param("caseName") String caseName, @Param("interfaceManageID") String interfaceManageID,@Param("isSuccess") int isSuccess, @Param("page") int page, @Param("limit") int limit);

    @Insert("INSERT INTO `demo`.`tb_case_manage` (`interface_manage_id`,`case_name`,`header_data`,`param_data`,`assert_type`,`assert_data`,`state`,`creator`,`create_date`,`update_date`) VALUES (#{interfaceManageID},#{caseName},#{headerData},#{paramData},#{assertType},#{assertData},#{state},#{creator},now(),now())")
    int add(@Param("interfaceManageID") Integer interfaceManageID, @Param("caseName") String caseName, @Param("headerData") String headerData, @Param("paramData") String paramData,@Param("assertType") String assertType,@Param("assertData") String assertData, @Param("state") String state, @Param("creator") String creator);

    @Delete("update `demo`.`tb_case_manage` set is_delete=1,update_date=now() where id = #{id} ")
    int delete(@Param("id") int id);

    @Update("update `demo`.`tb_case_manage` set interface_manage_id=#{interfaceManageID},case_name=#{caseName},header_data=#{headerData},param_data=#{paramData},assert_type=#{assertType},assert_data=#{assertData},state=#{state},creator=#{creator},update_date=now() where id = #{id}")
    int edit(@Param("id") int id,@Param("interfaceManageID") Integer interfaceManageID, @Param("caseName") String caseName, @Param("headerData") String headerData, @Param("paramData") String paramData,@Param("assertType") String assertType,@Param("assertData") String assertData, @Param("state") String state, @Param("creator") String creator);

}
