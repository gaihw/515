package com.zmj.dao.orther.future;

import com.zmj.domain.orther.ShortMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DocumentarySmsDao {
    @Select("SELECT mobile,keywords,send_time sendTime FROM documentary_king.msg_sms_record WHERE keywords!='' ORDER BY send_time DESC LIMIT #{pageNum},#{pageSize}")
    List<ShortMessage> documentarySmsList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);

    @Select("SELECT mobile,keywords,send_time sendTime FROM documentary_king.msg_sms_record WHERE keywords!='' AND mobile=#{mobile} ORDER BY send_time DESC")
    List<ShortMessage> searchDocumentarySmsList(@Param("mobile") String mobile);
}
