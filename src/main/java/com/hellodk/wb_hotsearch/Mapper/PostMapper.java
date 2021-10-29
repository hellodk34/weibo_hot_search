package com.hellodk.wb_hotsearch.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hellodk.wb_hotsearch.Bean.Post;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PostMapper extends BaseMapper<Post> {
}
