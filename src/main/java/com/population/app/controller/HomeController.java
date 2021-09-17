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
		//only 2018 population list 
				List<countrystats> populist=service.getAllPopulation().stream().filter(p->p.getYear().equals("2018")).collect(Collectors.toList());
		//only 2018 population list 
		model.addAttribute("latestcount", populist);
		//2018 world population
		model.addAttribute("population", service.getAllPopulation().stream().filter(p->p.getYear().equals("2018")).mapToLong(p->Long.parseLong(p.getPopulation())).sum());
		//iterate 2018 population list to find indian population in the year 2018
		for(countrystats record:populist) {
			if(record.getCountry().equals("India")){
				model.addAttribute("Indainpopulation", record.getPopulation());
			}
		}
		return "latest";
	}
}
