package es.isoco.trie.io;

import es.isoco.trie.Trie;

public class GenericMethods {

	
	/*
	 * This method maintains a mapping between the dictionary of processes 
	 * and their associated place in the array. A predefined dictionary is
	 * needed in order to allow the mapping. 
	 * */
	
	public static int transform(String s)
	{
		if (s.equalsIgnoreCase("action1")) return Constants.action1;
		if (s.equalsIgnoreCase("action2")) return Constants.action2;
		if (s.equalsIgnoreCase("action3")) return Constants.action3;
		if (s.equalsIgnoreCase("action4")) return Constants.action4;
		if (s.equalsIgnoreCase("action5")) return Constants.action5;
		if (s.equalsIgnoreCase("action6")) return Constants.action6;
		if (s.equalsIgnoreCase("action7")) return Constants.action7;
		if (s.equalsIgnoreCase("action8")) return Constants.action8;
		return -1;
	}
	
	public static float[] sumLevels(float[][] array)
	{
		float[] auxArr = new float[Constants.numLevels];
		
		for (int i = 0; i < Constants.numLevels; i++)
		{
			auxArr[i] = 0;
		}
		
		for (int i = 0; i < Constants.numLevels; i++)
		{
			for (int j = 0; j < Constants.numActions; j++)
			{
				auxArr[i] = auxArr[i] + Trie.percByActLevel[j][i]; 
			}
		}
		return auxArr;
	}
	
	public static float[] sumFreqLevels()
	{
		float[] auxArr = new float[Constants.numLevels];
		
		for (int i = 0; i < Constants.numLevels; i++)
		{
			auxArr[i] = 0;
		}
		
		for (int i = 0; i < Constants.numLevels; i++)
		{
			for (int j = 0; j < Constants.numActions; j++)
			{
				auxArr[i] = auxArr[i] + Trie.freqByActLevel[j][i]; 
			}
		}
		return auxArr;
	}
}
