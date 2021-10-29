package com.hellodk.wb_hotsearch.Bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: hellodk
 * @description Bean
 * @date: 10/18/2021 4:23 PM
 */

@Data
@TableName("wb_hotsearch")
public class Post {

    @TableId(type = IdType.AUTO)
    private Long postId;

    private String postTime;

    private String city;

    private String postTitle;

    private String content;

    private Date createdTime;
}
