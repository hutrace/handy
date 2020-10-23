package org.hutrace.handy.mapping;

import java.util.Map;

import org.hutrace.handy.annotation.DAO;
import org.hutrace.handy.annotation.Service;
import org.hutrace.handy.exception.AppLoaderException;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;

/**
 * <p>映射执行者，一键调用{@link #start()}方法既可以执行
 * <p>它不仅仅只是加载映射，还会将{@link Service}和{@link DAO}进行扫描与关系绑定
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class MappingExecutor {
	
	private static Logger log = LoggerFactory.getLogger(MappingExecutor.class);
	
	/**
	 * <p>开始执行映射加载
	 * <p>扫描顺序为DAO、Service、Controller、WebSocket
	 * <p>将扫描到的结果进行关系绑定
	 * @throws AppLoaderException
	 */
	public void start() throws AppLoaderException {
		log.debug("serve.mapping.scan.start");
		MappingScanning scan = new MappingScanning();
		log.debug("serve.mapping.scan.dao");
		Map<String, Object> daoMap = scan.scanningDao();
		log.debug("serve.mapping.scan.service");
		Map<String, TableService> serviceMap = scan.scanningService();
		log.debug("serve.mapping.scan.control");
		Map<String, TableController> controllerMap = scan.scanningController();
		log.debug("serve.mapping.scan.ws");
		Map<String, TableWebSocket> webSocketMap = scan.scanningWebSocket();
		log.debug("serve.mapping.scan.finish");
		MappingTable.instance.init(daoMap, serviceMap, controllerMap, webSocketMap);
		log.debug(MappingTable.instance.debugControl(), false);
		log.debug(MappingTable.instance.debugWebSocket(), false);
	}
	
}
