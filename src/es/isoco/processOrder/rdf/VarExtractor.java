package es.isoco.processOrder.rdf;

public class VarExtractor {
	
	public String SimpleExtract(String line){
		String result=null;
		
		int begin=line.indexOf("<uri>")+5;
		int end=line.lastIndexOf("</uri>");
		result = line.substring(begin, end);
		
		return result;
	}
	
	public boolean checkAppereance(String line, String param){
		return line.contains("<binding name=\""+param+"\">");
	}
	
	public boolean checkSimpleAppereance(String line, String param){
		return line.contains("<"+param+">");
	}
	
	public String extractParam(String line,String param){
		String result=null;
		int tam=param.length()+2;
		int begin=line.indexOf("<"+param+">")+tam;
		int end=line.lastIndexOf("</"+param+">");
		result = line.substring(begin, end);
		return result;
	}
	
	public String literalExtract(String line){
		String result=null;
		
		int begin=line.indexOf("<literal>")+9;
		int end=line.lastIndexOf("</literal>");
		result = line.substring(begin, end);
		
		return result;
	}

}
