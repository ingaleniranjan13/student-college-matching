
public class GetDefaultPreferences {
	
	final double allFactors[]={0.25 , 0.21 , 0.15 , 0.12 , 0.09 , 0.06 , 0.05 , 0.035 , 0.025 , 0.01 };
	final int selectedFactors[]={1,2,3,4,5,6};
	double weights[];
	final int optionalOffset=3;
	
	GetDefaultPreferences(){
		double sum=0;
		weights=new double[selectedFactors.length];
		for(int i=0;i<selectedFactors.length;i++){
			weights[i]=allFactors[selectedFactors[i]];
			sum+=weights[i];
		}
		for(int i=0;i<weights.length;i++)
			weights[i]/=sum;
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
