package io.github.rhettlangseth.gravalignonline.rating;

public final class RatingCalculator {

    private static final int ASSUMED_RATING_GAME_COUNT = 50;
    private static final int RATING_FLOOR = 100;

    private RatingCalculator() {

    }

    public static int calculateNewRating(boolean result, int rating, int otherRating) {

        double actualScore = result ? 1.0 : 0.0;
        double expectedScore = calculateExpectedScore(rating, otherRating);
        double kFactor = calculateKFactor(rating);
        int ratingChange = (int) Math.round(kFactor * (actualScore - expectedScore));

        return Math.max(RATING_FLOOR, rating + ratingChange);

    }

    private static double calculateExpectedScore(int rating, int otherRating) {

        return 1.0 / (1.0 + Math.pow(10.0, (otherRating - rating) / 400.0));

    }

    private static double calculateKFactor(int rating) {

        double ratingBasedGameCap;

        if (rating <= 2355) {
            ratingBasedGameCap = 50.0 / Math.sqrt(0.662 + 0.00000739 * Math.pow(2569.0 - rating, 2.0));
        } else {
            ratingBasedGameCap = 50.0;
        }

        double effectiveGameCount = Math.min(ASSUMED_RATING_GAME_COUNT, ratingBasedGameCap);

        return 800.0 / (effectiveGameCount + 1.0);

    }

}
