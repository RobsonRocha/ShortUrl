package br.com.shorturl.pojo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "initTime", "finishTime", "timeTaken" })
public class Statistics {
	
	private Long initTime;
	private Long finishTime;
	private Long timeTaken;
	
	public Statistics(Long initTime, Long finishTime){
		this.initTime = initTime;
		this.finishTime = finishTime;
		
		this.timeTaken = finishTime - initTime;
		
	}

	public Long getInitTime() {
		return initTime;
	}

	public void setInitTime(Long initTime) {
		this.initTime = initTime;
	}

	public Long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Long finishTime) {
		this.finishTime = finishTime;
	}

	public Long getTimeTaken() {
		return timeTaken;
	}
	
	
}
