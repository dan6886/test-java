package time;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TimeRecognition {

	private TimeDao timeDao;

	private TimeNormalizer timeNormalizer;

	public String singleTimeRecognition(String word) {
		initTime();
		TimeUnit[] timeUnits = timeNormalizer.parse(word);

		if (timeUnits != null && timeUnits.length > 0) {
			return timeUnits[0].Time_Norm;
		}
		return "";
	}

	public List<String> multiTimeRecognition(String text) {
		initTime();
		List<String> result = new ArrayList<>();
		TimeUnit[] timeUnits = timeNormalizer.parse(text);
		for (TimeUnit timeUnit : timeUnits) {
			result.add(timeUnit.Time_Norm);
		}
		return result;
	}

	public Map<String, String> timeRecognition(String text) {
		initTime();
		Map<String, String> timeMap = new HashMap<>();
		TimeUnit[] timeUnits = timeNormalizer.parse(text);
		if (timeUnits != null) {
			for (TimeUnit timeUnit : timeUnits) {
				timeMap.put(timeUnit.Time_Expression, timeUnit.Time_Norm);
			}
		}
		return timeMap;
	}

	public Map<String, String> checkClock(String word) {
		initTime();
		return timeNormalizer.checkClock(word);
	}

	public String numberTranslator(String target) {
		initTime();
		return TimeNormalizer.numberTranslator(target);
	}

	public String timeShift(String target, String baseTime) {
		initTime();
		TimeUnit[] timeUnits = timeNormalizer.timeShift(target, baseTime);
		if (timeUnits != null) {
			for (TimeUnit timeUnit : timeUnits) {
				return timeUnit.Time_Norm;
			}
		}
		return null;
	}

	private void initTime() {
		if (timeNormalizer == null) {
			timeNormalizer = new TimeNormalizer(timeDao.getSolarFestivals(), timeDao.getlunarFestivals(),
					timeDao.getNlpPeriodTimeConfigs(), timeDao.getNlpPeriodClockConfigs());
		}
	}

	public TimeNormalizer getTimeNormalizer() {
		return timeNormalizer;
	}

}
