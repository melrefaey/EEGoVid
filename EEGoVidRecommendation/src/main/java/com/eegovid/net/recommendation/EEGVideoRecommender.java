/**
 * 
 */
package com.eegovid.net.recommendation;

import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * @author Mohamed El-Refaey
 *
 */
public class EEGVideoRecommender {

	/**
	 * 
	 */
	public EEGVideoRecommender() {
		// TODO Auto-generated constructor stub
	}


	public static RecommenderBuilder userBuilder(final UserSimilarity us,
			final UserNeighborhood un) throws TasteException {
		System.out.println("Recommendation is User-based");
		return new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel model)
					throws TasteException {
				return new GenericUserBasedRecommender(model, un, us);
			}
		};
	}

	public static RecommenderBuilder itemBuilder(final ItemSimilarity is)
			throws TasteException {
		System.out.println("Recommendation is Item-based");
		return new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel model)
					throws TasteException {
				return new GenericItemBasedRecommender(model, is);
			}
		};
	}

	public static RecommenderBuilder buildSVDRecommender() throws TasteException {
		System.out.println("Recommendation is ALSWR-based and SVDRecommender");
		return new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel model)
					throws TasteException {
				return new SVDRecommender(model, new ALSWRFactorizer(model, 10,
						0.05, 10));
			}
		};
	}

	// this method should be called from the web client.
	
	public static void displayRecommendation(
			List<RecommendedItem> recommendations, DataModel dataModel)
			throws TasteException {

		for (LongPrimitiveIterator users = dataModel.getUserIDs(); users
				.hasNext();) {
			long userId = users.nextLong();
			// List<RecommendedItem> recommendations =
			// recommender.recommend(userId, 5);

			for (RecommendedItem recommendation : recommendations) {
				System.out.println(userId + "," + recommendation.getItemID()
						+ "," + recommendation.getValue());
			}
		}
	}



}
