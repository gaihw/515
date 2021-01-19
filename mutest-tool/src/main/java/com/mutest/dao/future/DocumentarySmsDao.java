package com.mutest.dao.future;

import com.mutest.model.ShortMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author muguozheng
 * @date 2020/6/22 21:15
 * @Description: 主站短信验证码
 * @modify
 */
@Mapper
public interface DocumentarySmsDao {
    @Select("SELECT mobile,keywords,send_time sendTime FROM documentary_king.msg_sms_record WHERE keywords!='' ORDER BY send_time DESC LIMIT #{pageNum},#{pageSize}")
    List<ShortMessage> documentarySmsList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    @Select("SELECT mobile,keywords,send_time sendTime FROM documentary_king.msg_sms_record WHERE keywords!='' AND mobile=#{mobile} ORDER BY send_time DESC")
    List<ShortMessage> searchDocumentarySmsList(@Param("mobile") String mobile);
}
