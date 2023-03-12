package telran.monitoring;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import telran.monitoring.model.PulseProbe;

public class PulseProbesImitatorImpl implements PulseProbesImitator {
	@Value("${app.patients.amount}")
	private Long numberAmount;
	@Value("${app.jump.multiplier}")
	private double numJump;
	@Value("${app.no-jump.multiplier}")
	private double numNoJump;
	@Value("${app.increase.probability}")
	private double numProbability;
	@Value("${app.pulse.value.min}")
	private int minValue;
	@Value("${app.pulse.value.max}")
	private int maxValue;
	private HashMap<Long, Integer> idAndValue;
	private static long seqNumber = 1;

	@Override
	public PulseProbe nextProbe() {
		long varId = getRandomId(1, numberAmount);
		Integer res = idAndValue.get(varId);
		int newValue = getValue();
		if (res != null) {
			if (newValue < numProbability) {
				double delta = res * numJump;
				newValue = (int) (newValue < numProbability ? res + delta : res - delta);
			} else {
				double delta = res * numNoJump;
				newValue = (int) (newValue < numProbability ? res + delta : res - delta);
			}
		}

		res = idAndValue.putIfAbsent(varId, newValue);
		if (res != null) {
			seqNumber++;
		}

		return new PulseProbe(varId, getTimestamp(), seqNumber, newValue);
	}

	private int getValue() {
		return (int) randomNumber(minValue, maxValue);
	}

	private long getTimestamp() {
		return System.currentTimeMillis();
	}

	private long getRandomId(int i, Long numberAmount2) {
		return randomNumber(i, numberAmount2);
	}

	private long randomNumber(long numberMin, long numberMax) {
		return (long) (numberMin + Math.random() * (numberMax - numberMin + 1));
	}
}
