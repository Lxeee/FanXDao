package com.dao;

import java.util.List;


public interface GeneralDao<T> {
	public int insert(T entity) throws Exception;
	public int update(T entity);
	public int delete(T entity);
	public List<Object> select(T entity);
	public T selectOne(T entity);
	//select * from users where id between 40 and 50
    public List<Object> selectBet(T entity,int first,int last);
	//select * from users where username like 'li%'
    public List<Object> selectLike(T entity,String shux,String ziduan) throws Exception;
    //select * from orders group by name;
    public List<Object> selectGroup(T entity,String shux);
    //select sum(money) from orders
    public int selectSum(T entity,String shux);
    //select count(*) from emp;
    public int selectCount(T entity);
    //select * from orders limit 10,5
    public List<Object> selectLimit(T entity,int first,int last); 

}
