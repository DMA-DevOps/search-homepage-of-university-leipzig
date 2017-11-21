package de.uni_leipzig.search_engine_uni.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.suggest.Lookup.LookupResult;
import org.apache.lucene.search.suggest.analyzing.FuzzySuggester;
import org.apache.lucene.search.suggest.fst.WFSTCompletionLookup;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.SneakyThrows;

@Controller
public class RecommendationController
{
	private final WFSTCompletionLookup completionLookup = new WFSTCompletionLookup(new RAMDirectory(), "sadsa");
	
	private final FuzzySuggester fuzzySuggester = new FuzzySuggester(new RAMDirectory(), "sadsa", new SimpleAnalyzer());
	@Autowired
	@SneakyThrows
	public RecommendationController(Dictionary dict)
	{
//		FuzzySuggester fuzzySuggester = new FuzzySuggester(new RAMDirectory(), "sadsa", new SimpleAnalyzer());
		fuzzySuggester.build(dict);
	}
	
	@SneakyThrows
	@RequestMapping(method=RequestMethod.GET, path="queryRecommendation")
	public Object recommendForUserQuery(@RequestParam String query)
	{
		List<LookupResult> ret = fuzzySuggester.lookup(query, Boolean.FALSE, 10);
		
		for(LookupResult lookupResult : ret)
		{
			System.out.println(lookupResult);
		}
		
		return new QueryRecommendation(ret.stream()
				.map(i -> String.valueOf(i.key))
				.collect(Collectors.toList()));
	}
}
