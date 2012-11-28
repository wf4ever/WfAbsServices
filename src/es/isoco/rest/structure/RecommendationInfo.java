package es.isoco.rest.structure;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RecommendationInfo {
	
	private double freq;
	private double prob;
	private String id;
	
	@XmlAttribute
	public double getFreq() {
		return freq;
	}
	public void setFreq(double freq) {
		this.freq = freq;
	}
	
	@XmlAttribute
	public double getProb() {
		return prob;
	}
	public void setProb(double prob) {
		this.prob = prob;
	}
	
	@XmlAttribute
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public void fillProcessInfo(double prob, double freq, String proc) {
		this.setProb(prob);
		this.setFreq(freq);
		this.setId(proc);
	}
	

}
