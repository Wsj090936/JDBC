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
	 * ʹ filed ��Ϊ�ɷ���
	 * @param field
	 */
	public static void makeAccessible(Field field){
		if(!Modifier.isPublic(field.getModifiers())){
			field.setAccessible(true);
		}
	}
	/**
	 * ѭ������ת��, ��ȡ����� DeclaredField
	 * @param object
	 * @param filedName
	 * @return
	 */
	public static Field getDeclaredField(Object object, String filedName){
		
		for(Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()){
			try {
				return superClass.getDeclaredField(filedName);
			} catch (NoSuchFieldException e) {
				//Field ���ڵ�ǰ�ඨ��, ��������ת��
			}
		}
		return null;
	}
	
	/**
	 * ֱ�����ö�������ֵ, ���� private/protected ���η�, Ҳ������ setter
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
			System.out.println("�������׳����쳣");
		}
	}
	
	/**
	 * ֱ�Ӷ�ȡ���������ֵ, ���� private/protected ���η�, Ҳ������ getter
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
			System.out.println("�������׳����쳣");
		}
		
		return result;
	}
}
