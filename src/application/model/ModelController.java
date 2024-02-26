package application.model;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ModelController {

	Parser parser;
	Calculations calculations;

	public ModelController() {
		parser = new Parser();
		calculations = new Calculations();
	}

	public boolean parseFile(String parseFileIdentifier, File logFile) {
		boolean success = parser.parseLogFile(parseFileIdentifier, logFile);

		return success;
	}

	public void populateMetricsLists() {
		parser.instantiateRecordsWrappers();

	}

	public CalculationsManager getCalculationsManager() {
		return parser.getCalculationsManager();
	}

	public ImpressionLogWrapper getImpressionWrapper() {
		return parser.getImpressionWrapper();
	}

	public ClickLogWrapper getClickLogWrapper() {
		return parser.getClickWrapper();
	}

	public ServerLogWrapper getSeverLogWrapper() {
		return parser.getServerWrapper();
	}

	public PersonWrapper getPersonWrapper() {
		return parser.getPersonWrapper();
	}

	public ImpressionLogWrapper getFilterImpressionWrapper() {
		return parser.getFilterImpressionWrapper();
	}

	public ClickLogWrapper getFilterClickLogWrapper() {
		return parser.getFilterClickWrapper();
	}

	public ServerLogWrapper getFilterSeverLogWrapper() {
		return parser.getFilterServerWrapper();
	}

	public PersonWrapper getFilterPersonWrapper() {
		return parser.getFilterPersonWrapper();
	}

	public void setFilterImpressionWrapper(ImpressionLogWrapper wrapper) {
		parser.setFilterImpressionArray(wrapper.impressions);
	}

	public void setFilterClickLogWrapper(ClickLogWrapper wrapper) {
		parser.setFilterClickArray(wrapper.clicks);
	}

	public void setFilterSeverLogWrapper(ServerLogWrapper wrapper) {
		parser.setFilterServerArray(wrapper.serverRecArray);
	}

	public void setFilterPersonWrapper(PersonWrapper wrapper) {
		parser.setFilterPersonArray(wrapper.people);
	}

	public void populateCalculationsObject() {
		this.parser.getCalculationsManager().modifyCalculations(calculations);

	}

	public void resetCalculations() {
		this.parser.resetCalculationValues();
		this.calculations.resetCalculations();
	}

	public Parser getParser() {
		return parser;
	}

	public Calculations getCalculations() {
		return calculations;
	}

	public Calculations getNewCalculations() {
		return calculations.clone();
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public Map<String, Double> getCTROverWeeks() {

		Map<String, Long> imprMap = getFilterImpressionWrapper().getImpressionsOverWeeks();
		Map<String, Long> clickMap = getFilterClickLogWrapper().getClicksOverWeeks();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : clickMap.keySet()) {
			treeMap.put(key, ((double) clickMap.get(key) / imprMap.get(key)) * 100);
		}

		return treeMap;

	}

	public Map<String, Double> getCTROverDays() {

		Map<String, Long> imprMap = getFilterImpressionWrapper().getImpressionsOverDays();
		Map<String, Long> clickMap = getFilterClickLogWrapper().getClicksOverDays();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : clickMap.keySet()) {
			treeMap.put(key, ((double) clickMap.get(key) / imprMap.get(key)) * 100);
		}

		return treeMap;

	}

	public Map<String, Double> getCTROverHours() {

		Map<String, Long> imprMap = getFilterImpressionWrapper().getImpressionsOverHours();
		Map<String, Long> clickMap = getFilterClickLogWrapper().getClicksOverHours();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : clickMap.keySet()) {
			treeMap.put(key, ((double) clickMap.get(key) / imprMap.get(key)) * 100);
		}

		return treeMap;

	}

	public Map<String, Double> getCPAOverWeeks() {

		Map<String, Double> totalMap = getFilterClickLogWrapper().getTotalCostOverWeeks();
		Map<String, Long> convMap = getFilterSeverLogWrapper().getConversionsOverWeeks();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : totalMap.keySet()) {

			if (convMap.containsKey(key)) {
				treeMap.put(key, ((double) totalMap.get(key) / convMap.get(key)));
			} else {
				treeMap.put(key, totalMap.get(key));
			}
		}

		return treeMap;

	}

	public Map<String, Double> getCPAOverDays() {

		Map<String, Double> totalMap = getFilterClickLogWrapper().getTotalCostOverDays();
		Map<String, Long> convMap = getFilterSeverLogWrapper().getConversionsOverDays();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : totalMap.keySet()) {

			if (convMap.containsKey(key)) {
				treeMap.put(key, ((double) totalMap.get(key) / convMap.get(key)));
			} else {
				treeMap.put(key, totalMap.get(key));
			}
		}

		return treeMap;

	}

	public Map<String, Double> getCPAOverHours() {

		Map<String, Double> totalMap = getFilterClickLogWrapper().getTotalCostOverHours();
		Map<String, Long> convMap = getFilterSeverLogWrapper().getConversionsOverHours();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : totalMap.keySet()) {

			if (convMap.containsKey(key)) {
				treeMap.put(key, ((double) totalMap.get(key) / convMap.get(key)));
			} else {
				treeMap.put(key, totalMap.get(key));
			}
		}

		return treeMap;

	}

	public Map<String, Double> getCPCOverWeeks() {

		Map<String, Double> totalMap = getFilterClickLogWrapper().getTotalCostOverWeeks();
		Map<String, Long> clickMap = getFilterClickLogWrapper().getClicksOverWeeks();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : totalMap.keySet()) {

			if (clickMap.containsKey(key)) {
				treeMap.put(key, ((double) totalMap.get(key) / clickMap.get(key)));
			} else {
				treeMap.put(key, totalMap.get(key));
			}
		}

		return treeMap;

	}

	public Map<String, Double> getCPCOverDays() {

		Map<String, Double> totalMap = getFilterClickLogWrapper().getTotalCostOverDays();
		Map<String, Long> clickMap = getFilterClickLogWrapper().getClicksOverDays();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : totalMap.keySet()) {

			if (clickMap.containsKey(key)) {
				treeMap.put(key, ((double) totalMap.get(key) / clickMap.get(key)));
			} else {
				treeMap.put(key, totalMap.get(key));
			}
		}

		return treeMap;

	}

	public Map<String, Double> getCPCOverHours() {

		Map<String, Double> totalMap = getFilterClickLogWrapper().getTotalCostOverHours();
		Map<String, Long> clickMap = getFilterClickLogWrapper().getClicksOverHours();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : totalMap.keySet()) {

			if (clickMap.containsKey(key)) {
				treeMap.put(key, ((double) totalMap.get(key) / clickMap.get(key)));
			} else {
				treeMap.put(key, totalMap.get(key));
			}
		}

		return treeMap;

	}

	public Map<String, Double> getCPMOverWeeks() {

		Map<String, Double> totalMap = getFilterClickLogWrapper().getTotalCostOverWeeks();
		Map<String, Long> impressionsMap = getFilterImpressionWrapper().getImpressionsOverWeeks();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : totalMap.keySet()) {

			if (impressionsMap.containsKey(key)) {
				treeMap.put(key, (((double) totalMap.get(key) / impressionsMap.get(key)) * 1000));
			} else {
				treeMap.put(key, totalMap.get(key));
			}
		}

		return treeMap;

	}

	public Map<String, Double> getCPMOverDays() {

		Map<String, Double> totalMap = getFilterClickLogWrapper().getTotalCostOverDays();
		Map<String, Long> impressionsMap = getFilterImpressionWrapper().getImpressionsOverDays();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : totalMap.keySet()) {

			if (impressionsMap.containsKey(key)) {
				treeMap.put(key, (((double) totalMap.get(key) / impressionsMap.get(key)) * 1000));
			} else {
				treeMap.put(key, totalMap.get(key));
			}
		}

		return treeMap;

	}

	public Map<String, Double> getCPMOverHours() {

		Map<String, Double> totalMap = getFilterClickLogWrapper().getTotalCostOverHours();
		Map<String, Long> impressionsMap = getFilterImpressionWrapper().getImpressionsOverHours();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : totalMap.keySet()) {

			if (impressionsMap.containsKey(key)) {
				treeMap.put(key, (((double) totalMap.get(key) / impressionsMap.get(key)) * 1000));
			} else {
				treeMap.put(key, totalMap.get(key));
			}
		}

		return treeMap;

	}

	public Map<String, Double> getBounceRateOverTime(int selected, int bounceIdentifier, int timeGranularity) {

		Map<String, Integer> bounceMap = getFilterSeverLogWrapper().getBouncesOverTime(selected, bounceIdentifier,
				timeGranularity);
		Map<String, Long> clickMap = getFilterClickLogWrapper().getClicksOverDays();

		TreeMap<String, Double> treeMap = new TreeMap<>();

		for (String key : bounceMap.keySet()) {

			if (clickMap.containsKey(key)) {
				treeMap.put(key, (((double) bounceMap.get(key) / clickMap.get(key)) * 100));
			} else {
				treeMap.put(key, bounceMap.get(key).doubleValue());
			}
		}

		return treeMap;

	}

	public ArrayList<ServerRecord> filterServerLog(LocalDate startDate, LocalDate endDate,
			ArrayList<ImpressionRecord> newImpressionsArray) {

		Date stDate = java.sql.Date.valueOf(startDate);
		Date eDate = java.sql.Date.valueOf(endDate);

		HashSet<Double> ids = newImpressionsArray.parallelStream().map(i -> i.id)
				.collect(Collectors.toCollection(HashSet::new));

		return getSeverLogWrapper().serverRecArray.parallelStream().filter(x -> ids.contains(x.getId()))
				.filter(s -> (isRecordDateOnSameDayAsFilterDate(s.getEntryDate(), startDate) || s.getEntryDate().after(stDate)))
				.filter(s -> (isRecordDateOnSameDayAsFilterDate(s.getExitDate(), endDate) || s.getExitDate().before(eDate)))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public ArrayList<ImpressionRecord> filterImpressionLog(HashSet<Integer> context, LocalDate startDate,
			LocalDate endDate) {

		Date stDate = java.sql.Date.valueOf(startDate);
		Date eDate = java.sql.Date.valueOf(endDate);


		return getImpressionWrapper().impressions.parallelStream()
				.filter(s -> (isRecordDateOnSameDayAsFilterDate(s.getDate(), startDate) || s.getDate().after(stDate)))
				.filter(s -> (isRecordDateOnSameDayAsFilterDate(s.getDate(), endDate) || s.getDate().before(eDate)))
        		.filter(c -> context.contains(c.getContext()))
        		.collect(Collectors.toCollection(ArrayList::new));
}

	public ArrayList<PersonRecord> filterPersonLog(HashSet<Integer> selectedAudienceSet,
			ArrayList<ImpressionRecord> newImpressionsArray) {

		HashSet<Double> ids = newImpressionsArray.parallelStream().map(i -> i.id)
				.collect(Collectors.toCollection(HashSet::new));

		return getPersonWrapper().people.parallelStream().filter(p -> ids.contains(p.getId()))
				.filter(g -> selectedAudienceSet.contains(g.getGender())
						&& selectedAudienceSet.contains(g.getAgeGroup()) && selectedAudienceSet.contains(g.getIncome()))
				.collect(Collectors.toCollection(ArrayList::new));

	}

	public ArrayList<ImpressionRecord> filterFinalImpressionLog(ArrayList<PersonRecord> filteredPersonLog,
			ArrayList<ImpressionRecord> filteredImpressionLog) {

		HashSet<Double> ids = filteredPersonLog.parallelStream().map(i -> i.id)
				.collect(Collectors.toCollection(HashSet::new));

		return filteredImpressionLog.parallelStream().filter(p -> ids.contains(p.getId()))
				.collect(Collectors.toCollection(ArrayList::new));

	}

	public ArrayList<ClickRecord> filterClickLog(LocalDate startDate, LocalDate endDate,
			ArrayList<ImpressionRecord> filteredImpressionLog) {

		Date stDate = java.sql.Date.valueOf(startDate);
		Date eDate = java.sql.Date.valueOf(endDate);

		HashSet<Double> ids = filteredImpressionLog.parallelStream().map(i -> i.id)
				.collect(Collectors.toCollection(HashSet::new));

		return getClickLogWrapper().clicks.parallelStream().filter(c -> ids.contains(c.getId()))
				.filter(s -> (isRecordDateOnSameDayAsFilterDate(s.getDate(), startDate) || s.getDate().after(stDate)))
				.filter(s -> (isRecordDateOnSameDayAsFilterDate(s.getDate(), endDate) || s.getDate().before(eDate)))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public void resetFilteredArrays() {
		parser.resetFilteredArrays();
	}
	
	protected boolean isRecordDateOnSameDayAsFilterDate(Date date1, LocalDate localDate2) {
		LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		return localDate1.equals(localDate2);
	}

}
