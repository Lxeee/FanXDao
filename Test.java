package com.dao;

import java.util.List;

import com.entity.Address;
import com.entity.Orders;
import com.entity.Users;


public class Test {
	public static void main(String[] args) throws Exception {
		Test t=new Test();
//		t.selectone();
//		t.selectAll();
//		t.selectBet();
		t.selectLike();
//		t.selectGroup();
//		t.selectSum();
//		t.selectLimit();
//		t.selectCount();
//		t.add();
//		t.update();
//		t.delete();
	}
	
	public void add() throws Exception{
		//添加user对象
//		Users u=new Users();
//		u.setId(559);
//		u.setUsername("lixiaoyy");
//		u.setPassword("lalalala");
//		System.out.println(u);
//		GeneralDaoImpl<Users> g=new GeneralDaoImpl<Users>();
//		g.insert(u);
		//添加address对象
		Address a=new Address();
		a.setId(14);
		a.setName("mdj");
		DaoImpl<Address> g=new DaoImpl<Address>();
		g.insert(a);
	}
	
	public void update(){
		Users u=new Users();
		u.setId(56);
		u.setUsername("eeopop");
		u.setPassword("33w");
		DaoImpl<Users> g=new DaoImpl<Users>();
		g.update(u);
	}
	
	public void delete(){
		Users u=new Users();
		u.setId(56);
		DaoImpl<Users> g=new DaoImpl<Users>();
		g.delete(u);
	}
	
	public void selectAll(){
		DaoImpl<Users> g=new DaoImpl<Users>();
		Users user = new Users();
        List list = g.select(user);
        for (int a = 0; a < list.size(); a++) {
            Users user11 = (Users) list.get(a);
            System.out.println("id="+user11.getId()+" name="+user11.getUsername()+" password="+user11.getPassword());
        }
	}
	
	public void selectone(){
		DaoImpl<Users> g=new DaoImpl<Users>();
		Users user = new Users();
		user.setId(47);
        user=g.selectOne(user);
        System.out.println(user);
	}
	
	public void selectBet(){
		DaoImpl<Users> g=new DaoImpl<Users>();
		Users user = new Users();
		int first=40,last=50;
		List list =g.selectBet(user, first, last);
		for (int a = 0; a < list.size(); a++) {
            Users user11 = (Users) list.get(a);
            System.out.println("id="+user11.getId()+" name="+user11.getUsername()+" password="+user11.getPassword());
        }
	}
	
	public void selectLike() throws Exception{
		Users u=new Users();
		DaoImpl<Users> g=new DaoImpl<Users>();
		List list =g.selectLike(u,"password","4");
		for (int a = 0; a < list.size(); a++) {
            Users user11 = (Users) list.get(a);
            System.out.println("id="+user11.getId()+" name="+user11.getUsername()+" password="+user11.getPassword());
        }
	}
	
	public void selectGroup() {
		Orders o=new Orders();
		DaoImpl<Orders> g=new DaoImpl<Orders>();
		List list =g.selectGroup(o, "name");
		   for (int a = 0; a < list.size(); a++) {
	         Orders or=(Orders) list.get(a);
	         System.out.println("id=" + or.getId() + ", name=" + or.getName() + ", address=" + or.getAddress()
		          + ", tel=" + or.getTel() + ", money=" + or.getMoney());
	        }
	}
	 
	public void selectSum() {
		Orders o=new Orders();
		DaoImpl<Orders> g=new DaoImpl<Orders>();
		int sum=g.selectSum(o, "address");
		System.out.println(sum);
	}
	
	public void selectCount() {
		Users u=new Users();
		DaoImpl<Users> g=new DaoImpl<Users>();
		System.out.println(g.selectCount(u));
	}
	
	public void selectLimit(){
		Orders o=new Orders();
		DaoImpl<Orders> g=new DaoImpl<Orders>();
		List list =g.selectLimit(o, 10, 5);
		for (int a = 0; a < list.size(); a++) {
	           Orders or=(Orders) list.get(a);
	           System.out.println("id=" + or.getId() + ", name=" + or.getName() + ", address=" + or.getAddress()
				+ ", tel=" + or.getTel() + ", money=" + or.getMoney());
	        }
	}

}
