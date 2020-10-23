package org.hutrace.handy.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hutrace.handy.annotation.Autowire;
import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.DAO;
import org.hutrace.handy.annotation.Service;
import org.hutrace.handy.annotation.WebSocket;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.ScanningApplicationException;
import org.hutrace.handy.language.SystemProperty;
import org.hutrace.handy.utils.scan.ScanningAnnotation;
import org.hutrace.handy.utils.scan.ScanningAnnotationConduct;

import java.util.Set;


/**
 * <p>主动扫描需要注入的类
 * @author hutrace
 * @since 1.8
 * @version 1.0
 * @time 2019年9月29日
 */
public class MappingScanning {
	
	/**
	 * 最终生效的{@link DAO}实列Map
	 */
	private Map<String, Object> daoMap = new HashMap<>();
	
	/**
	 * 最终生效的{@link Service}封装类实列Map
	 */
	private Map<String, TableService> serviceMap = new HashMap<>();
	
	/**
	 * 最终生效的{@link Controller}封装类实列Map
	 */
	private Map<String, TableController> controllerMap = new HashMap<>();
	
	/**
	 * 最终生效的{@link WebSocket}封装类实列Map
	 */
	private Map<String, TableWebSocket> webSocketMap = new HashMap<>();
	
	/**
	 * <p>扫描所有{@link DAO}类
	 * @return 
	 * @throws ScanningApplicationException <br/>
	 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
	 * &emsp;2. 你注入的非{@link CommonDao}时，被注入的类没有添加{@link DAO}注解<br/>
	 */
	Map<String, Object> scanningDao() throws ScanningApplicationException {
		ScanningDao scan = new ScanningDao();
		ScanningAnnotation.build(scan);
		scan.instantiationField();
		return daoMap;
	}
	
	/**
	 * <p>扫描所有{@link Service}类
	 * @return
	 * @throws ScanningApplicationException <br/>
	 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
	 * &emsp;2. 你注入{@link Service}时，被注入的类没有添加{@link Service}注解<br/>
	 */
	Map<String, TableService> scanningService() throws ScanningApplicationException {
		ScanningService scan = new ScanningService();
		ScanningAnnotation.build(scan);
		scan.instantiationField();
		return serviceMap;
	}
	
	/**
	 * <p>扫描所有{@link Controller}类
	 * @return
	 * @throws ScanningApplicationException <br/>
	 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
	 * &emsp;2. 你注入{@link Service}时，被注入的类没有添加{@link Service}注解<br/>
	 */
	Map<String, TableController> scanningController() throws ScanningApplicationException {
		ScanningController scan = new ScanningController();
		ScanningAnnotation.build(scan);
		scan.instantiationField();
		return controllerMap;
	}

	/**
	 * <p>扫描所有{@link WebSocket}类
	 * @return
	 * @throws ScanningApplicationException <br/>
	 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
	 * &emsp;2. 你注入{@link Service}时，被注入的类没有添加{@link Service}注解<br/>
	 */
	Map<String, TableWebSocket> scanningWebSocket() throws ScanningApplicationException {
		ScanningWebSocket scan = new ScanningWebSocket();
		ScanningAnnotation.build(scan);
		scan.instantiationField();
		return webSocketMap;
	}
	
	/**
	 * <p>扫描包含DAO注解的类，并将类实例化后存入{@link #mapDao}
	 * @author hutrace
	 * @see ScanningAnnotationConduct
	 * @since 1.8
	 * @version 1.0
	 * @time 2019年9月29日
	 */
	class ScanningDao implements ScanningAnnotationConduct {
		
		/**
		 * <p>临时缓存，存储首次扫描后的实列以及类信息，便于{@link #instantiationField}解析
		 * @author hutrace
		 * @since 1.8
		 * @version 1.0
		 * @time 2019年9月29日
		 */
		class TempCacheDao {
			Object instance;
			Class<?> clazs;
			TempCacheDao(Class<?> clazs) throws Exception {
				this.clazs = clazs;
				instance = clazs.newInstance();
			}
		}
		
		/**
		 * 创建一个公共DAO
		 */
		private InterfaceDao dao = null;
		
		/**
		 * 临时缓存，用于解析扫描的类
		 */
		private Map<String, TempCacheDao> map = new HashMap<>();
		
		private void initCommonDao() throws ScanningApplicationException {
			Class<?> clazs = null;
			try {
				clazs = Class.forName(Configuration.commonDao());
			}catch (Exception e) {}
			if(clazs != null) {
				try {
					dao = (InterfaceDao) clazs.newInstance();
				}catch (Exception e) {
					throw new ScanningApplicationException(e);
				}
			}
		}
		
		/**
		 * <p>根据临时缓存{@link #map}实例化所有DAO类的注入
		 * <p>判断DAO类中的字段，对添加注解{@link Autowire}的字段，并判断它们是{@link CommonDao}或者{@link DAO}的字段才进行注入
		 * @throws ScanningApplicationException <br/>
		 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
		 * &emsp;2. 你注入的非{@link CommonDao}时，被注入的类没有添加{@link DAO}注解<br/>
		 */
		public void instantiationField() throws ScanningApplicationException {
			initCommonDao();
			Set<Entry<String, TempCacheDao>> set = map.entrySet();
			for(Entry<String, TempCacheDao> entry : set) {
				Field[] fields = entry.getValue().clazs.getDeclaredFields();
				for(Field field : fields) {
					if(field.getAnnotation(Autowire.class) != null) {
						field.setAccessible(true);
						if(field.getType().isAssignableFrom(dao.getClass())) {
							if(dao == null) {
								throw new ScanningApplicationException(SystemProperty.get("serve.mapping.scan.dao.nothave"));
							}
							try {
								field.set(entry.getValue().instance, dao);
							}catch (Exception e) {
								throw new ScanningApplicationException(e);
							}
						}else {
							try {
								field.set(entry.getValue().instance, map.get(field.getType().getName()).instance);
							}catch (NullPointerException e) {
								throw new ScanningApplicationException(SystemProperty.get(
										"serve.mapping.scan.dao.error", field.getType().getName()));
							}catch (IllegalArgumentException e) {
								throw new ScanningApplicationException(e);
							}catch (IllegalAccessException e) {
								throw new ScanningApplicationException(e);
							}
						}
						if(daoMap.get(entry.getKey()) == null) {
							daoMap.put(entry.getKey(), entry.getValue().instance);
						}
					}
				}
			}
			// 释放临时缓存
			map = null;
		}
		
		@Override
		public String[] getPackages() {
			return Configuration.scan();
		}

		@Override
		public Class<? extends Annotation> getTypeAnnotation() {
			return DAO.class;
		}

		@Override
		public Class<? extends Annotation> getMethodAnnotation() {
			return null;
		}

		@Override
		public Class<? extends Annotation> getFieldAnnotation() {
			return null;
		}

		@Override
		public void addClass(Annotation a, Class<?> clazs) throws ScanningApplicationException {
			try {
				map.put(clazs.getName(), new TempCacheDao(clazs));
			}catch (Exception e) {
				throw new ScanningApplicationException(e);
			}
		}

		@Override
		public void addMethod(Annotation a, Class<?> clazs, Method method) throws ScanningApplicationException {
			
		}

		@Override
		public void addField(Annotation a, Class<?> clazs, Field field) throws ScanningApplicationException {
			
		}
		
	}
	
	/**
	 * <p>扫描带{@link Service}的类
	 * @author hutrace
	 * @see ScanningAnnotationConduct
	 * @since 1.8
	 * @version 1.0
	 */
	class ScanningService implements ScanningAnnotationConduct {
		
		/**
		 * <p>实例化Service中的字段
		 * <p>判断Service类中的字段，对添加注解{@link Autowire}的字段，并判断它们是{@link Service}或者{@link DAO}的字段才进行注入
		 * <p>如果不是{@link Service}活着或者{@link DAO}，将会抛出异常.
		 * @throws ScanningApplicationException <br/>
		 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
		 * &emsp;2. 你注入{@link Service}时，被注入的类没有添加{@link Service}注解<br/>
		 * &emsp;3. 你注入{@link DAO}时，被注入的类没有添加{@link DAO}注解<br/>
		 */
		private void instantiationField() throws ScanningApplicationException {
			Set<Entry<String, TableService>> set =  serviceMap.entrySet();
			List<TableDao> daos;
			List<TableService> tableServices;
			List<Field> tableServiceFields;
			for(Entry<String, TableService> entry : set) {
				Field[] fields = entry.getValue().clazs.getDeclaredFields();
				daos = new ArrayList<>();
				tableServices = new ArrayList<>();
				tableServiceFields = new ArrayList<>();
				for(Field field : fields) {
					if(field.getAnnotation(Autowire.class) != null) {
						field.setAccessible(true);
						DAO dao = field.getType().getAnnotation(DAO.class);//
						if(dao != null) {
							instantiationFieldDao(field, entry.getValue(), daos);
							continue;
						}
						Service service = field.getType().getAnnotation(Service.class);
						if(service != null) {
							instantiationFieldService(field, entry.getValue(), tableServices, tableServiceFields);
							continue;
						}
						// 非DAO和Service类注入时直接抛出异常
						throw new ScanningApplicationException(SystemProperty.get(
								"serve.mapping.scan.service.error", field.getType().getName()));
					}
				}
				addDaosAndServices(daos, tableServices, entry.getValue(), tableServiceFields);
			}
		}
		
		/**
		 * <p>初始化{@link TableService}中的{@link TableService#childDaos}和{@link TableService#childServices}
		 * @param daos
		 * @param tableServices
		 * @param tableService
		 */
		private void addDaosAndServices(List<TableDao> daos, List<TableService> tableServices, TableService tableService, List<Field> tableServiceFields) {
			if(daos.size() > 0) {
				TableDao[] objs = new TableDao[daos.size()];
				for(int i = 0; i < objs.length; i++) {
					objs[i] = daos.get(i);
					tableService.childDaos = objs;
				}
			}
			if(tableServices.size() > 0) {
				TableService[] ts = new TableService[tableServices.size()];
				Field[] fields = new Field[tableServices.size()];
				for(int i = 0; i < ts.length; i++) {
					ts[i] = tableServices.get(i);
					fields[i] = tableServiceFields.get(i);
				}
				tableService.childServices = ts;
				tableService.childServiceFields = fields;
			}
		}
		
		/**
		 * <p>实例化字段类注解为{@link DAO}的类
		 * @param field
		 * @param tableService 当前的TableService
		 * @param daos
		 * @throws ScanningApplicationException
		 */
		private void instantiationFieldDao(Field field, TableService tableService,
				List<TableDao> daos) throws ScanningApplicationException {
			Object instance = daoMap.get(field.getType().getName());
			if(instance == null) {
				throw new ScanningApplicationException(SystemProperty.get(
						"serve.mapping.scan.dao.error", field.getType().getName()));
			}
			if(tableService.singleton) {
				try {
					field.set(tableService.instance, instance);
				}catch (IllegalArgumentException e) {
					throw new ScanningApplicationException(e);
				}catch (IllegalAccessException e) {
					throw new ScanningApplicationException(e);
				}
			}else {
				daos.add(new TableDao(instance, field));
			}
		}
		
		/**
		 * <p>实例化字段类注解为{@link Service}的类
		 * @param field
		 * @param tableService
		 * @param tableServices
		 * @throws ScanningApplicationException<br/>
		 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
		 * &emsp;2. 你注入{@link Service}时，被注入的类没有添加{@link Service}注解<br/>
		 */
		private void instantiationFieldService(Field field, TableService tableService, List<TableService> tableServices,
				List<Field> tableServiceFields) throws ScanningApplicationException {
			TableService instance = serviceMap.get(field.getType().getName());
			if(tableService.singleton && instance.singleton) {
				try {
					field.set(tableService.instance, instance.instance);
				}catch (IllegalArgumentException e) {
					throw new ScanningApplicationException(e);
				}catch (IllegalAccessException e) {
					throw new ScanningApplicationException(e);
				}
			}else {
				tableServiceFields.add(field);
				tableServices.add(instance);
			}
		}
		
		@Override
		public String[] getPackages() {
			return Configuration.scan();
		}

		@Override
		public Class<? extends Annotation> getTypeAnnotation() {
			return Service.class;
		}

		@Override
		public Class<? extends Annotation> getMethodAnnotation() {
			return null;
		}

		@Override
		public Class<? extends Annotation> getFieldAnnotation() {
			return null;
		}

		@Override
		public void addClass(Annotation a, Class<?> clazs) throws ScanningApplicationException {
			try {
				serviceMap.put(clazs.getName(), new TableService(clazs));
			}catch (Exception e) {
				throw new ScanningApplicationException(e);
			}
		}

		@Override
		public void addMethod(Annotation a, Class<?> clazs, Method method) throws ScanningApplicationException {
			
		}

		@Override
		public void addField(Annotation a, Class<?> clazs, Field field) throws ScanningApplicationException {
			
		}
		
	}
	
	
	/**
	 * <p>扫描带{@link Service}的类
	 * @author <hutrace
	 * @see ScanningAnnotationConduct
	 * @since 1.8
	 * @version 1.0
	 */
	class ScanningController implements ScanningAnnotationConduct {
		
		/**
		 * <p>实例化Service中的字段
		 * <p>判断Service类中的字段，对添加注解{@link Autowire}的字段，并判断它们是{@link Service}才进行注入
		 * @throws ScanningApplicationException <br/>
		 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
		 * &emsp;2. 你注入{@link Service}时，被注入的类没有添加{@link Service}注解<br/>
		 */
		private void instantiationField() throws ScanningApplicationException {
			Set<Entry<String, TableController>> set =  controllerMap.entrySet();
			List<TableService> tableServices;
			List<Field> tableServiceFields;
			for(Entry<String, TableController> entry : set) {
				Field[] fields = entry.getValue().clazs.getDeclaredFields();
				tableServices = new ArrayList<>();
				tableServiceFields = new ArrayList<>();
				for(Field field : fields) {
					if(field.getAnnotation(Autowire.class) != null) {
						field.setAccessible(true);
						Service service = field.getType().getAnnotation(Service.class);
						if(service != null) {
							instantiationFieldService(field, entry.getValue(), tableServices, tableServiceFields);
							continue;
						}
						// 非Service类注入时直接抛出异常
						throw new ScanningApplicationException(SystemProperty.get(
								"serve.mapping.scan.control.error", field.getType().getName()));
					}
				}
				addServices(tableServices, tableServiceFields, entry.getValue());
			}
		}
		
		/**
		 * <p>初始化{@link TableController}中的{@link TableController#childServices}
		 * @param daos
		 * @param tableServices
		 * @param tableService
		 */
		private void addServices(List<TableService> tableServices, List<Field> tableServiceFields, TableController tableController) {
			if(tableServices.size() > 0) {
				TableService[] ts = new TableService[tableServices.size()];
				Field[] fields = new Field[tableServices.size()];
				for(int i = 0; i < ts.length; i++) {
					ts[i] = tableServices.get(i);
					fields[i] = tableServiceFields.get(i);
				}
				tableController.childServices = ts;
				tableController.childFields = fields;
			}
		}
		
		/**
		 * <p>实例化字段类注解为{@link Service}的类
		 * @param field
		 * @param tableController
		 * @param tableServices
		 * @throws ScanningApplicationException<br/>
		 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
		 * &emsp;2. 你注入{@link Service}时，被注入的类没有添加{@link Service}注解<br/>
		 */
		private void instantiationFieldService(Field field, TableController tableController,
				List<TableService> tableServices, List<Field> tableServiceFields) throws ScanningApplicationException {
			TableService instance = serviceMap.get(field.getType().getName());
			if(tableController.singleton && instance.singleton) {
				try {
					field.set(tableController.instance, instance.instance);
				}catch (IllegalArgumentException e) {
					throw new ScanningApplicationException(e);
				}catch (IllegalAccessException e) {
					throw new ScanningApplicationException(e);
				}
			}else {
				tableServiceFields.add(field);
				tableServices.add(instance);
			}
		}
		
		@Override
		public String[] getPackages() {
			return Configuration.scan();
		}

		@Override
		public Class<? extends Annotation> getTypeAnnotation() {
			return Controller.class;
		}

		@Override
		public Class<? extends Annotation> getMethodAnnotation() {
			return null;
		}

		@Override
		public Class<? extends Annotation> getFieldAnnotation() {
			return null;
		}

		@Override
		public void addClass(Annotation a, Class<?> clazs) throws ScanningApplicationException {
			try {
				controllerMap.put(clazs.getName(), new TableController(clazs));
			}catch (Exception e) {
				throw new ScanningApplicationException(e);
			}
		}

		@Override
		public void addMethod(Annotation a, Class<?> clazs, Method method) throws ScanningApplicationException {
			
		}

		@Override
		public void addField(Annotation a, Class<?> clazs, Field field) throws ScanningApplicationException {
			
		}
		
	}
	
	/**
	 * <p>扫描带{@link Service}的类
	 * @author hutrace
	 * @see ScanningAnnotationConduct
	 * @since 1.8
	 * @version 1.0
	 */
	class ScanningWebSocket implements ScanningAnnotationConduct {
		
		/**
		 * <p>实例化Service中的字段
		 * <p>判断Service类中的字段，对添加注解{@link Autowire}的字段，并判断它们是{@link Service}才进行注入
		 * @throws ScanningApplicationException <br/>
		 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
		 * &emsp;2. 你注入{@link Service}时，被注入的类没有添加{@link Service}注解<br/>
		 */
		private void instantiationField() throws ScanningApplicationException {
			Set<Entry<String, TableWebSocket>> set =  webSocketMap.entrySet();
			List<TableService> tableServices;
			List<Field> tableServiceFields;
			for(Entry<String, TableWebSocket> entry : set) {
				Field[] fields = entry.getValue().clazs.getDeclaredFields();
				tableServices = new ArrayList<>();
				tableServiceFields = new ArrayList<>();
				for(Field field : fields) {
					if(field.getAnnotation(Autowire.class) != null) {
						field.setAccessible(true);
						Service service = field.getType().getAnnotation(Service.class);
						if(service != null) {
							instantiationFieldService(field, entry.getValue(), tableServices, tableServiceFields);
							continue;
						}
						// 非Service类注入时直接抛出异常
						throw new ScanningApplicationException(SystemProperty.get(
								"serve.mapping.scan.control.error", field.getType().getName()));
					}
				}
				addServices(tableServices, tableServiceFields, entry.getValue());
			}
		}
		
		/**
		 * <p>初始化{@link TableController}中的{@link TableController#childServices}
		 * @param daos
		 * @param tableServices
		 * @param tableService
		 */
		private void addServices(List<TableService> tableServices, List<Field> tableServiceFields, TableWebSocket tableWebSocket) {
			if(tableServices.size() > 0) {
				TableService[] ts = new TableService[tableServices.size()];
				Field[] fields = new Field[tableServices.size()];
				for(int i = 0; i < ts.length; i++) {
					ts[i] = tableServices.get(i);
					fields[i] = tableServiceFields.get(i);
				}
				tableWebSocket.childServices = ts;
				tableWebSocket.childFields = fields;
			}
		}
		
		/**
		 * <p>实例化字段类注解为{@link Service}的类
		 * @param field
		 * @param tableController
		 * @param tableServices
		 * @throws ScanningApplicationException<br/>
		 * &emsp;1. 当实例化错误的时候会抛出此异常(一般不会出现这种情况)<br/>
		 * &emsp;2. 你注入{@link Service}时，被注入的类没有添加{@link Service}注解<br/>
		 */
		private void instantiationFieldService(Field field, TableWebSocket tableController,
				List<TableService> tableServices, List<Field> tableServiceFields) throws ScanningApplicationException {
			TableService instance = serviceMap.get(field.getType().getName());
			if(tableController.singleton && instance.singleton) {
				try {
					field.set(tableController.instance, instance.instance);
				}catch (IllegalArgumentException e) {
					throw new ScanningApplicationException(e);
				}catch (IllegalAccessException e) {
					throw new ScanningApplicationException(e);
				}
			}else {
				tableServiceFields.add(field);
				tableServices.add(instance);
			}
		}
		
		@Override
		public String[] getPackages() {
			return Configuration.scan();
		}

		@Override
		public Class<? extends Annotation> getTypeAnnotation() {
			return WebSocket.class;
		}

		@Override
		public Class<? extends Annotation> getMethodAnnotation() {
			return null;
		}

		@Override
		public Class<? extends Annotation> getFieldAnnotation() {
			return null;
		}

		@Override
		public void addClass(Annotation a, Class<?> clazs) throws ScanningApplicationException {
			try {
				webSocketMap.put(clazs.getName(), new TableWebSocket(clazs));
			}catch (Exception e) {
				throw new ScanningApplicationException(e);
			}
		}

		@Override
		public void addMethod(Annotation a, Class<?> clazs, Method method) throws ScanningApplicationException {
			
		}

		@Override
		public void addField(Annotation a, Class<?> clazs, Field field) throws ScanningApplicationException {
			
		}
		
	}
	
}
