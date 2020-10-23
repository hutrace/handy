package org.hutrace.handy.mapping;

import java.util.List;
import java.util.Map;

/**
 * 标准DAO接口
 * <p>所有持久层操作实现此类用作公共dao
 * @author hu trace
 */
public interface InterfaceDao {

	
	/**
	 * <p>查询一个字段，且只有一条数据的数字，不传入查询语句条件
	 * @param statement Mapper namespace.id
	 * @return int数字，Mapper中result类型需要为int或者_int
	 * @throws Throwable
	 */
	public int selectInt(String statement) throws Throwable;
	
	/**
	 * <p>查询一个字段，且只有一条数据的数字，需要传入查询语句条件，参数可以传入null
	 * @param statement Mapper namespace.id
	 * @param parameter 参数，与Mapper中parameter类型匹配
	 * @return int数字，Mapper中result类型需要为int或者_int
	 * @throws Throwable
	 */
	public int selectInt(String statement, Object parameter) throws Throwable;
	
	/**
	 * 查询一个字段，且只有一条数据的数字，不传入查询语句条件
	 * @param statement Mapper namespace.id
	 * @return long数字，Mapper中result类型需要为long或者_long
	 * @throws Throwable
	 */
	public long selectLong(String statement) throws Throwable;
	
	/**
	 * 查询一个字段，且只有一条数据的数字，需要传入查询语句条件，参数可以传入null
	 * @param statement Mapper namespace.id
	 * @param parameter 参数，与Mapper中parameter类型匹配
	 * @return long数字，Mapper中result类型需要为long或者_long
	 * @throws Throwable
	 */
	public long selectLong(String statement, Object parameter) throws Throwable;
	
	/**
	 * 查询一个字段，且只有一条数据的字符串，不传入查询语句条件
	 * @param statement Mapper namespace.id
	 * @return 字符串，Mapper中result类型需要为string
	 * @throws Throwable
	 */
	public String selectString(String statement) throws Throwable;
	
	/**
	 * 查询一个字段，且只有一条数据的字符串，需要传入查询语句条件，参数可以传入null
	 * @param statement Mapper namespace.id
	 * @param parameter 参数，与Mapper中parameter类型匹配
	 * @return 字符串，Mapper中result类型需要为string
	 * @throws Throwable
	 */
	public String selectString(String statement, Object parameter) throws Throwable;
	
	/**
	 * 查询一条数据，不传入查询语句条件
	 * @param <T>
	 * @param statement
	 * @return
	 * @throws Throwable
	 */
	public <T> T selectOne(String statement) throws Throwable;
	
	/**
	 * <p>查询一条数据，需要传入查询语句条件，参数可以传入null
	 * @param statement Mapper namespace.id
	 * @param parameter 参数，与Mapper中parameter类型匹配
	 * @return 泛型，与Mapper中result相匹配
	 * @throws Throwable
	 */
	public <T> T selectOne(String statement, Object parameter) throws Throwable;
	
	/**
	 * <p>查询多条数据，不传入查询语句条件
	 * @param statement Mapper namespace.id
	 * @return 泛型，与Mapper中result类型匹配的{@link List}集合
	 * @throws Throwable
	 */
	public <T> List<T> selectList(String statement) throws Throwable;
	
	/**
	 * <p>查询多条数据，需要传入查询语句条件，参数可以传入null
	 * @param statement Mapper namespace.id
	 * @param parameter 参数，与Mapper中parameter类型匹配
	 * @return 泛型，与Mapper中result类型匹配的{@link List}集合
	 * @throws Throwable
	 */
	public <T> List<T> selectList(String statement, Object parameter) throws Throwable;
	
	/**
	 * <p>修改数据，不传入sql语句条件
	 * @param statement Mapper namespace.id
	 * @return 受影响的数据条数，可以根据此返回值判断是否完成修改数据
	 * @throws Throwable
	 */
	public int update(String statement) throws Throwable;
	
	/**
	 * <p>修改数据，需要传入sql语句条件，参数可以传入null，可以根据参数批量修改数据
	 * @param statement Mapper namespace.id
	 * @param parameter 参数，与Mapper中parameter类型匹配
	 * @return 受影响的数据条数，可以根据此返回值判断是否完成修改数据
	 * @throws Throwable
	 */
	public int update(String statement, Object parameter) throws Throwable;
	
	/**
	 * <p>删除数据，不传入sql语句条件
	 * @param statement Mapper namespace.id
	 * @return 受影响的数据条数，可以根据此返回值判断是否完成删除数据
	 * @throws Throwable
	 */
	public int delete(String statement) throws Throwable;
	
	/**
	 * <p>删除数据，需要传入sql语句条件，参数可以传入null，可以根据参数批量删除数据
	 * @param statement Mapper namespace.id
	 * @param parameter 参数，与Mapper中parameter类型匹配
	 * @return 受影响的数据条数，可以根据此返回值判断是否完成删除数据
	 * @throws Throwable
	 */
	public int delete(String statement, Object parameter) throws Throwable;
	
	/**
	 * <p>新增（添加、插入）数据，需要传入sql语句条件，参数可以传入null，可以根据参数批量删除数据
	 * @param statement Mapper namespace.id
	 * @return 受影响的数据条数，可以根据此返回值判断是否完成添加数据
	 * @throws Throwable
	 */
	public int insert(String statement) throws Throwable;
	
	/**
	 * <p>新增（添加、插入）数据，不传入sql语句条件
	 * @param statement Mapper namespace.id
	 * @param parameter 参数，与Mapper中parameter类型匹配
	 * @return 受影响的数据条数，可以根据此返回值判断是否完成添加数据
	 * @throws Throwable
	 */
	public int insert(String statement, Object parameter) throws Throwable;
	
	/**
	 * <p>分页查询数据
	 * @param <T>
	 * @param statement Mapper namespace.id
	 * @param parameter 参数，与Mapper中parameter类型匹配
	 * @return 返回的map中包含了list(数据)和count(总数量)
	 * @throws Throwable
	 */
	public <T> Map<String, Object> queryPage(String statement, Object parameter) throws Throwable;
	
}
