package com.hellodk.wb_hotsearch.Bean;

import lombok.Data;

/**
 * @author: hellodk
 * @description result
 * @date: 10/18/2021 4:42 PM
 */

/**
 * 向前端返回信息封装
 *
 * @param <T> 可变类型
 */

@Data
public class Result<T> {
    /**
     * 返回信息
     */
    private String msg;

    /**
     * 数据是否正常请求
     */
    private boolean success;

    /**
     * 具体返回的数据
     */
    private T detail;

}

