package Test_Prepared_Statement;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {
	public static void test1(Object object,String filedName,Object Value){
		Class<?> clazz = object.getClass();
		Field f1 = null;
		try{
			Object A = clazz.newInstance();
			f1 = clazz.getDeclaredField(filedName);
		    f1.setAccessible(true);
			f1.set(object, Value);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 使 filed 变为可访问
	 * @param field
	 */
	public static void makeAccessible(Field field){
		if(!Modifier.isPublic(field.getModifiers())){
			field.setAccessible(true);
		}
	}
	/**
	 * 循环向上转型, 获取对象的 DeclaredField
	 * @param object
	 * @param filedName
	 * @return
	 */
	public static Field getDeclaredField(Object object, String filedName){
		
		for(Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()){
			try {
				return superClass.getDeclaredField(filedName);
			} catch (NoSuchFieldException e) {
				//Field 不在当前类定义, 继续向上转型
			}
		}
		return null;
	}
	
	/**
	 * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter
	 * @param object
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(Object object, String fieldName, Object value){
		Field field = getDeclaredField(object, fieldName);
		
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		
		makeAccessible(field);
		
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			System.out.println("不可能抛出的异常");
		}
	}
	
	/**
	 * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object object, String fieldName){
		Field field = getDeclaredField(object, fieldName);
		
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		
		makeAccessible(field);
		
		Object result = null;
		
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			System.out.println("不可能抛出的异常");
		}
		
		return result;
	}
}
