package com.xixi.dao.Bill;

import com.mysql.cj.util.StringUtils;
import com.xixi.dao.baseDao;
import com.xixi.pojo.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BillDaoImpl implements  BillDao{

    @Override
    public int add(Connection connection, Bill bill) throws Exception {
        PreparedStatement pstm=null;
        int updateNum=0;
        if (connection!=null){
            String sql = "insert into smbms_bill (billCode,productName,productDesc," +
                    "productUnit,productCount,totalPrice,isPayment,providerId,createdBy,creationDate) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {bill.getBillCode(),bill.getProductName(),bill.getProductDesc(),
                    bill.getProductUnit(),bill.getProductCount(),bill.getTotalPrice(),bill.getIsPayment(),
                    bill.getProviderId(),bill.getCreatedBy(),bill.getCreationDate()};
            updateNum = baseDao.execute(connection, sql, params, pstm);
        }
        baseDao.close(connection,pstm,null);
        return  updateNum;
    }

    @Override
    public List<Bill> getBillList(Connection connection, Bill bill) throws Exception {
        ArrayList<Bill> bills = new ArrayList<>();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT b.*,p.proName AS providerName FROM smbms_bill b, smbms_provider p WHERE b.providerId = p.id");
            List<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(bill.getProductName())) {//判断用户是否输入商品名称
                sql.append(" AND b.`productName` LIKE ?");
                list.add("%" + bill.getProductName() + "%");
            }
            if (bill.getProviderId() > 0) {//判断是否选择了供应商
                sql.append(" AND p.`providerId`=?");
                list.add(bill.getProviderId());
            }
            if (bill.getIsPayment() > 0) {//判断是否选择了是否付款
                sql.append(" AND b.`isPayment`=?");
                list.add(bill.getIsPayment());
            }
            Object[] params = list.toArray();
            rs = baseDao.execute(connection, sql.toString(), params, pstm, rs);
            System.out.println("当前的sql是----->" + sql.toString());
            while (rs.next()) {
                Bill _bill = new Bill();//创建一个bill对象存储查询到的属性
                _bill.setId(rs.getInt("id"));
                _bill.setBillCode(rs.getString("billCode"));
                _bill.setProductName(rs.getString("productName"));
                _bill.setProductDesc(rs.getString("productDesc"));
                _bill.setProductUnit(rs.getString("productUnit"));
                _bill.setProductCount(rs.getBigDecimal("productCount"));
                _bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                _bill.setIsPayment(rs.getInt("isPayment"));
                _bill.setProviderId(rs.getInt("providerId"));
                _bill.setProviderName(rs.getString("providerName"));
                _bill.setCreationDate(rs.getTimestamp("creationDate"));
                _bill.setCreatedBy(rs.getInt("createdBy"));
                bills.add(_bill);
            }

        }
        return bills;
    }

    @Override
    public int deleteBillById(Connection connection, String delId) throws Exception {
        int deleteNum=0;
        PreparedStatement pstm=null;
        String delNum=null;
        if (connection!=null){
            String sql="DELETE FROM `smbms_bill` WHERE id=?";
            Object[] params={delId};
            deleteNum = baseDao.execute(connection, sql, params, pstm);
            baseDao.close(connection,pstm,null);
        }
        return  deleteNum;
    }

    @Override
    public Bill getBillById(Connection connection, String id) throws Exception {
        Bill bill = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        if(null != connection){
            String sql = "select b.*,p.proName as providerName from smbms_bill b, smbms_provider p " +
                    "where b.providerId = p.id and b.id=?";
            Object[] params = {id};
            rs = baseDao.execute(connection,sql, params,pstm,rs);
            if(rs.next()){
                bill = new Bill();
                bill.setId(rs.getInt("id"));
                bill.setBillCode(rs.getString("billCode"));
                bill.setProductName(rs.getString("productName"));
                bill.setProductDesc(rs.getString("productDesc"));
                bill.setProductUnit(rs.getString("productUnit"));
                bill.setProductCount(rs.getBigDecimal("productCount"));
                bill.setTotalPrice(rs.getBigDecimal("totalPrice"));
                bill.setIsPayment(rs.getInt("isPayment"));
                bill.setProviderId(rs.getInt("providerId"));
                bill.setProviderName(rs.getString("providerName"));
                bill.setModifyBy(rs.getInt("modifyBy"));
                bill.setModifyDate(rs.getTimestamp("modifyDate"));
            }
            baseDao.close(null, pstm, rs);
        }
        return bill;

    }

    @Override
    public int modify(Connection connection, Bill bill) throws Exception {
        int modifyNum=0;
        PreparedStatement pstm=null;
        if(connection!=null){
            String sql = "update smbms_bill set productName=?," +
                    "productDesc=?,productUnit=?,productCount=?,totalPrice=?," +
                    "isPayment=?,providerId=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] params = {bill.getProductName(),bill.getProductDesc(),
                    bill.getProductUnit(),bill.getProductCount(),bill.getTotalPrice(),bill.getIsPayment(),
                    bill.getProviderId(),bill.getModifyBy(),bill.getModifyDate(),bill.getId()};
            modifyNum=baseDao.execute(connection,sql,params,pstm);
            baseDao.close(null,pstm,null);
        }
        return modifyNum;
    }

    @Override
    public int getBillCountByProviderId(Connection connection, String providerId) throws Exception {
        int billcount=0;
        PreparedStatement pstm=null;
        ResultSet rs=null;
        if(connection!=null){
            String sql="SELECT COUNT(1) as billCount FROM `smbms_bill` WHERE `providerId`=?";
            Object[] params={providerId};
            rs = baseDao.execute(connection, sql, params, pstm, rs);
            while(rs.next()){
                billcount=rs.getInt("billCount");
            }
            baseDao.close(null,pstm,rs);
        }
        return billcount;
    }

}
