package it.uniroma1.lcl.supWSD.modules;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import it.uniroma1.lcl.supWSD.data.Lexel;
import it.uniroma1.lcl.supWSD.data.Token;
import it.uniroma1.lcl.supWSD.inventory.SenseInventory;
import it.uniroma1.lcl.supWSD.mns.MNS;
import it.uniroma1.lcl.supWSD.modules.classification.Evaluator;
import it.uniroma1.lcl.supWSD.modules.classification.Serializer;
import it.uniroma1.lcl.supWSD.modules.classification.classifiers.Classifier;
import it.uniroma1.lcl.supWSD.modules.classification.instances.AmbiguityTest;
import it.uniroma1.lcl.supWSD.modules.classification.scorer.Score;
import it.uniroma1.lcl.supWSD.modules.extraction.extractors.FeatureExtractor;
import it.uniroma1.lcl.supWSD.modules.parser.Parser;
import it.uniroma1.lcl.supWSD.modules.preprocessing.Preprocessor;
import it.uniroma1.lcl.supWSD.modules.writer.Writer;
import net.didion.jwnl.JWNLException;
import opennlp.tools.util.InvalidFormatException;

/**
 * @author Simone Papandrea
 *
 */
public class Tester extends Analyzer<AmbiguityTest> {

	private final Evaluator mEvaluator;
	private final Writer mWriter;
	private final MNS mMNS;

	public Tester(Parser parser, MNS mns, Preprocessor preprocessor, FeatureExtractor[] featureExtractors,
			Classifier<?, ?> sc, Writer writer, Map<String, SortedSet<String>> senses, SenseInventory lr)
			throws InvalidFormatException, IOException, ParserConfigurationException, SAXException, JWNLException {

		super(parser, preprocessor, featureExtractors, senses);

		this.mEvaluator = new Evaluator(sc, lr, this.getSensesCount());
		this.mWriter = writer;
		this.mMNS = mns;
	}

	@Override
	protected void classify(Collection<AmbiguityTest> ambiguities) {

		for (AmbiguityTest ambiguity : ambiguities)
			 mEvaluator.evaluate(ambiguity);
	}

	@Override
	protected AmbiguityTest getAmbiguity(String lexel) {

		AmbiguityTest ambiguityTest = null;

		try {

			ambiguityTest = Serializer.readStatistic(lexel);

		} catch (IOException e) {

			ambiguityTest = new AmbiguityTest(lexel, null);
		}

		return ambiguityTest;
	}

	@Override
	public void init() throws Exception {

		Writer.clear();
		
		super.init();

		if (mMNS != null)
			mMNS.load();
	}

	@Override
	public void finalyze() {

		Score score;

		super.finalyze();

		if (mMNS != null)
			mMNS.unload();
		
		mEvaluator.closeResource();
		score = mEvaluator.getScore();
		score.evaluate();
		printResults(score);

		try {
			mWriter.write(score.getResults());
		} catch (IOException e) {
		}

	}

	private void printResults(Score score) {

		System.out.printf("precision : %.3f ( %d correct of %d attempted )\n", score.precision(), score.getCorrected(),
				score.getAttempted());
		System.out.printf("recall : %.3f ( %d correct of %d in total )\n", score.recall(), score.getCorrected(),
				score.getTotal());
		System.out.printf("f : %.3f\n", score.f());
		System.out.printf("attempted : %.3f ( %d attempted of %d in total )\n", score.getAttemptedPerc(),
				score.getAttempted(), score.getTotal());
	}

	@Override
	protected List<String> getModelName(Lexel lexel, Token token) {

		List<String> names;

		if (mMNS != null)
			names = mMNS.resolve(lexel, token.getPOS());
		else
			names = super.getModelName(lexel, token);

		return names;
	}

}
