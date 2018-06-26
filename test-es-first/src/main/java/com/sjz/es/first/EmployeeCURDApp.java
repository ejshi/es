package com.sjz.es.first;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.lucene.search.Query;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.ToXContent.Params;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryShardContext;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class EmployeeCURDApp {
	
	public static void main(String[] args) throws Exception {
		Settings settings = 
				Settings.builder().put("cluster.name","elasticsearch").build();
		
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		
		createEmployee(client);
		getEmployee(client);
		updateEmployee(client);
		getEmployee(client);
//		deleteEmployee(client);
	}
	
	private static void createEmployee(TransportClient client) throws Exception{
		IndexResponse response = 
				client.prepareIndex("company", "employee","2")
						.setSource(
							XContentFactory.jsonBuilder().startObject()
								.field("name","zhangsan")
								.field("age", 20)
								.field("position", "technique")
								.field("country", "china")
								.field("join_date", "2017-01-01")
								.field("salary", 9000)
								.endObject()
						).get();
		
		System.out.println("--->"+response.getResult());
		
		//test
	}
	
	private static void getEmployee(TransportClient client){
		GetResponse response = client.prepareGet("company", "employee", "1").get();
		System.out.println("--->"+response.getSourceAsString());
	}
	
	private static void updateEmployee(TransportClient client) throws Exception{
		UpdateResponse response = client.prepareUpdate("company", "employee", "1")
				.setDoc(XContentFactory.jsonBuilder()
						.startObject()
							.field("name", "jack1")
						.endObject())
				.get();
		
		System.out.println("--->"+response.getResult());
	}
	
	private static void deleteEmployee(TransportClient client){
		DeleteResponse response = client.prepareDelete("company", "employee", "2").get();
		System.out.println("--->"+response.getResult());
	}
	
	private static void searchEmployee(TransportClient client){
		SearchResponse response = client.prepareSearch("company")
				.setTypes("employee")
					.setQuery(QueryBuilders.matchQuery("name", "jack"))
					 	.get();
		SearchHit[] hits = response.getHits().getHits();
		
		if(null != hits && hits.length > 0){
			for (SearchHit searchHit : hits) {
				System.out.println("--->"+searchHit.getSourceAsString());
			}
		}
		System.out.println("--->搜索结束");
	}
}
