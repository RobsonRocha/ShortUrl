package br.com.shorturl.service;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.shorturl.utils.Utils;
import br.com.shoturl.exception.CustomAliasAlreadyExistsException;
import br.com.shoturl.exception.InternalErrorException;
import br.com.shoturl.exception.InvalidURLException;
import br.com.shoturl.exception.ShortURLNotFoundException;
import br.com.shoturl.exception.UnableToCreateException;
import br.com.shoturl.pojo.ShortURL;
import br.com.shoturl.pojo.Top10;

public class ShortenerURLService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(ShortenerURLService.class);
	
	private static final int DUPLICATED_UNIQUE_COLUMN_CODE = 23505;
	
	private String alias;
	private String originalUrl;
	
	
	public ShortenerURLService(String alias, String originalUrl){
		this.alias = alias;
		this.originalUrl = originalUrl;
	}
	
	public ShortenerURLService(){}

	public String getAlias() {
		return alias;
	}	

	public String getOriginalUrl() {
		return originalUrl;
	}

	public List<Top10> getTop10() throws InternalErrorException{
		List<Top10>result = new ArrayList<Top10>();
		
		try {
			result = Utils.getTop10();
		}  catch (SQLException | PropertyVetoException e) {
			logger.error(e.getMessage());
			throw new InternalErrorException(e.getMessage());
		}
		return result;
			
		
	}
	
	public ShortURL encode() throws InternalErrorException, CustomAliasAlreadyExistsException, UnableToCreateException{
		ShortURL su = new ShortURL();
		try {
			String code = "";
			
			if(originalUrl.contains("http://shortener.be/") || originalUrl.startsWith("http://shortener.be/"))
				throw new UnableToCreateException("Unable to short a shorted URL "+originalUrl);
			
			if(alias != null){
				code = alias.toLowerCase().trim();
				Utils.insertURL(code, originalUrl, code);
			}
			else {
				code = Utils.getCode();
				Utils.insertURL(code, originalUrl);		
			}
			
			su.setUrl("http://shortener.be/"+code);
			su.setAlias(code);
				
			logger.debug("Inserted Code="+code+" URL="+originalUrl);
			
			return su;
			
		} catch (SQLException e){
			if(e.getErrorCode() == DUPLICATED_UNIQUE_COLUMN_CODE)
				throw new CustomAliasAlreadyExistsException("CUSTOM ALIAS ALREADY EXISTS");
			throw new InternalErrorException(e.getMessage());			
		} catch(PropertyVetoException e) {			
			throw new InternalErrorException(e.getMessage());
		}
	}
	
	public ShortURL decode(String shortUrl) throws InternalErrorException, InvalidURLException, ShortURLNotFoundException{
		
		try {
			if(!shortUrl.contains("http://shortener.be/") || !shortUrl.startsWith("http://shortener.be/"))
				throw new InvalidURLException("Invalid URL "+shortUrl);
			
			String code = shortUrl.replace("http://shortener.be/", "");
        	ShortURL su = Utils.getURL(code);
        	
        	if(su == null){
        		su = Utils.getURLAlias(code.toLowerCase().trim());
        		if(su == null)
            		throw new ShortURLNotFoundException("Short URL not found "+shortUrl);      		
        			
        	}
        	        	
        	return su;
			
		} catch (SQLException | PropertyVetoException e) {
			logger.error(e.getMessage());
			throw new InternalErrorException(e.getMessage());
		}
	}
	
}
