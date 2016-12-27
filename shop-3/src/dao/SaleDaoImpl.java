package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Sale;

@Repository
public class SaleDaoImpl implements SaleDao{
   private NamedParameterJdbcTemplate template;
   @Autowired
   public void SetDataSource(DataSource dataSource){
      template = new NamedParameterJdbcTemplate(dataSource);
   }
   @Override
   public Integer getMaxSaleId() {
      String sql = "select nvl(max(saleid),0) from sale";
      int i = template.queryForObject(sql, new HashMap(), Integer.class);  //queryForObject() : ��ȸ�� ���ڵ� ���� �� ���� ����� , i : sale���̺��� �ִ� saleId���� �����ϰ� �ִº���
      return i+1; //���� sale ���̺� �ִ� �ִ� saleId����  �ϳ� ������Ŵ.
   }

   @Override
   public void createSale(Sale sale) {
      String sql = "insert into sale (saleId, userId, updateTime) values (:saleId, :user.userId, :updateTime)"; //:user.userId -> sale.getUser().getUserId()�� ���� ������ (�̹� sale�ȿ��� user������ �� �ְ�, �� user��ü�� ������ �������°���)
      SqlParameterSource param = new BeanPropertySqlParameterSource(sale);
      template.update(sql, param);
   }
@Override
public List<Sale> list(String id) {
	String sql = "select * from sale where userid = :userId";
	Map<String, String> param = new HashMap<String,String>();
	param.put("userId", id);
	RowMapper<Sale> mapper = new BeanPropertyRowMapper<Sale>(Sale.class);
	return template.query(sql, param,mapper);
}
}