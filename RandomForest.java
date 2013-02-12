import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A RandomForest is simply a collection of DecisionTrees.
 * These trees are grown in a certain random way, and they vote on decisions.
 */
public class RandomForest<D> {
    
    /**
     * Grows a random forest from a list of samples.
     *
     * @param attrs     The attributes and their possible values.
     * @param samples   The sample data to train on.
     * @param size      The number of trees in the forest.
     * @param n         The number of sample records to choose with replacement
     *                  for each tree.
     * @param m         The best attribute at each node in a tree will be chosen
     *                  from m attributes selected at random without replacement.
     * @return          A new RandomForest.
     */
    public static <D> RandomForest<D> growRandomForest(
            Map<String,List<String>> attrs,
            List<Sample<D>> samples,
            int size,
            int n,
            int m)
    {
        List<DecisionTree<D>> trees = new ArrayList<DecisionTree<D>>(size);
        for (int i = 0; i < size; i++) {
            List<Sample<D>> sampleChoices = ListUtils.choices(samples, n);
            trees.add(DecisionTree.growDecisionTree(attrs, sampleChoices, m));
        }
        return new RandomForest<D>(trees);
    }

    /** The trees in this forest. */
    private List<DecisionTree<D>> trees;

    /**
     * Private constructor, for some reason.
     *
     * @param trees     The trees in this forest.
     */
    private RandomForest(List<DecisionTree<D>> trees) {
        this.trees = trees;
    }
    
    /** 
     * Run a list of samples against this forest.
     *
     * @param samples   The samples to run.
     * @return          The number of correct decisions by this forest.
     */
    public int numberOfCorrectDecisions(List<Sample<D>> samples) {
        int correct = 0;
        for (Sample<D> sample : samples) {
            if (sample.decision.equals(decide(sample))) {
                correct++;
            }
        }
        return correct;
    }

    /** 
     * Gets the mode decision of the trees in this forest on the sample.
     *
     * @param sample    The sample whose attributes will be used to get
     *                  a decision from the forest.
     * @return          The decision of this forest on the sample.
     */
    public D decide(Sample<D> sample) {
        Counter<D> decisions = new Counter<D>();
        for (DecisionTree<D> tree : trees) {
            decisions.add(tree.decide(sample.choices));
        }  
        return decisions.mode();
    }

}
