package br.com.shoturl.pojo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "quantity", "shortUrl" })
public class Top10 {
	
	private int quantity;
	private ShortURL shortUrl;
	
	public Top10(int quantity, ShortURL shortUrl){
		this.quantity = quantity;
		this.shortUrl = shortUrl;
	}
	
	public int getQuantity() {
		return quantity;
	}


	public ShortURL getShortUrl() {
		return shortUrl;
	}
	
	
	
}
