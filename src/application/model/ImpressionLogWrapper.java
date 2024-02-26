package application.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


public class ImpressionLogWrapper{

	ArrayList<ImpressionRecord> impressions;

	public ImpressionLogWrapper(ArrayList<ImpressionRecord> impr){
		impressions = new ArrayList<ImpressionRecord>(impr);
	}

	public HashSet<Double> getUniquePeople(){

		HashSet<Double> uniques = new HashSet<>();

		for(ImpressionRecord record : impressions){
			uniques.add(record.getId());
		}

		return uniques;
	}

	public ArrayList<ImpressionRecord> getImpressions() {
		return impressions;
	}

	public int getImpressionCostDistribution(float begin, float end) {

		List<ImpressionRecord> result = impressions.stream()
			.filter(rec -> (rec.impressionCost>=begin && rec.impressionCost<end))
			.collect(Collectors.toList());

		return result.size();
	}

	public int filterContext(int contextGroup) {

		List<ImpressionRecord> result = impressions.stream()
				.filter(rec -> rec.context == contextGroup)
				.collect(Collectors.toList());

		return result.size();
	}


	public TreeMap<String,Long> getImpressionsOverWeeks(){
		Map<String,Long> map = impressions.stream().collect(Collectors.groupingBy(
				ImpressionRecord::getWeek, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

	public TreeMap<String,Long> getImpressionsOverDays(){
		Map<String,Long> map = impressions.stream().collect(Collectors.groupingBy(
				ImpressionRecord::getYearMonthDate, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

	public TreeMap<String,Long> getImpressionsOverHours(){
		Map<String,Long> map = impressions.stream().collect(Collectors.groupingBy(
				ImpressionRecord::getYearMonthDateHour, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

}
