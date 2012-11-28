package es.isoco.rest.structure;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType (propOrder={"process"})
public class Search {
	
	//private List<String> uri;
	private ProcessInfo process;

	
	public ProcessInfo getProcess() {
		return process;
	}

	public void setProcess(ProcessInfo process) {
		this.process = process;
	}

	
	public void fillList(ArrayList<String> urisList, double freq, String proc){
		ProcessInfo p= new ProcessInfo();
		p.fillProcessInfo(freq, proc);
		p.setUri(urisList);
		this.setProcess(p);
	}

}
