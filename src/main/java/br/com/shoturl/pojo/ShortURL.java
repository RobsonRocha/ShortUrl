package br.com.shoturl.pojo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "alias", "url", "errorCode", "description", "statistics" })
public class ShortURL {
	
	private String alias;
	private String url;
	private String errorCode;
	private String description;
	private Statistics statistics;
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Statistics getStatistics() {
		return statistics;
	}
	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((alias == null  || url == null) ? 0
						: alias.hashCode() + url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		if(obj instanceof ShortURL){
			if(this.getAlias() == null) 
				return false;
			if(this.getAlias().equals(((ShortURL)obj).getAlias()) && this.getUrl().equals(((ShortURL)obj).getUrl()))
					return true;
		}
		
		return false;
	}
	
}
