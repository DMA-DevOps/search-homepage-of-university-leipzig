package de.uni_leipzig.search_engine.events;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.Data;

@Data
@RunWith(Parameterized.class)
public class SearchResultSelectedEventTest
{
	private static final String MOCKED_SESSION_ID = "my_session_id";
	
	private final String expectedQuery;
	
	private final int expectedResultPage;
	
	private final long expectedDocumentId;
	
	private final String inputUrl;
	
	private final String referrerUrl;
	
	@Parameters
	public static Collection<Object> parameters()
	{
		return Arrays.asList
			(
				new Object[] {"Medien,Gameboy,Youtube", 1, 1362, "http://localhost:7777/api/v1/url?documentId=1362&documentId=1362",  "http://www.bla.de/api/v1/search?q=Medien&q=Gameboy&q=Youtube"},
				new Object[] {"Medien,Gameboy,Youtube", 1, 1362, "http://localhost:7777/api/v1/url?documentId=1362&documentId=111", "http://www.bla.de/api/v1/search?q=Medien,Gameboy,Youtube"},
				new Object[] {"Medien,Youtube", 1, 1, "http://localhost:7777/api/v1/url?documentId=1&documentId=111", "http://www.bla.de/api/v1/search?q=Medien&q=Youtube"},
				new Object[] {"Medien,Youtube", 1, 1, "http://localhost:7777/api/v1/url?documentId=1&documentId=111", "http://www.bla.de/api/v1/search?q=Medien,Youtube"},
				new Object[] {"Medien,Youtube", 2, 1, "http://localhost:7777/api/v1/url?documentId=1&documentId=111", "http://www.bla.de/api/v1/search?q=Medien&q=Youtube&p=2&p=4"},
				new Object[] {"Medien,Youtube", 2, 1, "http://localhost:7777/api/v1/url?documentId=1&documentId=111", "http://www.bla.de/api/v1/search?q=Medien,Youtube&p=2&p=4"},
				new Object[] {"Medien", 2, 1, "http://localhost:7777/api/v1/url?documentId=1","http://www.bla.de/api/v1/search?q=Medien&p=2"},
				new Object[] {"Medien", 1, 1, "http://localhost:7777/api/v1/url?documentId=1", "http://www.bla.de/api/v1/search?q=Medien"},
				new Object[] {"Medien", 1, 1, "http://localhost:7777/api/v1/url?documentId=1", "http://www.bla.de/api/v1/search?q=Medien&p=drei"}
			);
	}
	
	@Test
	public void assertSearchResultSelectedEventHasQueryAndPage()
	{
		Map<String, String> header = new HashMap<>();
		header.put("key-1", "value-1");
		header.put("key-2", "value-2");
		header.put("referer", referrerUrl);
		
		HttpServletRequest request = TestUtils.mockServletRequest(inputUrl, header);
		ServletRequestAttributes attributes = new ServletRequestAttributes(request);
		
		attributes = Mockito.spy(attributes);
		Mockito.doReturn(MOCKED_SESSION_ID).when(attributes).getSessionId();
		WebResponseEvent event = new WebResponseEvent(attributes);
		Map<String, Object> responseModel = new HashMap<>();
		responseModel.put(SearchResultSelectedEvent.LOCATION, "www.google.de");
		event.setResponseModel(responseModel);
		SearchResultSelectedEvent actual = SearchResultSelectedEvent.fromWebResponseEvent(event);
		
		Assert.assertEquals(expectedQuery, actual.getQuery());
		Assert.assertEquals(expectedResultPage, actual.getSearchPage());
		Assert.assertEquals("www.google.de", actual.getDocumentUri());
		Assert.assertEquals(expectedDocumentId, actual.getResultId());
		
		Assert.assertEquals(MOCKED_SESSION_ID, actual.getClientId());
		Assert.assertEquals(responseModel, actual.getResponseModel());
		Assert.assertEquals(header, actual.getRequest().getHeaders());
		Assert.assertEquals("0.0.0.0", actual.getRequest().getRemoteAddr());
		Assert.assertEquals("localhost", actual.getRequest().getRemoteHost());
		Assert.assertEquals(Integer.valueOf(1234), actual.getRequest().getRemotePort());
		Assert.assertEquals(inputUrl, actual.getRequest().getRequestUrl());
	}
}
