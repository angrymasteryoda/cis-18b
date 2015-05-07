package tk.michael.project.util;

/**
 * Created By: Michael Risher
 * Date: 4/29/15
 * Time: 6:13 PM
 */
public class SyntaxRegex {

	public static String mysqlCommand(){
		return"(\\W)*(?i:add|all|alter|analyze|as|asc|between|by|both|case|change|check|column|constraint|create|cross|databases|day_hour|day_minute|day_second|delayed|delete|desc|describe|sistinct|distinctrow|drop|else|enclosed|escaped|exists|explain|for|foreign|from|fulltext|grant|group|having|high_priority|hour_minute|hour_second|if|ignore|in|index|infile|inner|insert|into|is|join|key|keys|kill|leading|like|limit|load|lock|low_priority|match|minute_second|natural|on|optimize|option|optionally|order|outer|outfile|primary|procedure|purge|read|references|regexp|rename|replace|require|restrict|revoke|rlike|select|set|show|sql_big_result|sql_small_result|starting|staight_join|table|terminated|then|to|trailing|union|unique|unlock|update|usage|use|using|values|when|where|with|write|year_month)";
	}

	public static String mysqlDataType(){
		return "(\\W)*(?i:bigint|blob|char|character|dec|decimal|double|float|float4|float8|int|int1|int2|int3|int4|int8|integer|long|longblob|longtext|mediumblob|mediumint|mediumtext|middleint|numeric|real|smallint|tinyblob|tinyint|tinytext|varbinary|varchar)";
	}

	public static String mysqlWhereSearch(){
		return "(\\W)*(?i:and|not|null|or)";
	}

	public static String mysqlUnknown1(){
		return "(\\W)*(?i:binary|default|precision|unsigned|varying|zerofill)";
	}

	public static String mysqlUnknown2(){
		return "(\\W)*(?i:current_date|current_time|current_timestamp|database|interval|left|mod|repeat|right)";
	}
}
