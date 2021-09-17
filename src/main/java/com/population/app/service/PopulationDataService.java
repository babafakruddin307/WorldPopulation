package com.population.app.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.population.app.model.countrystats;

@Service
public class PopulationDataService {
	
	String POPULATION_DATA_URL="https://raw.githubusercontent.com/datasets/population/master/data/population.csv";
	
	List<countrystats> allstats=new ArrayList<>();
	
	List<countrystats> allsortstats=new ArrayList<>();
	
	//all years population list
	public List<countrystats> getAllPopulation() {
		return allstats;
	}
	
	@PostConstruct
	@Scheduled(cron="1 * * * * * ")
	public void populationData() throws IOException, InterruptedException {
		//all years population list
		List<countrystats> newstats=new ArrayList<>();
		
		
		HttpClient client=HttpClient.newHttpClient();
		HttpRequest request=HttpRequest.newBuilder().uri(URI.create(POPULATION_DATA_URL)).build();
		
		HttpResponse<String> httpResponse=client.send(request, HttpResponse.BodyHandlers.ofString());
		//System.out.println(httpResponse.body());
		
		StringReader csvBodyReader=new StringReader(httpResponse.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		for (CSVRecord record : records) {
			countrystats stats=new countrystats();
			stats.setCountry(record.get("Country Name"));
			stats.setCountryCode(record.get("Country Code"));
			stats.setYear(record.get("Year"));
			stats.setPopulation(record.get("Value"));
			newstats.add(stats);
		}
		this.allstats=newstats;
	}
	
}
