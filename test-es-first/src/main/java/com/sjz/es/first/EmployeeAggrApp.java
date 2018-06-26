package com.sjz.es.first;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class EmployeeAggrApp {
	
	private final static String _INDEX = "company";
	private final static String _TYPE = "employee";
	
	public static void main(String[] args) throws Exception {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
		
		TransportClient client = new PreBuiltTransportClient(settings)
			.addTransportAddress(
					new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
		
		aggrEmployee(client);
		
		client.close();
		
	}
	
	private static void aggrEmployee(TransportClient client){
		SearchResponse searchResponse = client.prepareSearch(_INDEX).setTypes(_TYPE)
				.addAggregation(AggregationBuilders.terms("group_by_country").field("country")
						.subAggregation(AggregationBuilders
								.dateHistogram("group_by_join_date")
								.field("join_date")
								.dateHistogramInterval(DateHistogramInterval.YEAR)
								.format("yyyy-MM-dd")
								.subAggregation(AggregationBuilders.avg("avg_salary").field("salary"))
						)
			).execute().actionGet();
		
		Map<String, Aggregation> aggrMap = searchResponse.getAggregations().asMap();
		
		StringTerms groupByCountry = (StringTerms) aggrMap.get("group_by_country");
		
		List<Bucket> countryBuckets = groupByCountry.getBuckets();
		if(null != countryBuckets && !countryBuckets.isEmpty()){
			for (Bucket countryBucket : countryBuckets) {
				System.out.println("--->"+countryBucket.getKey()+">>>"+countryBucket.getDocCount());
				
				Histogram groupByJoinDate = (Histogram) countryBucket.getAggregations().asMap().get("group_by_join_date");
				List<org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket> joinDateBuckets = groupByJoinDate.getBuckets();
				if(null != joinDateBuckets && !joinDateBuckets.isEmpty()){
					for (org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket joinDateBucket : joinDateBuckets) {
						System.out.println("sub--->"+joinDateBucket.getKeyAsString()+">>>"+joinDateBucket.getDocCount());
						Map<String, Aggregation> asMap = joinDateBucket.getAggregations().asMap();
						Avg avg = (Avg) asMap.get("avg_salary");
						System.out.println("--->"+avg.getValue());
					}
				}
			}
		}
	}
}
