package Fuzzy;

import java.util.LinkedList;

public class Fuzzy {
	
	private double limit;
	private double k;
	private int flexibility;
	private double delta;
	private LinkedList<Double> deltas;

	public Fuzzy(int flexibility)
	{
		this.flexibility=flexibility;
		limit=1;
		k=1;
		delta=0;
		deltas=new LinkedList<Double>();
	}
	
	public LinkedList<Factor> fuzzyI(LinkedList<Factor> factors)
	{
	/*	System.out.println("at the begining");
		System.out.println("size="+factors.size());
		for(int i=0;i<factors.size();i++)
		{
			
			System.out.println("factors.get("+i+").getCurrentIndex()="+factors.get(i).getCurrentIndex());
		}
		*/
		
		
		k=Math.abs(factors.get(0).getDefaultWeight() - factors.get(factors.size() - 1).getDefaultWeight()) / 
		Math.abs(factors.get(0).getDefaultWeight() + factors.get(factors.size() - 1).getDefaultWeight());
		for(int i=1;i<factors.size();i++) 
		{
			int j=i-1;
			double c=0.0;
			while(j>=0) 
			{
				c+=Math.abs(factors.get(i).getDefaultWeight()+factors.get(j).getDefaultWeight())/Math.abs(factors.get(i).getDefaultWeight()-factors.get(j).getDefaultWeight());
						j--;
			}
			c*=k;
			double current_limit=(1 - factors.get(i).getDefaultWeight())/c;
			if(limit>current_limit)
				limit=current_limit;
		}
		for(int i=0;i<factors.size()-1;i++)
		{
			int j=i+1;
			double c=0.0;
			while(j<factors.size())
			{
				c+=Math.abs(factors.get(i).getDefaultWeight()+factors.get(j).getDefaultWeight())/Math.abs(factors.get(i).getDefaultWeight()-factors.get(j).getDefaultWeight());
				j++;
			}
			c*=k;
			double current_limit=factors.get(i).getDefaultWeight()/c;
			if(limit>current_limit)
				limit=current_limit;
		}
		delta=limit*flexibility;
		for(int i=0;i<factors.size();i++)
			for(int j=i+1;j<factors.size();j++)
				deltas.add(k*delta*(Math.abs(factors.get(i).getDefaultWeight() + factors.get(j).getDefaultWeight())/Math.abs(factors.get(i).getDefaultWeight() - factors.get(j).getDefaultWeight())));        
		for(int i=0;i<factors.size();i++) 
		{
			int j=i;
		    for(;j<factors.size();j++) {
		    	//System.out.println("factors.get(j).getCurrentIndex()="+factors.get(j).getCurrentIndex());
		    	if(factors.get(j).getCurrentIndex()==i)
		    		break;
		    }
		    //System.out.println("j="+j);
		    int l=j-1;
		    while(l>=i)
		    {
		    	int offset=factors.size()*(factors.size()-1)/2-(factors.size()-(l+1))*(factors.size()-(l+1)-1)/2-(factors.size()-(j+1))-1;
		        double currentWeight=factors.get(j).getCurrentWeight();
		        currentWeight+=deltas.get(offset);
		        factors.get(j).setCurrentWeight(currentWeight);
		        currentWeight=factors.get(l).getCurrentWeight();
		        currentWeight-=deltas.get(offset);
		        factors.get(l).setCurrentWeight(currentWeight);
		        l--;
		    }
		    Factor holder=factors.remove(j);
		    factors.add(0, holder);
		}
		/*
		int i=0;
		int offset=0;
		while(i<factors.size())
		{
		    double UL=factors.get(i).getDefaultWeight();
		    double LL=factors.get(i).getDefaultWeight();
		    int j=factors.get(i).getDefaultIndex();
		    int l=factors.get(i).getDefaultIndex()-1;
		    while(l>=0)
		    {
		    	offset=(factors.size()*(factors.size()-1)/2-(factors.size()-(l+1))*(factors.size()-(l+1)-1)/2-1)-(factors.size()-1-j);
		    	UL+=deltas.get(offset);
		    	l--;
		    }
		    factors.get(i).setUpperLimit(UL);
		    l=factors.get(i).getDefaultIndex()+1;
		    offset=factors.size()*(factors.size()-1)/2-((factors.size()-j)*((factors.size()-j)-1)/2);
		
		    while(l<factors.size())
		    {
		    	System.out.println("offset="+offset);
		    	LL-=deltas.get(offset);
		    	offset--;
		    	l++;
		    }
		    factors.get(i).setLowerLimit(LL);
		    i++;
		}*/
		return factors;		
	}
	/*
	public static void main(String[] args)
	{
		Factor[] factors=new Factor[5];
		factors[0]=new Factor("Placement",0.39,0,0,1);
		factors[1]=new Factor("Quality_of_Education",0.27,0,1,0);
		factors[2]=new Factor("Fees",0.18,0,2,4);
		factors[3]=new Factor("Quality_of_Education",0.09,0,3,2);
		factors[4]=new Factor("Quality_of_Education",0.07,0,4,3);
		Fuzzy fuzzy=new Fuzzy(1);
		LinkedList<Factor> list=new LinkedList<Factor>();
		for(int i=0;i<factors.length;i++)
		{
			list.addLast(factors[i]);
		}
		list=fuzzy.fuzzyI(list);
        for(int i=0;i<list.size();i++)
        {
        	System.out.println();
        	System.out.print("Name="+list.get(i).getName());
        	System.out.print("Current Weight="+list.get(i).getCurrentWeight());
        	System.out.print("Default Weight="+list.get(i).getDefaultWeight());
        }
		
	}*/
	
}

