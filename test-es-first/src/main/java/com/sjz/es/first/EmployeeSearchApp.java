package com.sjz.es.first;

import java.net.InetAddress;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class EmployeeSearchApp {

	private final static String _INDEX = "company";
	private final static String _TYPE = "employee";

	public static void main(String[] args) throws Exception {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
//		createEmployee(client);
		searchEmployee(client);
		
		client.close();
	}
//	（1）搜索职位中包含technique的员工
//	（2）同时要求age在30到40岁之间
//	（3）分页查询，查找第一页
	private static void searchEmployee(TransportClient client) {
		SearchResponse response = 
					client.prepareSearch(_INDEX).setTypes(_TYPE)
							.setQuery(
									QueryBuilders.boolQuery()
										.must(QueryBuilders.matchQuery("position", "technique"))
										.must(QueryBuilders.rangeQuery("age").from(30).to(40))
							)
							.setFrom(0)
							.setSize(2)
							.get();
		
		SearchHit[] hits = response.getHits().getHits();
		if (null != hits && hits.length > 0) {
			for (SearchHit searchHit : hits) {
				System.out.println("--->" + searchHit.getSourceAsString());
			}
		} else {
			System.out.println("--->查询无数据");
		}
	}
	//生成测试数据
	private static void createEmployee(TransportClient client) throws Exception {
		client.prepareIndex(_INDEX, _TYPE, "1")
				.setSource(XContentFactory.jsonBuilder()
						.startObject()
							.field("name", "jack").field("age", 27)
							.field("position", "technique software")
							.field("country", "china")
							.field("join_date", "2017-01-01")
							.field("salary", 10000)
						.endObject())
				.get();

		client.prepareIndex(_INDEX, _TYPE, "2")
				.setSource(XContentFactory.jsonBuilder()
						.startObject().field("name", "marry")
							.field("age", 35)
							.field("position", "technique manager")
							.field("country", "china")
							.field("join_date", "2017-01-01")
							.field("salary", 12000)
						.endObject())
				.get();

		client.prepareIndex(_INDEX, _TYPE, "3")
				.setSource(XContentFactory.jsonBuilder()
						.startObject()
							.field("name", "tom")
							.field("age", 32)
							.field("position", "senior technique software")
							.field("country", "china")
							.field("join_date", "2016-01-01")
							.field("salary", 11000)
						.endObject())
				.get();

		client.prepareIndex(_INDEX, _TYPE, "4")
				.setSource(XContentFactory.jsonBuilder()
						.startObject().field("name", "jen")
							.field("age", 25)
							.field("position", "junior finance")
							.field("country", "usa")
							.field("join_date", "2016-01-01")
							.field("salary", 7000)
						.endObject())
				.get();

		client.prepareIndex(_INDEX, _TYPE, "5")
				.setSource(XContentFactory.jsonBuilder()
						.startObject()
							.field("name", "mike")
							.field("age", 37)
							.field("position", "finance manager")
							.field("country", "usa")
							.field("join_date", "2015-01-01")
							.field("salary", 15000)
						.endObject())
				.get();
		
		System.out.println("--->索引添加成功");
	}
}
