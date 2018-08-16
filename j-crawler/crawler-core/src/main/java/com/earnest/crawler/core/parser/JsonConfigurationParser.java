package com.earnest.crawler.core.parser;


/**
 * {
 *   "from": "http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html",
 *   "thread": 5,
 *   "match": "/www/4/38-------------4-\\d-1-iqiyi--.html",
 *   "consumer": [
 *     ""
 *   ],
 *   "request": {
 *     "method": "GET",
 *     "charset": "UTF-8",
 *     "cookies": {
 *       "__uuid": "48c04310-fe61-4b7e-d7ba-2178d31b2ea5"
 *     },
 *     "headers": {
 *       "_1_auth": "b7zDuUiMlnJGF8BArM0Eg0LH2vbgBv",
 *       "_1_ver": "0.3.0",
 *       "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"
 *     },
 *     "parameters": {}
 *   },
 *   "custom": {
 *     "strip": "body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li",
 *     "entity": {
 *       "image": "div.site-piclist_pic > a > img->src",
 *       "title": "div.site-piclist_pic > a > img->title",
 *       "href": "div.site-piclist_pic > a->abs:href"
 *     }
 *   }
 * }
 */
public class JsonConfigurationParser implements Parser {



    @Override
    public void close() {
        //ignored
    }
}
