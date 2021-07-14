package com.chuansen.system.controller;

import com.chuansen.system.service.SearchRequestService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/7/13 17:06
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Value("${elasticsearch.index.name}")
    private String indexName;

    @Autowired
    private SearchRequestService searchService;

    /**
     * 查询所有索引数据
     *
     * @return
     */
    @RequestMapping("/matchAllQuery")
    public ResponseEntity matchAllQuery() {
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 条件构造
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        SearchResponse response = searchService.searchIndexList(sourceBuilder, indexName);
        return ResponseEntity.ok(response.getHits());
    }

    /**
     * term 查询，查询条件为关键字
     */
    @RequestMapping("/termQuery")
    public ResponseEntity termQuery() {
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 条件构造
        sourceBuilder.query(QueryBuilders.termQuery("username", "占"));

        SearchResponse response = searchService.searchIndexList(sourceBuilder, indexName);
        return ResponseEntity.ok(response.getHits());
    }

    /**
     * 分页查询+排序
     */
    @RequestMapping("/pageList")
    public ResponseEntity pageList() {
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 条件构造
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        // 分页查询   当前页其实索引(第一条数据的顺序号)，from  每页显示多少条 size
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.sort("created", SortOrder.DESC);

        SearchResponse response = searchService.searchIndexList(sourceBuilder, indexName);
        return ResponseEntity.ok(response.getHits());
    }

    /**
     * 过滤字段查询
     */
    @RequestMapping("/excludes")
    public ResponseEntity excludes() {
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 条件构造
        sourceBuilder.query(QueryBuilders.matchAllQuery());

        //查询 excludes排除字段   显示字段includes
        String[] excludes = {"updated", "beforeContent"};
        String[] includes = {};
        sourceBuilder.fetchSource(includes, excludes);

        SearchResponse response = searchService.searchIndexList(sourceBuilder, indexName);
        return ResponseEntity.ok(response.getHits());
    }

    /**
     * Bool 多条件查询
     * @return
     */
    @RequestMapping("/boolQuery")
    public ResponseEntity boolQuery(){
        /// 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 条件构造
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 必须包含
        //boolQueryBuilder.must(QueryBuilders.matchQuery("modelCnName", "课程管理"));
        // 一定不含
        //boolQueryBuilder.mustNot(QueryBuilders.matchQuery("username", "占传森2"));
        // 可能包含
        //boolQueryBuilder.should(QueryBuilders.matchQuery("afterContent", "how"));
        sourceBuilder.query(boolQueryBuilder);

        SearchResponse response = searchService.searchIndexList(sourceBuilder, indexName);
        return ResponseEntity.ok(response.getHits());
    }

    /**
     * rangeQuery范围查询
     * @return
     */
    @RequestMapping("/rangeQuery")
    public ResponseEntity rangeQuery() {
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 条件构造
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("handleType");
        // 大于等于
        rangeQuery.gte("2");
        // 小于等于
        rangeQuery.lte("1");
        sourceBuilder.query(rangeQuery);

        SearchResponse response = searchService.searchIndexList(sourceBuilder, indexName);
        return ResponseEntity.ok(response.getHits());
    }

    /**
     * 模糊查询
     * @return
     */
    @RequestMapping("/likeQuery")
    public ResponseEntity likeQuery() {
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.fuzzyQuery("modelCnName","课程").fuzziness(Fuzziness.ONE));

        SearchResponse response = searchService.searchIndexList(sourceBuilder, indexName);
        return ResponseEntity.ok(response.getHits());
    }

    /**
     * TermsQuery 高亮查询
     */
    @RequestMapping("/termsQuery")
    public ResponseEntity termsQuery() {
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //构建查询方式：高亮查询
        TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("username","占");
        //设置查询方式
        sourceBuilder.query(termsQueryBuilder);

        //构建高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");//设置标签前缀
        highlightBuilder.postTags("</font>");//设置标签后缀
        highlightBuilder.field("username");//设置高亮字段
        //设置高亮构建对象
        sourceBuilder.highlighter(highlightBuilder);

        SearchResponse response = searchService.searchIndexList(sourceBuilder, indexName);
        return ResponseEntity.ok(response.getHits());
    }



}
