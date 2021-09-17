# WorldPopulation
This application show world population country wide

----Steps to send Http Request and process http Response from java Application

1>create of http client 
```HttpClient clinet=HttpClient.newHttpclinet();```

2>create Http Request Object

```HttpRequest req=HttpRequest.newBuider(new URI(url).GET().build();```

3>send HttpRequest by using HttpClient and Get the HttpResponse
```HttpResponse resp=client.send(req,HttpResponse.BodyHandler.asString());```

4>process HttpResponse 

By Using ``resp``` variable we can process the data 

**Model Class**
```
package com.population.app.model;

public class countrystats {
	private String country;
	private String countryCode;
	private String  year;
	private String population;
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getPopulation() {
		return population;
	}
	public void setPopulation(String population) {
		this.population = population;
	}
	@Override
	public String toString() {
		return "countrystats [country=" + country + ", countryCode=" + countryCode + ", year=" + year + ", population="
				+ population + "]";
	}
	
}

```


**service class**

```
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

```
**Controller**

```
package com.population.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.population.app.model.countrystats;
import com.population.app.service.PopulationDataService;

@Controller
@RequestMapping("population")
public class HomeController {
	@Autowired
	PopulationDataService service;

	@GetMapping("/all")
	public String home(Model model) {
		model.addAttribute("countrystats", service.getAllPopulation());
		return "home";
	}

	@GetMapping("/latest")
	public String Latest(Model model) {
		// only 2018 population list
		List<countrystats> populist = service.getAllPopulation().stream().filter(p -> p.getYear().equals("2018"))
				.collect(Collectors.toList());
		// only 2018 population list
		model.addAttribute("latestcount", populist);
		// 2018 world population
		model.addAttribute("population", service.getAllPopulation().stream().filter(p -> p.getYear().equals("2018"))
				.mapToLong(p -> Long.parseLong(p.getPopulation())).sum());
		// iterate 2018 population list to find indian population in the year 2018
		for (countrystats record : populist) {
			if (record.getCountry().equals("India")) {
				model.addAttribute("Indainpopulation", record.getPopulation());
			}
		}
		return "latest";
	}
}

```
**Front end**
```
<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">

<head>
<title>COVID CASES TRACKER</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
	crossorigin="anonymous">
<div class="jumbotron">
	<h1 align="center">WORLD POPULATION LIST</h1>
	<p class="lead">TOTAL POPULATION REPORTED OF THE WORLD</p>
	<h3 class="display-4" th:text="${population}"></h3>
	<hr class="my-4">
	<p class="lead">TOTAL INDAIN POPULATION</p>
	<h3 class="display-4" th:text="${Indainpopulation}"></h3>
	<hr class="my-4">
</div>
</head>

<body>
	<div class="container">
		<table class="table table-striped" style="width: 80%" align="center">
			<tr>
				<th>COUNTRY</th>
				<th>COUNTRY-CODE</th>
				<th>YEAR</th>
				<th>POPULATION</th>
			</tr>
			<tr th:each="stats : ${latestcount}">
				<td th:text="${stats.country}"></td>
				<td th:text="${stats.countryCode}"></td>
				<td th:text="${stats.year}">0</td>
				<td th:text="${stats.population}">0</td>
			</tr>
		</table>
	</div>
</body>

</html>
```
```

<!DOCTYPE html>

<html xmlns:th="http://www.thymeleaf.org">

<head>
<title>WORLD POPULATION LIST</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
	crossorigin="anonymous">
<div class="jumbotron">
<h1 align="center">WORLD POPULATION LIST</h1>
</div>
</head>

<body>

<div class="container">
	<table class="table table-striped" style="width:80%" align="center">
		<tr> 
			<th>COUNTRY</th>
			<th>COUNTRY-CODE</th>
			<th>YEAR</th>
			<th>POPULATION</th>
		</tr>
		<tr th:each="stats : ${countrystats}">
			<td th:text="${stats.country}"></td>
			<td th:text="${stats.countryCode}"></td>
			<td th:text="${stats.year}">0</td>
			<td th:text="${stats.population}">0</td>
		</tr>
	</table>
</div>
</body>

</html>
```
