package com.xixi.service.Bill;

import com.xixi.dao.Bill.BillDao;
import com.xixi.dao.Bill.BillDaoImpl;
import com.xixi.dao.baseDao;
import com.xixi.pojo.Bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillServiceImpl implements BillService{
    private BillDao billDao;
    public BillServiceImpl(){
        billDao = new BillDaoImpl();
    }
    @Override
    public boolean add(Bill bill) throws Exception {
        boolean flag;
        Connection connection=null;
         connection = baseDao.getConnection();
         connection.setAutoCommit(false);
        int add = billDao.add(connection, bill);
        connection.commit();
        if (add>0){
            flag=true;
        }else flag=false;
           baseDao.close(connection,null,null);
          return flag;
    }

    @Override
    public List<Bill> getBillList(Bill bill) throws Exception {
       List<Bill> billList = new ArrayList<>();
        Connection connection=null;
         connection = baseDao.getConnection();
         billList = billDao.getBillList(connection, bill);
         baseDao.close(connection,null,null);
          return billList;
    }

    @Override
    public boolean deleteBillById(String delId) throws SQLException {
        boolean flag=false;
        int delNum=0;
        Connection connection=null;
        try {
            connection=baseDao.getConnection();
            delNum=billDao.deleteBillById(connection,delId);
            if(delNum>0){
                flag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            baseDao.close(connection,null,null);
        }
        return flag;
    }

    @Override
    public Bill getBillById(String id) throws SQLException {
        Bill bill = new Bill();
        Connection connection=null;
        try {
            connection=baseDao.getConnection();
            bill = billDao.getBillById(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            baseDao.close(connection,null,null);
        }
        return bill;

    }

    @Override
    public boolean modify(Bill bill) throws SQLException {
        Boolean flag=false;
        int modifyNum=0;
        Connection connection=null;
        try {
            connection=baseDao.getConnection();
            modifyNum=billDao.modify(connection,bill);
            if(modifyNum>0){
                flag=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            baseDao.close(connection,null,null);
        }
        return flag;

    }
}
