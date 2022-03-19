package com.zmj.demo.dao.demodata.auto;

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

    @Insert("INSERT INTO `demo`.`tb_platform_manage` (`platform`,`project`,`module`,`ip`,`state`,`creator`,`create_date`,`update_date`) VALUES (#{platform},#{project},#{module},#{ip},#{state},#{creator},now(),now())")
    int add(@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("ip") String ip, @Param("state") String state, @Param("creator") String creator);

    @Delete("update `demo`.`tb_platform_manage` set is_delete=1,update_date=now() where id = #{id} ")
    int delete(@Param("id") int id);

    @Update("update `demo`.`tb_platform_manage` set platform=#{platform},project=#{project},module=#{module},ip=#{ip},state=#{state},creator=#{creator},update_date=now() where id = #{id}")
    int edit(@Param("id") int id,@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("ip") String ip, @Param("state") String state, @Param("creator") String creator);

    @Select("select platform from `demo`.`tb_platform_manage` GROUP BY platform")
    List<String> listPlatform();

    @Select("select project from `demo`.`tb_platform_manage` where platform=#{platform} GROUP BY project")
    List<String> listProject(@Param("platform") String platform);

    @Select("select id,module from `demo`.`tb_platform_manage` where platform=#{platform} and project=#{project}")
    List<PlatformChain> listModule(@Param("platform") String platform, @Param("project") String project);
}
