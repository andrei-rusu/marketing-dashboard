package application.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ServerLogWrapper{

	ArrayList<ServerRecord> serverRecArray;

	public ServerLogWrapper(ArrayList<ServerRecord> ser){
		this.serverRecArray = new ArrayList<ServerRecord>(ser);
	}

	public ArrayList<ServerRecord> getServerRecArray() {
		return serverRecArray;
	}

	public TreeMap<String,Long> getConversionsOverWeeks(){
		Map<String,Long> map = serverRecArray.stream().filter(e -> e.isConversion()).
				collect(Collectors.groupingBy(ServerRecord::getWeek, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

	public TreeMap<String,Long> getConversionsOverDays(){
		Map<String,Long> map = serverRecArray.stream().filter(e -> e.isConversion()).
				collect(Collectors.groupingBy(ServerRecord::getYearMonthDate, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

	public TreeMap<String,Long> getConversionsOverHours(){
		Map<String,Long> map = serverRecArray.stream().filter(e -> e.isConversion()).
				collect(Collectors.groupingBy(ServerRecord::getYearMonthDateHour, Collectors.counting()));

		TreeMap<String,Long> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		return treeMap;
	}

	public Map<String, Integer> getBouncesOverTime(int selected, int bounceIdentifier, int timeGranularity){
		TreeMap<String , Integer> map = new TreeMap<String, Integer>();
		if(selected == 0 ){
			for(ServerRecord record : serverRecArray){
				long bouncePerTime = (record.getExitDate().getTime() - record.getEntryDate().getTime())/1000;

				if(bouncePerTime > 0 && bouncePerTime <= bounceIdentifier){
					if(timeGranularity == 0){
						if(map.containsKey(record.getYearMonthDateHour()))
							map.put(record.getYearMonthDateHour(), (map.get(record.getYearMonthDateHour())) + 1);
						else {
							map.put(record.getYearMonthDateHour(), 1);
						}
					}else if(timeGranularity == 1){
						if(map.containsKey(record.getYearMonthDate()))
							map.put(record.getYearMonthDate(), (map.get(record.getYearMonthDate())) + 1);
						else {
							map.put(record.getYearMonthDate(), 1);
						}
					}else{
						if(map.containsKey(record.getWeek()))
							map.put(record.getWeek(), (map.get(record.getWeek())) + 1);
						else {
							map.put(record.getWeek(), 1);
						}
					}
				}
			}
		}
		else if(selected == 1 ){
			for(ServerRecord record : serverRecArray){
				int bouncePerPages = record.getPagesViewed();

				if(bouncePerPages > 0 && bouncePerPages <= bounceIdentifier){
					if(timeGranularity == 0){
						if(map.containsKey(record.getYearMonthDateHour()))
							map.put(record.getYearMonthDateHour(), (map.get(record.getYearMonthDateHour())) + 1);
						else {
							map.put(record.getYearMonthDateHour(), 1);
						}
					}else if(timeGranularity == 1){
						if(map.containsKey(record.getYearMonthDate()))
							map.put(record.getYearMonthDate(), (map.get(record.getYearMonthDate())) + 1);
						else {
							map.put(record.getYearMonthDate(), 1);
						}
					}else{
						if(map.containsKey(record.getWeek()))
							map.put(record.getWeek(), (map.get(record.getWeek())) + 1);
						else {
							map.put(record.getWeek(), 1);
						}
					}
				}
			}
		}

		return map;
	}

}
