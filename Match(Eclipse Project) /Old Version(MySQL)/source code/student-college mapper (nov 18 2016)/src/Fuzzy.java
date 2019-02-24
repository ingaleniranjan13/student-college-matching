
class Factor {
	int factor;
	double weight;
	public Factor(int factor, double weight) {
		this.factor=factor;
		this.weight=weight;
	}
}

public class Fuzzy {

	static Factor factors[];
	static Factor defaultWeights[];
	static double differences[];
	static double limit;
	double flexibility;
	
	Fuzzy(){
		
	}
	void fuzzyComputation(int permutation[],double customWeights[]){
		
		flexibility=1;		
		
		limit=1;

		factors=new Factor[customWeights.length];
		for(int i=0;i<customWeights.length;i++)
			factors[i]=new Factor(i+1,customWeights[i]);
		
		defaultWeights=new Factor[factors.length];
		for(int i=0;i<factors.length;i++)
			defaultWeights[i]=new Factor(factors[i].factor,factors[i].weight);
		
		differences=new double[factors.length*(factors.length-1)/2];
		int iterator=0;
		for(int i=0;i<factors.length;i++){
			for(int j=i+1;j<factors.length;j++){
				differences[iterator++]=((factors[i].weight - factors[j].weight)/(factors[i].weight + factors[j].weight));
			}
		}
		
		for(int i=0;i<factors.length;i++){
			
			double k=0;
			for(int j=i*factors.length-(i*(i+1)/2);j<i*factors.length-(i*(i+1)/2)+(factors.length-1-i);j++){
				k+=differences[0]/differences[j];

				if(((factors[i].weight)/(flexibility*k)) < limit)
					limit=((factors[i].weight)/(flexibility*k));

			}
		}
		int i=0;
		int j=0;
		for(i=0;i<permutation.length;i++){
			for(j=i;j<factors.length;j++){
				if(factors[j].factor==permutation[i])
					break;
			}
			int destinationIndex=i;
			int sourseIndex=j;
			while(sourseIndex!=destinationIndex){
				swap(sourseIndex-1,sourseIndex);
				sourseIndex--;
			}
		}
		
		for(i=0;i<factors.length;i++)
			customWeights[factors[i].factor-1]=factors[i].weight;
	}
	
	void swap(int i,int j){
		double difference;
		double delta;
		int index;
		int offset;
		if(factors[i].factor<factors[j].factor){
			index=factors[i].factor;
			offset=factors[j].factor;
		}

		else{
			index=factors[j].factor;
			offset=factors[i].factor;
		}
		difference=differences[ (index-1)*factors.length - (index-1)*(index)/2 + (offset-index-1)];
		delta=(differences[0]/difference)*limit;
		
		if(i<j){
			factors[i].weight=factors[i].weight-delta;
			factors[j].weight=factors[j].weight+delta;
		}
		else{
			factors[i].weight=factors[i].weight+delta;
			factors[j].weight=factors[j].weight-delta;
		}
		Factor tmp; 
		tmp=factors[i];
		factors[i]=factors[j];
		factors[j]=tmp;
		
		Factor tmp1;
		tmp1=defaultWeights[i];
		defaultWeights[i]=defaultWeights[j];
		defaultWeights[j]=tmp1;
	}
}