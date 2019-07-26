package com.openjava.datatag.utils;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;


public class VoUtils {

	/**
	 * 把对象转换字符串，如果对象是null，那么返回null
	 * @param val
	 * @return
	 */
	public static String toString(final Object val) {
		if(val != null) {
			return val.toString();
		}
		return null;
	}
	
	/**
	 * 把对象转换字符串，如果对象是null，那么返回空字符
	 * @param val
	 * @return
	 */
	public static String toStringOrEmpty(final Object val) {
		if(val != null) {
			return val.toString();
		}
		return "";
	}
	
	public static Integer toInteger(final Object val) {
		if(val != null) {
			return getInteger(val);
		}
		return null;
	}
	public static Integer toIntegerOrZero(final Object val) {
		if(val != null) {
			Integer res = getInteger(val);
			if(res == null) {
				res = 0;
			}
			return res;
		}
		return 0;
	}
	
	public static Long toLong(final Object val) {
		if(val != null) {
			return getLong(val);
		}
		return null;
	}
	public static Long toLongOrZero(final Object val) {
		if(val != null) {
			Long res = getLong(val);
			if(res == null) {
				res = 0L;
			}
			return res;
		}
		return 0L;
	}
	
	public static Float toFloat(final Object val) {
		if(val != null) {
			return getFloat(val);
		}
		return null;
	}
	public static Float toFloatOrZero(final Object val) {
		if(val != null) {
			return getFloat(val);
		}
		return 0.0f;
	}
	
	public static Double toDouble(final Object val) {
		if(val != null) {
			return getDouble(val);
		}
		return null;
	}
	public static Double toDouble(final Object val, Double def) {
		if(val != null) {
			return getDouble(val);
		}
		return def;
	}
	public static Double toDoubleOrZero(final Object val) {
		if(val != null) {
			return getDouble(val);
		}
		return 0.0d;
	}
	
	public static Boolean toBooleanOrFalse(final Object val) {
		if(val != null) {
			if(val instanceof Boolean) {
				return (Boolean)val;
			} else if(val instanceof String) {
				return Boolean.parseBoolean((String)val);
			} else if(val instanceof Integer
				|| val instanceof Long 
				|| val instanceof Float || val instanceof Double) {
				Number n = (Number)val;
				if(n.intValue() > 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	private static Integer getInteger(final Object val) {
		if(val instanceof Integer) {
			return (Integer)val;
		} else if(val instanceof Long
				|| val instanceof Float 
				|| val instanceof Double) {
			Number n = (Number)val;
			return n.intValue();
		} else if(val instanceof BigDecimal) {
			BigDecimal dec = (BigDecimal)val;
			return dec.intValue();
		} else {
			String str = val.toString();
			if(StringUtils.isNumeric(str)) {
				return new Integer(str);
			}
		}
		return null;
	}
	private static Long getLong(final Object val) {
		if(val instanceof Long) {
			return (Long)val;
		} else if(val instanceof Integer
				|| val instanceof Float 
				|| val instanceof Double) {
			Number n = (Number)val;
			return n.longValue();
		} else if(val instanceof BigDecimal) {
			BigDecimal dec = (BigDecimal)val;
			return dec.longValue();
		} else {
			String str = val.toString();
			if(StringUtils.isNumeric(str)) {
				return new Long(str);
			}
		}
		return null;
	}
	private static Float getFloat(final Object val) {
		if(val instanceof Float) {
			return (Float)val;
		} else if(val instanceof Integer
				|| val instanceof Long 
				|| val instanceof Double) {
			Number n = (Number)val;
			return n.floatValue();
		} else if(val instanceof BigDecimal) {
			BigDecimal dec = (BigDecimal)val;
			return dec.floatValue();
		} else {
			return new Float(val.toString());
		}
	}
	private static Double getDouble(final Object val) {
		if(val instanceof Double) {
			return (Double)val;
		} else if(val instanceof Integer
				|| val instanceof Long 
				|| val instanceof Float) {
			Number n = (Number)val;
			return n.doubleValue();
		} else if(val instanceof BigDecimal) {
			BigDecimal dec = (BigDecimal)val;
			return dec.doubleValue();
		} else {
			return new Double(val.toString());
		}
	}
	
}
