package com.earnest.crawler.core.handler1;

import com.earnest.crawler.core.HttpResponseResult;

import java.io.Serializable;

/**
 * 处理{@link HttpResponseResult}，返回最终结果<code>RESULT</code>的类型。
 *
 * @param <TYPE>   需要处理的{@link HttpResponseResult}的类型。
 * @param <H>      具体{@link HttpResponseResult}的实现类型。
 * @param <RESULT> 获得的结果类型。
 */
@FunctionalInterface
public interface HttpResponseHandler<TYPE, H extends HttpResponseResult<TYPE>, RESULT> {

    RESULT handle(H h);

}
