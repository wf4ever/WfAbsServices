package es.isoco.rest.structure;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ProcessInfo {
	
	private double freq;
	private String id;
	private List<String> uri;
	
	
	@XmlAttribute
	public double getFreq() {
		return freq;
	}
	public void setFreq(double freq) {
		this.freq = freq;
	}
	
	@XmlAttribute
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public void fillProcessInfo(double freq, String proc){
		setFreq(freq);
		setId(proc);
	}
	
	public List<String> getUri() {
		return uri;
	}

	public void setUri(List<String> uri) {
		this.uri = uri;
	}

}
