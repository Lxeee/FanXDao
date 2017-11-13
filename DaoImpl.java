package com.dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.entity.Address;
import com.entity.Entity;
import com.entity.Users;
import com.util.ConnectionManager;



public class DaoImpl<T> implements GeneralDao<T>{

	private static final String INSERT="insert into ";
	private static final String UPDATE="update ";
	private static final String DELETE="delete from ";
	private static final String SELECT="select * from ";
	private int flag=0;
    Connection conn = ConnectionManager.getConnection();

    
/**
 * insert方法
 */
	public int insert(T entity) throws Exception {
		
		//拼接sql语句
		DaoImpl d=new DaoImpl();
		String sql=d.getSql(entity,INSERT);
		Class clazz = entity.getClass();
		
        //执行jdbc
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            //确定ps.setString()中的索引值
            int i = 0;
            Field[] field = clazz.getDeclaredFields();
            Field.setAccessible(field, true);
            //对属性遍历
            for (Field field1 : field) {
                i = i + 1;
                PropertyDescriptor pd = new PropertyDescriptor(field1.getName(), clazz);
                //获取属性的get方法  通过get方法获取对象的属性
                Method getMethod = pd.getReadMethod();
                Object obj = (Object) getMethod.invoke(entity);
                //判断属性的类型 从而判断使用ps的那个方法
                //如果为String类型
                if (field1.getType() == String.class) {
                    ps.setString(i, obj.toString());
                    System.out.println(i+"-----"+obj.toString());
                    //如果为integer类型
                }else if (field1.getType() == Integer.class) {
                    ps.setInt(i, Integer.parseInt(obj.toString()));
                    System.out.println(i+"-----"+Integer.parseInt(obj.toString()));
                }
            }
            ps.execute();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return 0;
        
	}
	
	
/**
 * update方法
 */
	public int update(T entity) {
		
		 //拼接sql语句
	     DaoImpl d=new DaoImpl();
     	 String sql=d.getSql(entity,UPDATE);
	     Class clazz = entity.getClass();

	     try {
	         PreparedStatement ps = conn.prepareStatement(sql);
	         int i = 0;
	         int j = 0;
	         Field[] field = clazz.getDeclaredFields();
	         for (Field field1 : field) {
	        	 
	           //如果属性是id就跳过
	            if ("id".equals(field1.getName())) {
	                  continue;
	             }
	             i = i + 1;
	             PropertyDescriptor pd = new PropertyDescriptor(field1.getName(), clazz);
	             Method getMethod = pd.getReadMethod();
	             Object obj = (Object) getMethod.invoke(entity);
	             
	             //判断属性类型
	             if (field1.getType() == String.class) {
	                ps.setString(i, obj.toString());
	             } else if (field1.getType() == Integer.class) {
	                ps.setInt(i, Integer.parseInt(obj.toString()));
	             }
	             j = i;
	         }
	         //获取id的值并填入ps的方法中
	         for (Field field1 : field) {
	             PropertyDescriptor pd = new PropertyDescriptor(field1.getName(), clazz);
	             Method getMethod = pd.getReadMethod();
	             Object obj = (Object) getMethod.invoke(entity);
	             System.out.println(obj);
	             ps.setInt(j + 1, Integer.parseInt(obj.toString()));
	             break;
	         }
	         ps.executeUpdate();

	     } catch (Exception e) {
	        // TODO Auto-generated catch block
		     e.printStackTrace();
		 } 
	     return 0;
	     
	}
	
	
/**
 * delete方法
 */
	public int delete(T entity) {
		
		 //拼接sql语句
	     DaoImpl d=new DaoImpl();
         String sql=d.getSql(entity,DELETE);
	     Class clazz = entity.getClass();
	        
	     try {
	         PreparedStatement ps = conn.prepareStatement(sql);
	         Field[] field = clazz.getDeclaredFields();
	         //获取id的值并插入ps的方法中
	         for (Field field1 : field) {
	             PropertyDescriptor pd = new PropertyDescriptor(field1.getName(), clazz);
	             Method getMethod = pd.getReadMethod();
	             Object obj = (Object) getMethod.invoke(entity);
	             ps.setInt(1, Integer.parseInt(obj.toString()));
	             break;//获取到id的值就退出循环
	         }
	         ps.execute();
	     } catch (Exception e) {
	          e.printStackTrace();
	     }		
	     return 0;
	     
	}
    
	
/**
 * 根据id获取数据
 */
	public T selectOne(T entity) {
		
        DaoImpl d=new DaoImpl();
		String sql=d.getSql(entity,SELECT);
	    sql +=" where id=?";
        System.out.println(sql);
        Class clazz = entity.getClass();
        
        Field[] field = clazz.getDeclaredFields();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
            //获取id的值并插入ps的方法中
            for (Field field1 : field) {
                PropertyDescriptor pd = new PropertyDescriptor(field1.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object obj = (Object) getMethod.invoke(entity);
                ps.setInt(1, Integer.parseInt(obj.toString()));
                break;//获取到id的值就退出循环
            }
			ResultSet rs = ps.executeQuery();
		      while (rs.next()) {
		          Entity e = (Entity)clazz.newInstance();
		             for (int j = 1; j <=field.length; j++) {
		                 for (Field field1 : field) {
		                   PropertyDescriptor pd = new PropertyDescriptor(field1.getName(), clazz);
		                   Method setMethod = pd.getWriteMethod();
		                   if (field1.getType() == String.class) {
		                        setMethod.invoke(e, rs.getString(j));
		                        j+=1;
		                   }else if (field1.getType() == Integer.class) {
		                        setMethod.invoke(e, rs.getInt(j));
		                        j+=1;
		                   }
		                 }
		             }		  
		             return (T) e;
		       }
			}  catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		return null;
		
	}
	
	
/**
 * 获取数据库表中所有数据
 */
	public List<Object> select(T entity) {
		
		 DaoImpl d=new DaoImpl();
	  	 String sql=d.getSql(entity,SELECT);
	     System.out.println(sql);
	     Class clazz = entity.getClass();
	   
         DaoImpl d2=new DaoImpl();
         flag=0;
         List<Object> list =(List<Object>) d2.returnRes(clazz, sql,flag);
         return (List<Object>) list;
         
	}
/**
 * 根据id区间获取数据
 */
    public List<Object> selectBet(T entity,int first,int last) {
    	
        //拼接sql语句 
    	 DaoImpl d=new DaoImpl();
	  	 String sql=d.getSql(entity,SELECT);
	  	 sql +=" where id between ? and ?";
	     System.out.println(sql);
	     Class clazz = entity.getClass();
    	
    	 //执行sql语句
         List<Object> list = new ArrayList<Object>();
         Field[] field = clazz.getDeclaredFields();
         try {
             PreparedStatement ps = conn.prepareStatement(sql);
             ps.setInt(1, first);
             ps.setInt(2, last);
             ResultSet rs = ps.executeQuery();
             while (rs.next()) {
            	  Entity e = (Entity)clazz.newInstance();
                    for (int j = 1; j <=field.length; j++) {
                        for (Field field1 : field) {
                            PropertyDescriptor pd = new PropertyDescriptor(field1.getName(), clazz);
                            Method setMethod = pd.getWriteMethod();
                        if (field1.getType() == String.class) {
                            setMethod.invoke(e, rs.getString(j));
                            j+=1;
                        } else if (field1.getType() == Integer.class) {
                            setMethod.invoke(e, rs.getInt(j));
                            j+=1;
                        }
                    }
                 }
                    list.add(e);
            }
        }catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
        return (List<Object>) list;
        
    }
    
    
/**
 * 根据模糊查询获取数据
 * @throws Exception 
 */
    public List<Object> selectLike(T entity,String shux,String ziduan) throws Exception {

    	//拼接sql语句
    	DaoImpl d=new DaoImpl();
	    String sql=d.getSql(entity,SELECT);
	  	sql +=" where "+shux+" like '%"+ziduan+"%'";
	    System.out.println(sql);
	    Class clazz = entity.getClass();
     
    	//执行sql语句
	    DaoImpl d2=new DaoImpl();
	    flag=0;
        List<Object> list =(List<Object>) d2.returnRes(clazz, sql,flag);
        return (List<Object>) list;
        
    }
    
    
/**
 * 根据给定的属性分组
 *  select * from orders group by name;
 */
    public List<Object> selectGroup(T entity, String shux) {
	  
	  //拼接sql语句
	    DaoImpl d=new DaoImpl();
	    String sql=d.getSql(entity,SELECT);
	  	sql +=" group by "+shux;
	    System.out.println(sql);
	    Class clazz = entity.getClass();
	   
	  //执行sql语句
	    DaoImpl d2=new DaoImpl();
	    flag=0;
        List<Object> list =(List<Object>) d2.returnRes(clazz, sql,flag);
        return (List<Object>) list;

}
  
  /**
   * 查取某一列的总和
   * select sum(money) from orders
   */
    public int selectSum(T entity, String shux) {
	  
	    //拼接sql语句
	    String sql = "select sum("+shux+") from ";
        Class clazz = entity.getClass();
        String clazzName = clazz.getName();
        String tableName = clazzName.substring(clazzName.lastIndexOf(".") + 1, clazzName.length());
        sql += tableName;
      
	    //执行sql语句
        DaoImpl d2=new DaoImpl();
	    flag=1;
        int sum=(int) d2.returnRes(clazz, sql,flag);
        return sum;
        
  }
  
  
/**
 * 查询某列的数据总条数
 * select count(*) from emp;
 */
    public int selectCount(T entity) {
	   
	    //拼接sql语句
	    String sql = "select count(*) from ";
	    Class clazz = entity.getClass();
	    String clazzName = clazz.getName();
	    String tableName = clazzName.substring(clazzName.lastIndexOf(".") + 1, clazzName.length());
	    sql += tableName;
	    System.out.println(sql);
	    
	    //执行sql语句
	    DaoImpl d2=new DaoImpl();
	    flag=1;
        int count=(int) d2.returnRes(clazz, sql,flag);
        return count;
	   
   }
   
   
/**
 * 根据给定的范围查询数据
 * select * from orders limit 10,5
 */
    public List<Object> selectLimit(T entity, int first, int last) {
	   
	    //拼接sql语句
	    DaoImpl d=new DaoImpl();
	    String sql=d.getSql(entity,SELECT);
	  	sql +=" limit "+first+","+last;
	    System.out.println(sql);
	    Class clazz = entity.getClass();
	   
	    //执行sql语句
	    DaoImpl d2=new DaoImpl();
	    flag=0;
        List<Object> list =(List<Object>) d2.returnRes(clazz, sql,flag);
        return (List<Object>) list;
        
   }
   
   
   /**
    * 拼接sql语句
    * @return
    */
    public  String getSql(T entity,String sql){
    	
   	    //获取表名
   	    Class clazz = entity.getClass();
   	    String clazzName = clazz.getName();
   	    String tableName = clazzName.substring(clazzName.lastIndexOf(".") + 1, clazzName.length());
   	    Field[] fields = clazz.getDeclaredFields();
   	   
   	    switch(sql){
   	       case INSERT:
   	    	   sql=INSERT;
   	           sql = sql + tableName + "(";
   	           for (Field field : fields) { 
   	               String fieldName = field.getName();
   	               System.out.println(fieldName);
   	               sql += fieldName + ",";
   	           }
   	           
   	           //去掉最后一个属性的逗号
   	           sql = sql.substring(0, sql.length() - 1);
   	           sql += ") values (";
   	           
   	           //获得属性的个数，拼接逗号和问号
   	           int fieldLength = fields.length;
   	           for (int i = 1; i <= fieldLength; i++) {
   	               sql += "?,";
   	           }
   	           sql = sql.substring(0, sql.length() - 1);
   	           sql += ")";
   	           System.out.println(sql);
   	           break;
   	           
   	       case DELETE:
   	    	   sql =DELETE;
 	           sql += tableName;
 	           String setStringd = " where id = ?";
 	           sql += setStringd;
 	           System.out.println(sql);
 	           break;
 	           
   	       case UPDATE:
   	    	  sql = UPDATE;
	          sql += tableName;
	          String setString = " set ";
	          sql += setString;
	          for (Field field : fields) {
	              String fieldName = field.getName();
	              fieldName = fieldName.substring(fieldName.lastIndexOf(".") + 1, fieldName.length());
	              //如果属性是id就跳过
	              if (fieldName.equals("id")) {
	                  continue;
	              }
	              sql += fieldName + "=?,";
	          }
	          sql = sql.substring(0, sql.length() - 1);
	          sql += " where id=?";
	          System.out.println(sql);
	          break;
	          
   	       case SELECT:
   	    	  sql =SELECT;
   	          sql += tableName;
   	       break;
   	       
   	     }
   	    return sql;
      }

    
/**
 * 返回list结果集
 */
    public Object returnRes(Class clazz,String sql,int flag){
    	
          List<Object> list = new ArrayList<Object>();
          Field[] field = clazz.getDeclaredFields();
          
		  try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
	              while (rs.next()) {
	            	  if(flag==1){
		            	  int num=rs.getInt(1);
		                  return num;
		              }
	              	  Entity e = (Entity)clazz.newInstance();
	                   for (int j = 1; j <=field.length; j++) {
	                          for (Field field1 : field) {
	                              PropertyDescriptor pd = new PropertyDescriptor(field1.getName(), clazz);
	                              Method setMethod = pd.getWriteMethod();
	                              if (field1.getType() == String.class) {
	                                  setMethod.invoke(e, rs.getString(j));
	                                  j+=1;
	                              }
	                              else if (field1.getType() == Integer.class) {
	                                  setMethod.invoke(e, rs.getInt(j));
	                                  j+=1;
	                              }
	                           }
	                      }
	                      list.add(e);
	                      }
	              return list;
			}catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
             return null;
      }

    	      

}
