package utilities;

import java.util.Arrays;

public class Statistics {
	Object[] data;
    int size;   

    public Statistics(Object[] data) {
        this.data = data;
        size = data.length;
    }   

    public double getMean() {
        if (size == 0) return 0;
    	
        double sum = 0.0;
        for(Object a : data) {
        	if (a == null) return 0;
            sum += (long) a;
        }
        
        return sum/size;
    }

    public double getVariance() {
    	if (size==0)
    		return 0;
    	
        double mean = getMean();
        double temp = 0;
        for(Object a :data) {
        	if (a == null) return 0;
            temp += ((long)a-mean)*((long)a-mean);
        }
        
        if (size-1 !=0)
        	return temp/(size-1);
        else
        	return 0;
    }

    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double median() {
    	if (size == 0)
    		return 0;
       Arrays.sort(data);

       if (data.length % 2 == 0) {
          return ((long) data[(data.length / 2) - 1] + (long) data[data.length / 2]) / 2.0;
       } 
       return (long) data[data.length / 2];
    }
}
