package com.zmj.demo.dao.auto;


import com.zmj.demo.domain.auto.InterfaceChain;
import com.zmj.demo.domain.auto.PlatformChain;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InterfaceDao {

    @Select({"<script>"+
            "SELECT count(*) FROM `demo`.`tb_interface_manage`"+
            "WHERE 1=1 AND is_delete=0 "+
            "<if test=\"name!=null and name!=''\">"+
            "AND name = #{name}"+
            "</if>"+
            "<if test=\"path!=null and path!=''\">"+
            "AND path = #{path}"+
            "</if>"+
            "</script>"})
    int acount(@Param("name") String name, @Param("path") String path);

    @Select({"<script>"+
            "select id,platform_manage_id platformManageID,name,path,method,content_type contentType,state,creator,create_date createDate,update_date updateDate from `demo`.`tb_interface_manage`"+
            "WHERE 1=1"+
            "<if test=\"name!=null and name!=''\">"+
            "AND name = #{name}"+
            "</if>"+
            "<if test=\"path!=null and path!=''\">"+
            "AND path = #{path}"+
            "</if>"+
            "AND is_delete = 0 ORDER BY update_date DESC LIMIT #{page},#{limit}"+
            "</script>"})
    List<InterfaceChain> list(@Param("name") String name, @Param("path") String path, @Param("page") int page, @Param("limit") int limit);
	

    @Insert("INSERT INTO `demo`.`tb_interface_manage` (`platform_manage_id`,`name`,`path`,`method`,`content_type`,`state`,`creator`,`create_date`,`update_date`) VALUES (#{platformManageID},#{name},#{path},#{method},#{contentType},#{state},#{creator},now(),now())")
    int add(@Param("platformManageID") Integer platformManageID, @Param("name") String name, @Param("path") String path, @Param("method") String method,@Param("contentType") String contentType, @Param("state") String state, @Param("creator") String creator);

    @Delete("update `demo`.`tb_interface_manage` set is_delete=1,update_date=now() where id = #{id} ")
    int delete(@Param("id") int id);

    @Update("update `demo`.`tb_interface_manage` set platform_manage_id=#{platformManageID},name=#{name},path=#{path},method=#{method},content_type=#{contentType},state=#{state},creator=#{creator},update_date=now() where id = #{id}")
    int edit(@Param("id") int id,@Param("platformManageID") Integer platformManageID, @Param("name") String name, @Param("path") String path, @Param("method") String method,@Param("contentType") String contentType, @Param("state") String state, @Param("creator") String creator);

}
