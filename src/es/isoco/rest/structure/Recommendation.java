package es.isoco.rest.structure;



import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType (propOrder={"process"})
public class Recommendation {
	
private RecommendationInfo process;
	
	public RecommendationInfo getProcess() {
		return process;
	}

	public void setProcess(RecommendationInfo process) {
		this.process = process;
	}

	
	public void fillList(double prob, double freq, String proc){
		RecommendationInfo p= new RecommendationInfo();
		p.fillProcessInfo(prob,freq, proc);
		this.setProcess(p);
	}

}
