package com.eegovid.net.recommendation;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class RecommParametersMeasures {
	public static LogLikelihoodSimilarity getLogLikelihoodSimilarity(DataModel dataModel)
			throws TasteException {
		System.out.println("LogLikelihoodSimilarity");
		return new LogLikelihoodSimilarity(dataModel);
	}

	public static CityBlockSimilarity getCityBlockSimilarity(DataModel dataModel)
			throws TasteException {
		System.out.println("CityBlockSimilarity");
		return new CityBlockSimilarity(dataModel);
	}

	public static TanimotoCoefficientSimilarity getTanimotoCoefficientSimilarity(
			DataModel dataModel) throws TasteException {
		System.out.println("TanimotoCoefficientSimilarity");
		return new TanimotoCoefficientSimilarity(dataModel);
	}

	public static UserSimilarity getPearsonCorrelation(DataModel dataModel)
			throws TasteException {
		System.out.println("PearsonCorrelation");
		return new PearsonCorrelationSimilarity(dataModel);
	}
	
	

	public static UserSimilarity getEuclideanDistance(DataModel dataModel)
			throws TasteException {
		System.out.println("EuclideanDistance");
		return new EuclideanDistanceSimilarity(dataModel);
	}

	public static UserSimilarity getUncenteredCosine(DataModel dataModel)
			throws TasteException {
		System.out.println("UncenteredCosine");
		return new UncenteredCosineSimilarity(dataModel);
	}

	public static UserNeighborhood getNearestN(DataModel dataModel, UserSimilarity userSimilarity,
			int n) throws TasteException {
		System.out.println("NearestNUser");
		return new NearestNUserNeighborhood(n, userSimilarity, dataModel);
	}

	public static UserNeighborhood getThreshold(DataModel dataModel, UserSimilarity userSimilarity,
			double threshold) throws TasteException {
		System.out.println("Threshold");
		return new ThresholdUserNeighborhood(threshold, userSimilarity, dataModel);
	}


}
