package com.example;   // keep the same package path as your folders

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class RecommenderApp {

    public static void main(String[] args) {
        try {
            // 1. Load ratings
            DataModel model = new FileDataModel(new File("data/ratings_clean.csv"));

            // 2. Ask for a user ID (CLI arg → fallback to console)
            long userID;
            if (args.length > 0) {
                userID = Long.parseLong(args[0]);
            } else {
                // show a few valid IDs to help the user
                System.out.println("Sample user IDs in the dataset:");
                LongPrimitiveIterator it = model.getUserIDs();
                int shown = 0;
                while (it.hasNext() && shown < 10) {
                    System.out.print(it.nextLong() + "  ");
                    shown++;
                }
                System.out.print("\n\nEnter a user ID to get recommendations: ");
                Scanner sc = new Scanner(System.in);
                userID = sc.nextLong();
                sc.close();
            }

            // 3. Build the recommender
            UserSimilarity similarity   = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, model);
            GenericUserBasedRecommender recommender =
                    new GenericUserBasedRecommender(model, neighborhood, similarity);

            // 4. Recommend
            List<RecommendedItem> recs = recommender.recommend(userID, 5);

            System.out.printf("%nTop recommendations for user %d:%n", userID);
            if (recs.isEmpty()) {
                System.out.println("No recommendations found.");
            } else {
                for (RecommendedItem r : recs) {
                    System.out.printf("  itemId=%d  score=%.3f%n", r.getItemID(), r.getValue());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
