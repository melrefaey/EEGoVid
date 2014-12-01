package com.eegovid.net.recommendation;

import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FullRunningAverage;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.common.RunningAverage;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;


import com.eegovid.net.recommendation.InterestLevelDataModel;
import com.eegovid.net.recommendation.RecommParametersMeasures;
import com.eegovid.net.recommendation.EEGVideoRecommender;

/**
 * @author Mohamed El-Refaey
 *
 */
public class EEGVideoEvaluator {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		/********** Uncomment the line that matches the data set under test **********/

		// YAHOO_MUSIC_DATASET;
		System.out.println("Here");
		DataModel dataModel = InterestLevelDataModel.generateDataModel(1);
	
		/********** Uncomment the line to run the demo you like **********/

	//	runUserBasedRecommender(dataModel);
		runItemBasedRecommender(dataModel);
		/*	runALSSVDRecommender(dataModel);

		runItemBasedDemo(dataModel, "pearson", 0.9, 0.1);
		runItemBasedDemo(dataModel, "likelyhood", 0.9, 1.0);
		runItemBasedDemo(dataModel, "tanimoto", 0.9, 1.0);
		runItemBasedDemo(dataModel, "cityblock", 0.9, 1.0);
		runItemBasedDemo(dataModel, "ecludian", 0.9, 1.0);
		runItemBasedDemo(dataModel, "uncenteredcosine", 0.9, 1.0);

		runItemBasedDemo(dataModel, "pearson", 0.7, 1.0);
		runItemBasedDemo(dataModel, "likelyhood", 0.7, 1.0);
		runItemBasedDemo(dataModel, "tanimoto", 0.7, 1.0);
		runItemBasedDemo(dataModel, "cityblock", 0.7, 1.0);
		runItemBasedDemo(dataModel, "ecludian", 0.7, 1.0);
		runItemBasedDemo(dataModel, "uncenteredcosine", 0.7, 1.0);

		*//********** Uncomment the line to run with the threshold value you like **********//*
		
		runUserRecommender(dataModel, 0, 0.7, 0.7, 1.0, 2, "pearson");
		runUserRecommender(dataModel, 0, 0.7, 0.7, 1.0, 2, "likelyhood");
		runUserRecommender(dataModel, 0, 0.7, 0.7, 1.0, 2, "tanimoto");
		runUserRecommender(dataModel, 0, 0.7, 0.7, 1.0, 2, "cityblock");
		runUserRecommender(dataModel, 0, 0.7, 0.7, 1.0, 2, "ecludian");
		runUserRecommender(dataModel, 0, 0.7, 0.7, 1.0, 2, "uncenteredcosine");

		runUserRecommender(dataModel, 0, 0.8, 0.7, 1.0, 2, "pearson");
		runUserRecommender(dataModel, 0, 0.8, 0.7, 1.0, 2, "likelyhood");
		runUserRecommender(dataModel, 0, 0.8, 0.7, 1.0, 2, "tanimoto");
		runUserRecommender(dataModel, 0, 0.8, 0.7, 1.0, 2, "cityblock");
		runUserRecommender(dataModel, 0, 0.8, 0.7, 1.0, 2, "ecludian");
		runUserRecommender(dataModel, 0, 0.8, 0.7, 1.0, 2, "uncenteredcosine");

		runUserRecommender(dataModel, 0, 0.9, 0.7, 1.0, 2, "pearson");
		runUserRecommender(dataModel, 0, 0.9, 0.7, 1.0, 2, "likelyhood");
		runUserRecommender(dataModel, 0, 0.9, 0.7, 1.0, 2, "tanimoto");
		runUserRecommender(dataModel, 0, 0.9, 0.7, 1.0, 2, "cityblock");
		runUserRecommender(dataModel, 0, 0.9, 0.7, 1.0, 2, "ecludian");
		runUserRecommender(dataModel, 0, 0.9, 0.7, 1.0, 2, "uncenteredcosine");

		*//********** Uncomment the line to run with the Neighborhood size you like **********//*
		runUserRecommender(dataModel, 2, 0.7, 0.7, 1.0, 2, "pearson");
		runUserRecommender(dataModel, 2, 0.7, 0.7, 1.0, 2, "likelyhood");
		runUserRecommender(dataModel, 2, 0.7, 0.7, 1.0, 2, "tanimoto");
		runUserRecommender(dataModel, 2, 0.7, 0.7, 1.0, 2, "cityblock");
		runUserRecommender(dataModel, 2, 0.7, 0.7, 1.0, 2, "ecludian");
		runUserRecommender(dataModel, 2, 0.7, 0.7, 1.0, 2, "uncenteredcosine");

		runUserRecommender(dataModel, 5, 0.7, 0.7, 1.0, 2, "pearson");
		runUserRecommender(dataModel, 5, 0.7, 0.7, 1.0, 2, "likelyhood");
		runUserRecommender(dataModel, 5, 0.7, 0.7, 1.0, 2, "tanimoto");
		runUserRecommender(dataModel, 5, 0.7, 0.7, 1.0, 2, "cityblock");
		runUserRecommender(dataModel, 5, 0.7, 0.7, 1.0, 2, "ecludian");
		runUserRecommender(dataModel, 5, 0.7, 0.7, 1.0, 2, "uncenteredcosine");

		runUserRecommender(dataModel, 10, 0.7, 0.7, 1.0, 2, "pearson");
		runUserRecommender(dataModel, 10, 0.7, 0.7, 1.0, 2, "likelyhood");
		runUserRecommender(dataModel, 10, 0.7, 0.7, 1.0, 2, "tanimoto");
		runUserRecommender(dataModel, 10, 0.7, 0.7, 1.0, 2, "cityblock");
		runUserRecommender(dataModel, 10, 0.7, 0.7, 1.0, 2, "ecludian");
		runUserRecommender(dataModel, 10, 0.7, 0.7, 1.0, 2, "uncenteredcosine");*/

	}

	private static void runALSSVDRecommender(DataModel dataModel)
			throws TasteException {

		System.out.println("Start of Running an ALS SVD Recommendation");
		RecommenderBuilder recommenderBuilder = EEGVideoRecommender.buildSVDRecommender();

		SVDRecommender recommender = (SVDRecommender) recommenderBuilder
				.buildRecommender(dataModel);

		RunningAverage runningAverage = new FullRunningAverage();

		LongPrimitiveIterator userIDs = dataModel.getUserIDs();

		while (userIDs.hasNext()) {
			long userID = userIDs.nextLong();

			for (Preference pref : dataModel.getPreferencesFromUser(userID)) {

				double ratingValue = pref.getValue();
				double preferenceEstimate = recommender.estimatePreference(
						userID, pref.getItemID());

				System.out.println(userID + "," + pref.getItemID() + ","
						+ ratingValue);
				double errorValue = ratingValue - preferenceEstimate;
				runningAverage.addDatum(errorValue * errorValue);
			}
		}

		double rmse = Math.sqrt(runningAverage.getAverage());
		System.out.println(rmse);

		// Recommender Evaluation -- Average Absolute Difference Evaluator
		RecommenderEvaluator absoluteDifferenceEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		double score = absoluteDifferenceEvaluator.evaluate(recommenderBuilder,
				null, dataModel, 0.9, 1.0);
		System.out.println("ALS-based Recommender Average Score is: " + score);

		// Recommender Evaluation -- RMS Evaluator
		RecommenderEvaluator rmsEvaluator = new RMSRecommenderEvaluator();
		double rmsscore = rmsEvaluator.evaluate(recommenderBuilder, null,
				dataModel, 0.9, 1.0);
		System.out.println("ALS-based Recommender RMS Score is:" + rmsscore);

		// Recommender Evaluation -- IRStats Evaluator
		RecommenderIRStatsEvaluator irStatsEvaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = irStatsEvaluator.evaluate(recommenderBuilder,
				null, dataModel, null, 2,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);

		System.out.println("Precision Valus is : " + stats.getPrecision());
		System.out.println("Recall Value is : " + stats.getRecall());

		System.out.println("End of Running an ALS SVD Recommendation");
	}

	private static void runUserBasedRecommender(DataModel dataModel)
			throws TasteException {

		UserSimilarity userSimilarity = RecommParametersMeasures
				.getLogLikelihoodSimilarity(dataModel);

		UserNeighborhood neighborhood = RecommParametersMeasures.getThreshold(
				dataModel, userSimilarity, 0.1);

		RecommenderBuilder recommenderBuilder = EEGVideoRecommender.userBuilder(
				userSimilarity, neighborhood);

		for (LongPrimitiveIterator users = dataModel.getUserIDs(); users
				.hasNext();) {
			long userId = users.nextLong();

			List<RecommendedItem> recommendations = recommenderBuilder
					.buildRecommender(dataModel).recommend(userId, 1);

			for (RecommendedItem recommendation : recommendations) {
				System.out.println(userId + "," + recommendation.getItemID()
						+ "," + recommendation.getValue());
			}

		}

		// Recommender Evaluation -- Average Absolute Difference Evaluator
		RecommenderEvaluator absoluteDifferenceEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		double score = absoluteDifferenceEvaluator.evaluate(recommenderBuilder,
				null, dataModel, 0.9, 1.0);
		System.out.println("User-based Recommender Average Score is: " + score);

		// Recommender Evaluation -- RMS Evaluator
		RecommenderEvaluator rmsEvaluator = new RMSRecommenderEvaluator();
		double rmsscore = rmsEvaluator.evaluate(recommenderBuilder, null,
				dataModel, 0.7, 0.3);
		System.out.println("User-based Recommende RMS Score is:" + rmsscore);

		// Recommender Evaluation -- IRStats Evaluator
		RecommenderIRStatsEvaluator irStatsEvaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = irStatsEvaluator.evaluate(recommenderBuilder,
				null, dataModel, null, 1,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1);
		System.out.println("Precision Valus is : " + stats.getPrecision());
		System.out.println("Recall Value is : " + stats.getRecall());

	}

	private static void runItemBasedRecommender(DataModel dataModel)
			throws TasteException {

		TanimotoCoefficientSimilarity tanimotoSimilarity = new TanimotoCoefficientSimilarity(
				dataModel);

		GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(
				dataModel, tanimotoSimilarity);

		RecommenderBuilder recommenderBuilder = EEGVideoRecommender
				.itemBuilder(tanimotoSimilarity);

		for (LongPrimitiveIterator items = dataModel.getItemIDs(); items
				.hasNext();) {
			long itemId = items.nextLong();
			List<RecommendedItem> recommendations = recommender
					.mostSimilarItems(itemId, 5);

			for (RecommendedItem recommendation : recommendations) {
				System.out.println(itemId + "," + recommendation.getItemID()
						+ "," + recommendation.getValue());
			}

		}

		// Recommender Evaluation -- Average Absolute Difference Evaluator
		RecommenderEvaluator absoluteDifferenceEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		double score = absoluteDifferenceEvaluator.evaluate(recommenderBuilder,
				null, dataModel, 0.7, 0.3);
		System.out.println("Item-based Recommender Average Score is: " + score);

		// Recommender Evaluation -- RMS Evaluator
		RecommenderEvaluator rmsEvaluator = new RMSRecommenderEvaluator();
		double rmsscore = rmsEvaluator.evaluate(recommenderBuilder, null,
				dataModel, 0.7, 0.3);
		System.out.println("Item-based Recommende RMS Score is:" + rmsscore);

		// Recommender Evaluation -- IRStats Evaluator
		RecommenderIRStatsEvaluator irStatsEvaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = irStatsEvaluator.evaluate(recommenderBuilder,
				null, dataModel, null, 1,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1);
		System.out.println("Precision Valus is : " + stats.getPrecision());
		System.out.println("Recall Value is : " + stats.getRecall());

	}

	private static void runItemBasedDemo(DataModel dataModel,
			String similarity, double training, double evaluation)

	throws TasteException {

		System.out.println("Start of Running a Item-based Recommender, with:"
				+ similarity + " Similarity and training % = " + training
				+ " and evaluation % = " + evaluation);

		PearsonCorrelationSimilarity pearson;
		LogLikelihoodSimilarity logLikilySimilarity;
		TanimotoCoefficientSimilarity tanimotoSimilarity;
		CityBlockSimilarity citySimilarity;
		EuclideanDistanceSimilarity eculideanSimilarity;
		UncenteredCosineSimilarity cosineSimilarity;

		RecommenderBuilder recommenderBuilder = null;

		if (similarity.equals("pearson")) {
			System.out.println("Inside Pearson");
			pearson = new PearsonCorrelationSimilarity(dataModel);
			recommenderBuilder = EEGVideoRecommender.itemBuilder(pearson);

		} else if (similarity.equals("likelyhood")) {
			System.out.println("Inside likelyhood");
			logLikilySimilarity = new LogLikelihoodSimilarity(dataModel);
			recommenderBuilder = EEGVideoRecommender
					.itemBuilder(logLikilySimilarity);
		} else if (similarity.equals("tanimoto")) {
			System.out.println("Inside tanimoto");
			tanimotoSimilarity = new TanimotoCoefficientSimilarity(dataModel);
			recommenderBuilder = EEGVideoRecommender
					.itemBuilder(tanimotoSimilarity);
		} else if (similarity.equals("cityblock")) {
			System.out.println("Inside cityblock");
			citySimilarity = new CityBlockSimilarity(dataModel);
			recommenderBuilder = EEGVideoRecommender.itemBuilder(citySimilarity);

		} else if (similarity.equals("ecludian")) {
			System.out.println("Inside ecludian");
			eculideanSimilarity = new EuclideanDistanceSimilarity(dataModel);
			recommenderBuilder = EEGVideoRecommender
					.itemBuilder(eculideanSimilarity);

		} else if (similarity.equals("uncenteredcosine")) {
			System.out.println("Inside uncenteredcosine");
			cosineSimilarity = new UncenteredCosineSimilarity(dataModel);
			recommenderBuilder = EEGVideoRecommender.itemBuilder(cosineSimilarity);
		}

		// Recommender Evaluation -- Average Absolute Difference Evaluator
		RecommenderEvaluator absoluteDifferenceEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		double score = absoluteDifferenceEvaluator.evaluate(recommenderBuilder,
				null, dataModel, training, evaluation);
		System.out.println("Item-based Recommender Average Score is: " + score);

		// Recommender Evaluation -- RMS Evaluator
		RecommenderEvaluator rmsEvaluator = new RMSRecommenderEvaluator();

		double rmsscore = rmsEvaluator.evaluate(recommenderBuilder, null,
				dataModel, training, evaluation);
		System.out.println("The RMS Score is:" + rmsscore);

		// Recommender Evaluation -- IRStats Evaluator
		RecommenderIRStatsEvaluator irStatsEvaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = irStatsEvaluator
				.evaluate(recommenderBuilder, null, dataModel, null, 1,
						GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,
						evaluation);
		System.out.println("The Precision Valus is : " + stats.getPrecision());
		System.out.println("The Recall Value is : " + stats.getRecall());

		System.out.println("End of Running Item-based recommender ");
	}

	/****************************************** Running Demo for User-based Recommender ******************************************/
	private static void runUserRecommender(DataModel dataModel, int n,
			double threshold, double training, double evaluation, int atValue,
			String similarity) throws TasteException {

		System.out.println("Start of Running a User-based Recommender, with:"
				+ similarity + " Similarity and training % = " + training
				+ " and evaluation % = " + evaluation);

		UserSimilarity userSimilarity = null;
		UserNeighborhood neighborhood;

		if (similarity.equals("pearson")) {
			userSimilarity = RecommParametersMeasures
					.getPearsonCorrelation(dataModel);

		} else if (similarity.equals("likelyhood")) {
			userSimilarity = RecommParametersMeasures
					.getLogLikelihoodSimilarity(dataModel);

		} else if (similarity.equals("tanimoto")) {
			userSimilarity = RecommParametersMeasures
					.getTanimotoCoefficientSimilarity(dataModel);
		} else if (similarity.equals("cityblock")) {
			userSimilarity = RecommParametersMeasures
					.getCityBlockSimilarity(dataModel);

		} else if (similarity.equals("ecludian")) {
			userSimilarity = RecommParametersMeasures
					.getEuclideanDistance(dataModel);
		} else if (similarity.equals("uncenteredcosine")) {
			userSimilarity = RecommParametersMeasures.getUncenteredCosine(dataModel);
		}

		if (n > 0) {
			System.out.println("N Size = " + n);
			neighborhood = RecommParametersMeasures.getNearestN(dataModel,
					userSimilarity, n);
		} else {
			System.out.println("Threshold value = " + threshold);
			
			neighborhood = RecommParametersMeasures.getThreshold(dataModel,
					userSimilarity, threshold);
		}

		RecommenderBuilder recommenderBuilder = EEGVideoRecommender.userBuilder(
				userSimilarity, neighborhood);
		recommenderBuilder.buildRecommender(dataModel).recommend(26, 1);

		// Recommender Evaluation -- Average Absolute Difference Evaluator
		RecommenderEvaluator absoluteDifferenceEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		double score = absoluteDifferenceEvaluator.evaluate(recommenderBuilder,
				null, dataModel, training, evaluation);
		System.out.println("The Average Score for this recommender is: "
				+ score);

		// Recommender Evaluation -- RMS Evaluator
		RecommenderEvaluator rmsEvaluator = new RMSRecommenderEvaluator();
		double rmsscore = rmsEvaluator.evaluate(recommenderBuilder, null,
				dataModel, training, evaluation);
		System.out.println("The RMS Score for Pearson and threshold is:"
				+ rmsscore);

		// Recommender Evaluation -- IRStats Evaluator
		RecommenderIRStatsEvaluator irStatsEvaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = irStatsEvaluator
				.evaluate(recommenderBuilder, null, dataModel, null, 1,
						GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,
						evaluation);
		System.out.println("The Precision Valus is : " + stats.getPrecision());
		System.out.println("The Recall Value is : " + stats.getRecall());
		System.out.println("End of Run of user based recommender");
	}


}
