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

import logic.Item;


/*
 * @Repository �� �⺻������ @Component�� (����)
 * Component��� (�ش� ���, Ȥ�� Ŭ������)��üȭ +  Model���.
 * 
 */

@Repository //���Ӱ�ü (DB�� ����Ǿ� �ʿ�� �ϴ°��� �����͸� �Ѱ��� �� �ְ��ϴ� ��ü)
public class ItemDaoImpl implements ItemDao {
	private NamedParameterJdbcTemplate template;
	
	@Autowired//DataSource��ü�� �����ϰ� �ȴ�. -> NamedParameterJdbcTemplate�� �ٷ� Autowired�� ���̸� �ȵȴ�. NamedParameterJdbcTemplate�� ������ �ʾұ� ������
	 		//�׷��� setter�� �̿��Ͽ� dataSource�� �Ѱ��־� NamedParameterJdbcTemplate��ü�� �����.
	public void setDataSource(DataSource datasource){
		template = new NamedParameterJdbcTemplate(datasource);
	}
	@Override
	public List<Item> findAll() {
		String sql = "select * from item order by id";
		
		/*
		 * ItemŬ������ ������Ƽ�� item���̺��� �÷����� ���Ͽ� ���� �̸��� Item��ü�� ������ ����. (�ڼ��� ������ shop-1�� JavaResources - src/dao/ItemDaoImpl.java �ּ� ����)
		 */
		RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class); //������ �ٲ�� �������� set�� �̿��ؼ� �ִ� ���� �ƴ϶�, Rowmapper�� �̿��� �ѹ��� Item��ü ��ü�� �־��ش�.
		
		return template.query(sql, mapper);
	}
	@Override
	public Item selectById(Integer id) {
		String sql = "select * from item where id=:id";
		RowMapper<Item> mapper= new BeanPropertyRowMapper<Item>(Item.class);
		Map<String, Integer> paramMap = new HashMap<String,Integer>();
		paramMap.put("id",id);
		
		return template.queryForObject(sql, paramMap, mapper);// queryForObject() : ��ȸ�� ���ڵ� ���� �� ���� �����
	}
	@Override
	public void create(Item item) {
		String sql = "select nvl(max(id),0) from item";
		int i = template.queryForObject(sql, new HashMap<String,Object>(), Integer.class);
		
		item.setId(++i);
		sql = "insert into item(id,name,price,description,pictureUrl) values(:id, :name, :price, :description, :pictureUrl)";
		
		item.setPictureUrl(item.getPicture().getOriginalFilename());
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(item);
		template.update(sql, paramSource);
	}
	@Override
	public void update(Item item) {
		String sql = "update item set name=:name, price=:price, pictureUrl=:pictureUrl, description=:description where id=:id";
		
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(item);
		template.update(sql, paramSource);
		
	}
	@Override
	public void delete(Integer id) {
		String sql = "delete from item where id=:id";
		Map<String, Integer> paramMap = new HashMap<String,Integer>();
		paramMap.put("id",id);
		template.update(sql, paramMap);
	}

}
