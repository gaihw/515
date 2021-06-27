package com.zmj.demo.dao.auto;

import com.zmj.demo.domain.auto.PlatformChain;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PlatformDao {
    @Select({"<script>"+
            "SELECT count(*) FROM `demo`.`tb_platform_manage`"+
            "WHERE 1=1"+
            "<if test=\"platform!=null and platform!=''\">"+
            "AND platform = #{platform}"+
            "</if>"+
            "<if test=\"project!=null and project!=''\">"+
            "AND project = #{project}"+
            "</if>"+
            "<if test=\"module!=null and module!=''\">"+
            "AND module = #{module}"+
            "</if>"+
            "<if test=\"ip!=null and ip!=''\">"+
            "AND ip = #{ip}"+
            "</if>"+
            "</script>"})
    int acount(@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("ip") String ip);

    @Select({"<script>"+
            "select id,platform,project,module,ip,state,creator,create_date createDate,update_date updateDate from `demo`.`tb_platform_manage`"+
            "WHERE 1=1"+
            "<if test=\"platform!=null and platform!=''\">"+
            "AND platform = #{platform}"+
            "</if>"+
            "<if test=\"project!=null and project!=''\">"+
            "AND project = #{project}"+
            "</if>"+
            "<if test=\"module!=null and module!=''\">"+
            "AND module = #{module}"+
            "</if>"+
            "<if test=\"ip!=null and ip!=''\">"+
            "AND ip = #{ip}"+
            "</if>"+
            "AND is_delete = 0 ORDER BY update_date DESC LIMIT #{page},#{limit}"+
            "</script>"})
    List<PlatformChain> list(@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("ip") String ip, @Param("page") int page, @Param("limit") int limit);

//    @Insert("INSERT INTO `sonar`.`tb_project_platform` (`platform`,`project`,`module`,`ip`,`state`,`creator`) VALUES (#{platform},#{project},#{module},#{ip},#{state},#{creator})")
//    int addNewProject(@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("ip") String ip, @Param("state") String state, @Param("creator") String creator);
//
//    @Delete("delete from `sonar`.`tb_project_platform` where id = #{id} ")
//    int deleteData(@Param("id") int id);
//
//    @Update("update `sonar`.`tb_project_platform` set platform=#{platform},project=#{project},module=#{module},ip=#{ip},state=#{state},creator=#{creator} where id = #{id}")
//    int edit(@Param("id") int id,@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("ip") String ip, @Param("state") String state, @Param("creator") String creator);

}
