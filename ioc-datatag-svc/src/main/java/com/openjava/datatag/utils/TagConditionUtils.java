package com.openjava.datatag.utils;

import java.util.Arrays;
import java.util.List;

public class TagConditionUtils {
    private static String[] connectSymbol ={"AND","OR","(",")"};
    private static String[] operatorSymbol = {">","=","<","≤","≥","≠","LIKE"};
    private static String[] intTypes = {"TINYINT","SMALLINT","INT","BIGINT","NUMBER","FLOAT","DOUBLE","tinyint","smallint","int","bigint","number","float","double"};
    private static String[] stringTypes={"STRING","VARCHAR","CHAR","VARCHAR2","CLOB","string","varchar","char","varchar2","clob"};

    /**
     * 是否连接符
     * @param symbol
     * @return
     */
    public static boolean isConnectSymbol(String symbol){
        List<String> list = Arrays.asList(connectSymbol);
        if (list.contains(symbol)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 是否运算符
     * @param symbol
     * @return
     */
    public static boolean isOperatorSymbol(String symbol){
        List<String> list = Arrays.asList(operatorSymbol);
        if (list.contains(symbol)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 是否数字类型
     * @param symbol
     * @return
     */
    public static boolean isIntType(String symbol){
        List<String> list = Arrays.asList(intTypes);
        if (list.contains(symbol)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 字符串类型
     * @param symbol
     * @return
     */
    public static boolean isStringType(String symbol){
        List<String> list = Arrays.asList(stringTypes);
        if (list.contains(symbol)){
            return true;
        }else {
            return false;
        }
    }

    public static String toSqlSymbol(String symbol){
        switch (symbol) {
            case "≠":
                return "!=";
            case "≥":
                return ">=";
            case "≤":
                return "<=";
            default:
                return symbol;
        }
    }
    public static String initValues(String values,String valueType,String symbool){
        List<String> list = Arrays.asList(stringTypes);
        if (list.contains(valueType)) {
            if ("LIKE".equals(symbool)) {
                values = "%"+values+"%";
            }
            values = "'"+values+"'";
        }
       return null;
    }

    public static void main(String[] args) {
        System.out.println(TagConditionUtils.isConnectSymbol("AND"));
        System.out.println(TagConditionUtils.toSqlSymbol("≠"));
    }
}
