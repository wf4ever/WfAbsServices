package es.isoco.processOrder.main;

import java.util.ArrayList;
import java.util.Scanner;

import es.isoco.processOrder.rdf.VarExtractor;
import es.isoco.processOrder.sparql.Queries;
import es.isoco.processOrder.sparql.RunQuery;
import es.isoco.processOrder.model.Process;

public class CompareProvenance {
	

	private String log;
	
	public CompareProvenance(String wfRun1, String wfRun2){
		log="";
		RunExample r=new RunExample();
		System.out.println(r.runExample(wfRun1));
		ArrayList<Process> trace1=r.getExecutionTrace();
		System.out.println(r.runExample(wfRun2));
		ArrayList<Process> trace2=r.getExecutionTrace();
		compareTraces(trace1, trace2);
		
	}
	
	private boolean compareTraces(ArrayList<Process> trace1, ArrayList<Process> trace2){
		boolean pass=true;
		
		for(int i=0; i<trace1.size(); i++){
			if (i<trace2.size()){
			testTemplate(trace1.get(i).getTemplate(),trace2.get(i).getTemplate());
			testInputs(trace1.get(i), trace2.get(i));
			//testFinalOutputs();
			}
		}
		System.out.println(log);
		return pass;
	}


	private boolean testInputs(Process p1, Process p2) {
		boolean test=true;
		if (p1.getNumInputs()!=p2.getNumInputs())log=log+"Different number of inputs at" +p1.getTemplate()+"\n\n";
		
		for (int i=0; i<p1.getNumInputs(); i++){
			if (i<p2.getNumInputs())
				if(!p1.getInputName(i).equals(p2.getInputName(i))){
					log=log+(p1.getInputName(i) +"\n" +p2.getInputName(i)+"\n\n");
					test=false;
				}
		}
		for (int i=0; i<p1.getNumInputs(); i++)log=log+p1.getInputName(i)+"\n";
		log=log+"\n";
		for (int i=0; i<p2.getNumInputs(); i++)log=log+p2.getInputName(i)+"\n";
		log=log+"\n";
		return test;
	}

	private boolean testTemplate(String template, String template2) {
		boolean test=template.equals(template2);
		if (!test)log=log+(template +"\n" +template2+"\n\n");
		return test;
	}


}
