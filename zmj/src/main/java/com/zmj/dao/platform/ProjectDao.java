package com.zmj.dao.platform;

import com.zmj.domain.interfaceTest.ProjectApiChain;
import com.zmj.domain.interfaceTest.ProjectCaseChain;
import com.zmj.domain.interfaceTest.ProjectChain;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ProjectDao {
    /**
     * platform相关操作
     */

    @Select({"<script>"+
            "SELECT count(*) FROM `sonar`.`tb_project_platform`"+
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
    int acount(@Param("platform") String platform,@Param("project") String project,@Param("module") String module,@Param("ip") String ip);

    @Select({"<script>"+
            "select id,platform,project,module,ip,state,creator,create_date createDate,modify_date modifyDate from `sonar`.`tb_project_platform`"+
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
            " ORDER BY modify_date DESC LIMIT #{page},#{limit}"+
            "</script>"})
    List<ProjectChain> list(@Param("platform") String platform,@Param("project") String project,@Param("module") String module,@Param("ip") String ip,@Param("page") int page, @Param("limit") int limit);

    @Insert("INSERT INTO `sonar`.`tb_project_platform` (`platform`,`project`,`module`,`ip`,`state`,`creator`) VALUES (#{platform},#{project},#{module},#{ip},#{state},#{creator})")
    int addNewProject(@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("ip") String ip, @Param("state") String state, @Param("creator") String creator);

    @Delete("delete from `sonar`.`tb_project_platform` where id = #{id} ")
    int deleteData(@Param("id") int id);

    @Update("update `sonar`.`tb_project_platform` set platform=#{platform},project=#{project},module=#{module},ip=#{ip},state=#{state},creator=#{creator} where id = #{id}")
    int edit(@Param("id") int id,@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("ip") String ip, @Param("state") String state, @Param("creator") String creator);

    /**
     * api相关操作
     */

    @Select({"<script>"+
            "SELECT count(*) FROM `sonar`.`tb_project_api`"+
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
            "<if test=\"name!=null and name!=''\">"+
            "AND name = #{name}"+
            "</if>"+
            "<if test=\"path!=null and path!=''\">"+
            "AND path = #{path}"+
            "</if>"+
            "<if test=\"method!=null and method!=''\">"+
            "AND method = #{method}"+
            "</if>"+
            "<if test=\"param!=null and param!=''\">"+
            "AND param = #{param}"+
            "</if>"+
            "</script>"})
    int apiAcount(@Param("platform") String platform,@Param("project") String project,@Param("module") String module,@Param("name") String name,@Param("path") String path,@Param("method") String method,@Param("param") String param);

    @Select({"<script>"+
            "select id,platform_id platformId,platform,project,module,name,path,method,param,is_token isToken,state,creator,create_date createDate,modify_date modifyDate from `sonar`.`tb_project_api`"+
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
            "<if test=\"name!=null and name!=''\">"+
            "AND name = #{name}"+
            "</if>"+
            "<if test=\"path!=null and path!=''\">"+
            "AND path = #{path}"+
            "</if>"+
            "<if test=\"method!=null and method!=''\">"+
            "AND method = #{method}"+
            "</if>"+
            "<if test=\"param!=null and param!=''\">"+
            "AND param = #{param}"+
            "</if>"+
            " ORDER BY modify_date DESC LIMIT #{page},#{limit}"+
            "</script>"})
    List<ProjectApiChain> apiList(@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("name") String name, @Param("path") String path, @Param("method") String method, @Param("param") String param,@Param("page") int page, @Param("limit") int limit);

    @Select("select platform from sonar.tb_project_platform GROUP BY platform")
    List<String> apiListPlatform();

    @Select("select project from sonar.tb_project_platform where platform=#{platform} GROUP BY project")
    List<String> apiListProject(@Param("platform") String platform);

    @Select("select id,module from sonar.tb_project_platform where platform=#{platform} and project=#{project}")
    List<ProjectApiChain> apiListModule(@Param("platform") String platform, @Param("project") String project);

    @Insert("INSERT INTO `sonar`.`tb_project_api` (`platform_id`,`platform`,`project`,`module`,`name`,`path`,`method`,`param`,`is_token`,`state`,`creator`) VALUES (#{platformId},#{platform},#{project},#{module},#{name},#{path},#{method},#{param},#{isToken},#{state},#{creator})")
    int addNewApi(@Param("platformId") int platformId,@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("name") String name,@Param("path") String path, @Param("method") String method,@Param("param") String param, @Param("isToken") int isToken,@Param("state") String state, @Param("creator") String creator);


    @Delete("delete from `sonar`.`tb_project_api` where id = #{id} ")
    int deleteApiData(@Param("id") int id);

    @Update("update `sonar`.`tb_project_api` set platform_id=#{platformId},platform=#{platform},project=#{project},module=#{module},name=#{name},path=#{path},method=#{method},param=#{param},is_token=#{isToken},state=#{state},creator=#{creator} where id = #{id}")
    int editApi(@Param("id") int id,@Param("platformId") int platformId,@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("name") String name,@Param("path") String path, @Param("method") String method,@Param("param") String param, @Param("isToken") int isToken, @Param("state") String state, @Param("creator") String creator);

    /**
     * case相关
     */

    @Select({"<script>"+
            "SELECT count(*) FROM `sonar`.`tb_project_case`"+
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
            "<if test=\"name!=null and name!=''\">"+
            "AND name = #{name}"+
            "</if>"+
            "<if test=\"param!=null and param!=''\">"+
            "AND param = #{param}"+
            "</if>"+
            "<if test=\"assertData!=null and assertData!=''\">"+
            "AND assert_data = #{assertData}"+
            "</if>"+
            "</script>"})
    int caseAcount(@Param("platform") String platform,@Param("project") String project,@Param("module") String module,@Param("name") String name,@Param("param") String param,@Param("assertData") String assertData);

    @Select({"<script>"+
            "select id,api_id apiId,platform,project,module,name,param,assert_data assertData,state,creator,create_date createDate,modify_date modifyDate from `sonar`.`tb_project_case`"+
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
            "<if test=\"name!=null and name!=''\">"+
            "AND name = #{name}"+
            "</if>"+
            "<if test=\"param!=null and param!=''\">"+
            "AND param = #{param}"+
            "</if>"+
            "<if test=\"assertData!=null and assertData!=''\">"+
            "AND assert_data = #{assertData}"+
            "</if>"+
            " ORDER BY modify_date DESC LIMIT #{page},#{limit}"+
            "</script>"})
    List<ProjectCaseChain> caseList(@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("name") String name,@Param("param") String param,@Param("assertData") String assertData, @Param("page") int page, @Param("limit") int limit);

    @Select("select id,name from sonar.tb_project_api where platform=#{platform} and project=#{project} and module=#{module}")
    List<ProjectCaseChain> caseListName(@Param("platform") String platform, @Param("project") String project, @Param("module") String module);

    @Insert("INSERT INTO `sonar`.`tb_project_case` (`api_id`,`platform`,`project`,`module`,`name`,`param`,`assert_data`,`state`,`creator`) VALUES (#{apiId},#{platform},#{project},#{module},#{name},#{param},#{assertData},#{state},#{creator})")
    int addNewCase(@Param("apiId") int apiId,@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("name") String name,@Param("param") String param, @Param("assertData") String assertData,@Param("state") String state, @Param("creator") String creator);


    @Delete("delete from `sonar`.`tb_project_case` where id = #{id} ")
    int deleteCaseData(@Param("id") int id);

    @Update("update `sonar`.`tb_project_case` set api_id=#{apiId},platform=#{platform},project=#{project},module=#{module},name=#{name},param=#{param},assertData=#{assertData},state=#{state},creator=#{creator} where id = #{id}")
    int editCase(@Param("id") int id,@Param("apiId") int apiId,@Param("platform") String platform, @Param("project") String project, @Param("module") String module, @Param("name") String name,@Param("param") String param, @Param("assertData") String assertData, @Param("state") String state, @Param("creator") String creator);

    @Select("select tpc.id id,tpc.api_id apiId,tpa.platform_id platformId,tpc.platform platform,tpc.project project,tpc.module module,tpc.name name,tpp.ip ip,tpa.path path,tpa.method method,tpa.param paramType,tpa.is_token isToken,tpc.param param,tpc.assert_data assertData,tpc.state state,tpc.creator creator,tpc.create_date createDate from `sonar`.tb_project_case tpc inner JOIN `sonar`.tb_project_api tpa on tpc.api_id=tpa.id INNER JOIN `sonar`.tb_project_platform tpp on tpa.platform_id=tpp.id where tpc.id=#{id}")
    List<ProjectCaseChain> caseInfo(@Param("id") int id);
}
