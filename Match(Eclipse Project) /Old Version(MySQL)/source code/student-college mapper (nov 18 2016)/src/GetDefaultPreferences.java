
public class GetDefaultPreferences {
	
	final double allFactors[]={0.25 , 0.21 , 0.15 , 0.12 , 0.09 ,  0.05 , 0.035 , 0.06  , 0.025 , 0.01 };
	final int selectedFactors[]={1,2,3,4,5,6,7,8};
	double weights[];
	final int optionalOffset=4;
	
	GetDefaultPreferences(){
		double sum=0;
		weights=new double[selectedFactors.length];
		for(int i=0;i<selectedFactors.length;i++){
			weights[i]=allFactors[selectedFactors[i]];
			sum+=weights[i];
		}
		for(int i=0;i<weights.length;i++)
			weights[i]/=sum;
		
		System.out.println("Inside GetDefaultPreferences\n");
		for(int i=0;i<weights.length;i++)
			System.out.println("weights["+i+"]="+weights[i]);
//		System.exit(0);
	}
	
	double[] getSelectedFactorsWeights(){
		return weights;
	}
	
	int numberOfFactorsSelected(){
		return weights.length;
	}
	
	public int offset() {
		return optionalOffset;
	}
}
