package application.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClickLogWrapper  {

	ArrayList<ClickRecord> clicks;

	public ClickLogWrapper(ArrayList<ClickRecord> cl){
		clicks = new ArrayList<ClickRecord>(cl);
	}

	public ArrayList<ClickRecord> getClicks() {
		return clicks;
	}

	public int getClickCostDistribution(int begin, int end) {

		List<ClickRecord> result = clicks.stream()
			.filter(rec -> (rec.clickCost>=begin && rec.clickCost<end))
			.collect(Collectors.toList());

		return result.size();
	}

	public TreeMap<String,Long> getClicksOverWeeks(){
		Map<String,Long> map = clicks.stream().collect(Collectors.groupingBy(
				ClickRecord::getWeek, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

	public TreeMap<String,Long> getClicksOverDays(){
		Map<String,Long> map = clicks.stream().collect(Collectors.groupingBy(
				ClickRecord::getYearMonthDate, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

	public TreeMap<String,Long> getClicksOverHours(){
		Map<String,Long> map = clicks.stream().collect(Collectors.groupingBy(
				ClickRecord::getYearMonthDateHour, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

	public TreeMap<String,Double> getTotalCostOverWeeks(){
		TreeMap<String, Double> totalCostPerWeek = new TreeMap<String, Double>();

		for(ClickRecord click : clicks){
			if(!totalCostPerWeek.containsKey(click.getWeek())){
				totalCostPerWeek.put(click.getWeek(), click.getClickCost());
			}
			else {
				totalCostPerWeek.put(click.getWeek(),
						(totalCostPerWeek.get(click.getWeek())) + click.getClickCost());
			}
		}

		return totalCostPerWeek;
	}

	public TreeMap<String,Double> getTotalCostOverDays(){
		TreeMap<String, Double> totalCostPerDay = new TreeMap<String, Double>();

		for(ClickRecord click : clicks){
			if(!totalCostPerDay.containsKey(click.getYearMonthDate())){
				totalCostPerDay.put(click.getYearMonthDate(), click.getClickCost());
			}
			else {
				totalCostPerDay.put(click.getYearMonthDate(),
						(totalCostPerDay.get(click.getYearMonthDate())) + click.getClickCost());
			}
		}

		return totalCostPerDay;
	}

	public TreeMap<String,Double> getTotalCostOverHours(){
		TreeMap<String, Double> totalCostPerDay = new TreeMap<String, Double>();

		for(ClickRecord click : clicks){
			if(!totalCostPerDay.containsKey(click.getYearMonthDateHour())){
				totalCostPerDay.put(click.getYearMonthDateHour(), click.getClickCost());
			}
			else {
				totalCostPerDay.put(click.getYearMonthDateHour(),
						(totalCostPerDay.get(click.getYearMonthDateHour())) + click.getClickCost());
			}
		}

		return totalCostPerDay;
	}


	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Map<Object,Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public TreeMap<String,Long> getUniquesOverWeeks(){

		Map<String, Long> map = clicks.stream().filter(distinctByKey(p -> p.getId()))
			    .collect(Collectors.groupingBy(ClickRecord::getWeek, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

	public TreeMap<String,Long> getUniquesOverDays(){

		Map<String, Long> map = clicks.stream().filter(distinctByKey(p -> p.getId()))
			    .collect(Collectors.groupingBy(ClickRecord::getYearMonthDate, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

	public TreeMap<String,Long> getUniquesOverHours(){

		Map<String, Long> map = clicks.stream().filter(distinctByKey(p -> p.getId()))
			    .collect(Collectors.groupingBy(ClickRecord::getYearMonthDateHour, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

}
