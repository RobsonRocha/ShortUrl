package br.com.shorturl.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.shorturl.service.ShortenerURLService;
import br.com.shoturl.exception.CustomAliasAlreadyExistsException;
import br.com.shoturl.exception.InternalErrorException;
import br.com.shoturl.exception.InvalidURLException;
import br.com.shoturl.exception.ShortURLNotFoundException;
import br.com.shoturl.exception.UnableToCreateException;
import br.com.shoturl.pojo.ShortURL;
import br.com.shoturl.pojo.Statistics;
import br.com.shoturl.pojo.Top10;


@RestController
public class MainController {	
	
	private static final Logger logger = LoggerFactory
			.getLogger(MainController.class);	
	
	@RequestMapping(value = "/encode", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<ShortURL> createURL(@RequestParam(value = "alias", required = false) String alias, @RequestParam(value = "originalurl", required = true) String url){
		Long init = System.currentTimeMillis();
		
		ShortenerURLService sus = new ShortenerURLService(alias, url);
		
		ShortURL su = new ShortURL();
		try {
			su = sus.encode();
			
		} catch (InternalErrorException e) {
			logger.error(e.getMessage());
			su.setAlias(alias);
			su.setUrl(url);
			su.setErrorCode(InternalErrorException.getCode());
			su.setDescription(e.getMessage());
			Long finish = System.currentTimeMillis();
			Statistics statistics = new Statistics(init, finish);
			su.setStatistics(statistics);
			return new ResponseEntity<ShortURL>(su, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (CustomAliasAlreadyExistsException e) {
			logger.error(e.getMessage());
			su.setAlias(alias);
			su.setUrl(url);
			su.setErrorCode(CustomAliasAlreadyExistsException.getCode());
			su.setDescription(e.getMessage());
			Long finish = System.currentTimeMillis();
			Statistics statistics = new Statistics(init, finish);
			su.setStatistics(statistics);
			return new ResponseEntity<ShortURL>(su, HttpStatus.CONFLICT);
		} catch (UnableToCreateException e) {
			logger.error(e.getMessage());
			su.setAlias(alias);
			su.setUrl(url);
			su.setErrorCode(UnableToCreateException.getCode());
			su.setDescription(e.getMessage());
			Long finish = System.currentTimeMillis();
			Statistics statistics = new Statistics(init, finish);
			su.setStatistics(statistics);
			return new ResponseEntity<ShortURL>(su, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Long finish = System.currentTimeMillis();
		Statistics statistics = new Statistics(init, finish);
		su.setStatistics(statistics);
		
		return new ResponseEntity<ShortURL>(su, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/top10", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<Top10>> getTop10(){
		
		List<Top10> result = new ArrayList<Top10>();
		ShortenerURLService sus = new ShortenerURLService();
		try {
			result = sus.getTop10();
			
		} catch (InternalErrorException e) {
			logger.error(e.getMessage());			
			return new ResponseEntity<List<Top10>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
		return new ResponseEntity<List<Top10>>(result, HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/decode", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<ShortURL> getOriginalURL(@RequestParam(value = "shorturl", required = true) String shortUrl){
		
		Long init = System.currentTimeMillis();
        ResponseEntity<ShortURL> result = null;
        ShortenerURLService sus = new ShortenerURLService();
        		
		ShortURL su = new ShortURL();
		try {
        	
        	su = sus.decode(shortUrl);
        	
		} catch (InternalErrorException e) {
			logger.error(e.getMessage());
			su.setAlias(shortUrl);
			su.setErrorCode(InternalErrorException.getCode());
			su.setDescription(e.getMessage());
			Long finish = System.currentTimeMillis();
			Statistics statistics = new Statistics(init, finish);
			su.setStatistics(statistics);
			return new ResponseEntity<ShortURL>(su, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InvalidURLException e) {
			logger.error(e.getMessage());
			su.setAlias(shortUrl);
			su.setErrorCode(InvalidURLException.getCode());
			su.setDescription(e.getMessage());
			Long finish = System.currentTimeMillis();
			Statistics statistics = new Statistics(init, finish);
			su.setStatistics(statistics);
			return new ResponseEntity<ShortURL>(su, HttpStatus.NOT_ACCEPTABLE);
		} catch (ShortURLNotFoundException e) {
			logger.error(e.getMessage());
			su.setAlias(shortUrl);
			su.setErrorCode(ShortURLNotFoundException.getCode());
			su.setDescription(e.getMessage());
			Long finish = System.currentTimeMillis();
			Statistics statistics = new Statistics(init, finish);
			su.setStatistics(statistics);
			return new ResponseEntity<ShortURL>(su, HttpStatus.NOT_FOUND);
		}
        
        Long finish = System.currentTimeMillis();
		su.setStatistics(new Statistics(init, finish));        	
        
		result = new ResponseEntity<ShortURL>(su, HttpStatus.OK);        
        
        return result;

	}	

}
