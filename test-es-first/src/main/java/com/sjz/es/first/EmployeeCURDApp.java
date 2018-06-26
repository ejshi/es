package com.sjz.es.first;

import java.io.IOException;
import java.net.InetAddress;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class EmployeeCURDApp {
	
	public static void main(String[] args) throws Exception {
		Settings settings = 
				Settings.builder().put("cluster.name","elasticsearch").build();
		
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
		
//		createEmployee(client);
		getEmployee(client);
		updateEmployee(client);
		getEmployee(client);
		deleteEmployee(client);
	}
	
	private static void createEmployee(TransportClient client) throws Exception{
		IndexResponse response = 
				client.prepareIndex("company", "employee","3")
						.setSource(
							XContentFactory.jsonBuilder().startObject()
								.field("name","lisi")
								.field("age", 30)
								.field("position", "technique")
								.field("country", "china")
								.field("join_date", "2018-01-01")
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
}
